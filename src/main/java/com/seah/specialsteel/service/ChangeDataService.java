package com.seah.specialsteel.service;

import com.seah.specialsteel.entity.ChangeAlloyInput;
import com.seah.specialsteel.entity.ChangeExpectedResult;
import com.seah.specialsteel.entity.ChangeOriResult;
import com.seah.specialsteel.entity.DeCodingKey;
import com.seah.specialsteel.repository.ChangeAlloyInputRepository;
import com.seah.specialsteel.repository.ChangeExpectedRepository;
import com.seah.specialsteel.repository.ChangeOriResultRepository;
import com.seah.specialsteel.repository.DecodingKeyRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.charset.*;
import java.security.Key;
import java.util.*;
import java.util.regex.Pattern;

import static org.hibernate.query.criteria.internal.ValueHandlerFactory.isNumeric;

@Service
@AllArgsConstructor
public class ChangeDataService {

    @Autowired
    private ChangeOriResultRepository changeOriResultRepository;

    @Autowired
    private ChangeExpectedRepository changeExpectedRepository;

    @Autowired
    private ChangeAlloyInputRepository changeAlloyInputRepository;

    @Autowired
    private DecodingKeyRepository decodingKeyRepository;

    // 매핑 딕셔너리 생성
    static Map<String, String> columnMapping = new HashMap<>();


    public void saveData(Map<String, Object> dataMapList) {

        ArrayList<ArrayList<Object>> dataList = (ArrayList<ArrayList<Object>>) dataMapList.get("dataKeyList");
        String mode = (String) dataMapList.get("mode");

        if (dataList.isEmpty()) {
            return;
        }
        List<List<Object>> changeOriResultList = new ArrayList<>();
        for (int j = 2 ; j<dataList.size()-3; j++) {
            List<Object> changeOriResultList1 = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                changeOriResultList1.add(dataList.get(j).get(i));
            }
            changeOriResultList.add(changeOriResultList1);
        }

        List<List<Object>> changeAlloyInputList = new ArrayList<>();
        for(int q= 2; q<(dataList.size()-3); q++) {
            List<Object> changeAlloyInputList1 = new ArrayList<>();
            for (int i = 6; i < 31; i++) {
                changeAlloyInputList1.add(dataList.get(q).get(i));
            }
            changeAlloyInputList.add(changeAlloyInputList1);
        }

        List<List<Object>> changeExpectedList = new ArrayList<>();
        for (int q= 2; q<(dataList.size()-3); q++){
            List<Object> changeExpectedList1 = new ArrayList<>();
            for(int i = 32; i<dataList.get(2).size() ;i++){
                changeExpectedList1.add(dataList.get(q).get(i));
            }
            changeExpectedList.add(changeExpectedList1);
        }


        List<Object> alloyNameList = new ArrayList<>();
        for(int i = 6; i<31 ;i++){
            alloyNameList.add(dataList.get(1).get(i));
        }
        List<Object> expectedNameList = new ArrayList<>();
        for(int i = 32; i<dataList.get(1).size() ;i++){
            expectedNameList.add(dataList.get(1).get(i));
        }
        List<Object> decodingKeyMaxMeanList = new ArrayList<>();
        for(int i = 6; i<dataList.get(4).size(); i++){
            decodingKeyMaxMeanList.add(dataList.get(dataList.size()-3).get(i));
        }
        List<Object> decodingKeyMinStdList = new ArrayList<>();
        for(int i = 6; i<dataList.get(5).size() ;i++){
            decodingKeyMinStdList.add(dataList.get(dataList.size()-2).get(i));
        }
        List<Object> decodingKeyAllNameList = new ArrayList<>();
        for(int i = 6; i<dataList.get(1).size(); i++){
            decodingKeyAllNameList.add(dataList.get(1).get(i));
        }
        int shift = (int) dataList.get(dataList.size()-1).get(0);

        // 최대 historyId 값 조회
        Long maxHistoryId = changeOriResultRepository.findMaxHistoryId();
        if (maxHistoryId == null) {
            maxHistoryId = 1L;
        }else{
            maxHistoryId = maxHistoryId+1;
        }

        int totalAmount = 0;
        int heatNo = 0;
        int algorithms = 0;
        int totalCost = 0;
        int method = 0;
        int expectOutput = 0;

        for(int j =0;j<6;j++){
            if(dataList.get(1).get(j).equals("합금철 총 투입량")){
                totalAmount = j;
            }else if(dataList.get(1).get(j).equals("Heat 번호")){
                heatNo = j;
            }else if(dataList.get(1).get(j).equals("알고리즘(기존/수정)")){
                algorithms = j;
            }else if(dataList.get(1).get(j).equals("합금철 총 투입비용")){
                totalCost = j;
            }else if(dataList.get(1).get(j).equals("방법")){
                method = j;
            }else if(dataList.get(1).get(j).equals("예상용강량")){
                expectOutput = j;
            }
        }

        for (int i = 0; i < changeOriResultList.size(); i ++) {
            ChangeOriResult changeOriResult = new ChangeOriResult();
            changeOriResult.setHeatNo((String) changeOriResultList.get(i).get(heatNo));
            changeOriResult.setName((String) changeOriResultList.get(i).get(algorithms));
            changeOriResult.setTotalAmount(Double.parseDouble(changeOriResultList.get(i).get(totalAmount).toString()));
            Object value = changeOriResultList.get(i).get(totalCost);
            if(value instanceof Double){
                changeOriResult.setTotalCost((Double) changeOriResultList.get(i).get(totalCost));
            }else if(value instanceof String){
                changeOriResult.setTotalCost(Double.parseDouble((String) changeOriResultList.get(i).get(totalCost)));
            }
            changeOriResult.setMethod((String) changeOriResultList.get(i).get(method));
            changeOriResult.setExpectOutput(Double.parseDouble(changeOriResultList.get(i).get(expectOutput).toString()));
            changeOriResult.setHistoryId(maxHistoryId);
            changeOriResultRepository.save(changeOriResult);

            for (int e = 0; e < alloyNameList.size(); e++) {
                ChangeAlloyInput changeAlloyInput = new ChangeAlloyInput();

                String name = (String) alloyNameList.get(e);
                changeAlloyInput.setName(name);
                Double amount = null;

                if (mode.equals("standardizationRadio")){
                    if(changeAlloyInputList.get(i).get(e).equals(0)){
                        amount = (double) 0;
                    }else if(changeAlloyInputList.get(i).get(e) instanceof Integer){
                        amount = ((Integer) changeAlloyInputList.get(i).get(e)).doubleValue();
                    }else if(changeAlloyInputList.get(i).get(e) instanceof String){
                        String valueStr = changeAlloyInputList.get(i).get(e).toString();
                        int endIndex = valueStr.indexOf("(");
                        String numericValueStr = valueStr.substring(0, endIndex);
                        amount = Double.parseDouble(numericValueStr);
                    }else{
                        amount = (Double) changeAlloyInputList.get(i).get(e);
                    }
                }else {
                    amount = Double.parseDouble((String) changeAlloyInputList.get(i).get(e));
                }

                changeAlloyInput.setAmount(amount);

                // changeOriResult 참조를 설정
                changeAlloyInput.setChangeOriResult(changeOriResult);
                changeAlloyInput.setChangeOriResultHistoryId(maxHistoryId);
                changeAlloyInputRepository.save(changeAlloyInput);

            }


            for (int e = 0; e < expectedNameList.size(); e++) {

                ChangeExpectedResult changeExpectedResult = new ChangeExpectedResult();

                String name = (String) expectedNameList.get(e);
                Double amount = null;
                changeExpectedResult.setName(name);
                if (mode.equals("standardizationRadio")){
                    if(changeExpectedList.get(i).get(e).equals(0)){
                        amount = (double) 0;
                    }else if(changeExpectedList.get(i).get(e) instanceof Integer){
                        amount = ((Integer) changeExpectedList.get(i).get(e)).doubleValue();
                    }else if(changeExpectedList.get(i).get(e) instanceof String){
                        String valueStr = changeExpectedList.get(i).get(e).toString();
                        int endIndex = valueStr.indexOf("(");
                        String numericValueStr = valueStr.substring(0, endIndex);
                        amount = Double.parseDouble(numericValueStr);
                    }else{
                        amount = (Double) changeExpectedList.get(i).get(e);
                    }
                }else {
                    amount = Double.parseDouble((String) changeExpectedList.get(i).get(e));
                }

                changeExpectedResult.setAmount(amount);

                // changeOriResult 참조 설정
                changeExpectedResult.setChangeOriResult(changeOriResult);
                changeExpectedResult.setChangeOriResultHistoryId(maxHistoryId);
                changeExpectedRepository.save(changeExpectedResult);

            }

            if (mode.equals("standardizationRadio")) {
                for (int p = 0; p < decodingKeyMaxMeanList.size(); p++) {
                    DeCodingKey deCodingKey = new DeCodingKey();
                    deCodingKey.setName((String) decodingKeyAllNameList.get(p));

                    Object mean = decodingKeyMaxMeanList.get(p);
                    if (mean instanceof Double) {
                        deCodingKey.setMean((Double) mean);
                    } else if (mean instanceof Integer) {
                        deCodingKey.setMean(((Integer) mean).doubleValue());
                    }

                    Object stdDev = decodingKeyMinStdList.get(p);
                    if (stdDev instanceof Double) {
                        deCodingKey.setStdDev((Double) stdDev);
                    } else if (stdDev instanceof Integer) {
                        deCodingKey.setStdDev(((Integer) stdDev).doubleValue());
                    }

                    deCodingKey.setShift(shift);

                    // changeOriResult 참조를 설정
                    deCodingKey.setChangeOriResultId(changeOriResult);
                    deCodingKey.setChangeOriResultHistoryId(changeOriResult.getHistoryId());


                    decodingKeyRepository.save(deCodingKey);
                }
            } else if (mode.equals("normalizationRadio")) {

                for (int a = 0; a < decodingKeyMaxMeanList.size(); a++) {
                    DeCodingKey deCodingKey = new DeCodingKey();
                    deCodingKey.setName((String) decodingKeyAllNameList.get(a));

                    Object max = decodingKeyMaxMeanList.get(a);
                    if (max instanceof Double) {
                        deCodingKey.setMax((Double) max);
                    } else if (max instanceof Integer) {
                        deCodingKey.setMax(((Integer) max).doubleValue());
                    }

                    Object minObject = decodingKeyMinStdList.get(a);
                    if (minObject.equals(0)) {
                        deCodingKey.setMin(0.0);
                    } else {
                        if (minObject instanceof Double) {
                            deCodingKey.setMin((Double) minObject);
                        } else if (minObject instanceof Integer) {
                            deCodingKey.setMin(((Integer) minObject).doubleValue());
                        }
                    }

                    deCodingKey.setShift(shift);

                    // changeOriResult 참조를 설정
                    deCodingKey.setChangeOriResultId(changeOriResult);
                    deCodingKey.setChangeOriResultHistoryId(changeOriResult.getHistoryId());

                    decodingKeyRepository.save(deCodingKey);
                }
            }
        }

    }

    //엑셀 파일 파싱
    public ArrayList<ArrayList<Object>> parseExcelFileToArrayList(MultipartFile file) {
        ArrayList<ArrayList<Object>> data = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter dataFormatter = new DataFormatter();

            for (Row row : sheet) {
                ArrayList<Object> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            String cellValue = dataFormatter.formatCellValue(cell);
                            try {
                                double numericValue = Double.parseDouble(cellValue);
                                rowData.add(numericValue);
                            } catch (NumberFormatException e) {
                                rowData.add(cellValue);
                            }
                            break;

                        case NUMERIC:
                            rowData.add(cell.getNumericCellValue());
                            break;

                        case BOOLEAN:
                            rowData.add(Boolean.toString(cell.getBooleanCellValue()));
                            break;

                        case FORMULA:
                            rowData.add(cell.getCellFormula());
                            break;

                        default:
                            rowData.add("");
                    }
                }
                data.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }



    //엑셀 정규화
    public ArrayList<ArrayList<Object>> normalizeExcelData(ArrayList<ArrayList<Object>> excelData) {
        ArrayList<ArrayList<Object>> normalizedExcelData = new ArrayList<>();
        ArrayList<Object> decodingMaxList = new ArrayList<>();
        ArrayList<Object> decodingMinList = new ArrayList<>();
        ArrayList<Object> decodingShiftList = new ArrayList<>();
        int startIdx = 1;
        int shift = 3;

        // 열 단위로 최소값과 최대값을 저장할 배열 리스트
        ArrayList<Object> minValuesPerColumn = new ArrayList<>();
        ArrayList<Object> maxValuesPerColumn = new ArrayList<>();

        // 첫 번째 row를 처리 (제목)
        ArrayList<Object> firstRow = excelData.get(1);
        for (int j = 0; j < firstRow.size(); j++) {
            minValuesPerColumn.add(Double.MAX_VALUE);
            maxValuesPerColumn.add(Double.MIN_VALUE);
        }

        for (int i = startIdx; i < excelData.size(); i++) {
            ArrayList<Object> row = excelData.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) instanceof Number && j !=0) {
                    double value = ((Number)row.get(j)).doubleValue();
                    minValuesPerColumn.set(j, Math.min((Double) minValuesPerColumn.get(j), value));
                    maxValuesPerColumn.set(j, Math.max((Double) maxValuesPerColumn.get(j), value));
                }else if(j==0){
                    minValuesPerColumn.set(j, "최소값");
                    maxValuesPerColumn.set(j, "최대값");
                }
            }
        }

        // Heat번호와 방법이 열의 인덱스들에 대한 정보를 저장하세요 (예: 0번과 5번 열)
        int heatNumberColumnIndex = 0;
        int no1 = 2;
        int no2 = 3;
        int no3 = 4;
        int methodColumnIndex = 5;

        for (int i = 0; i < excelData.size(); i++) {
            ArrayList<Object> row = excelData.get(i);

            ArrayList<Object> normalizedRow = new ArrayList<>();
            ArrayList<Object> originRow = new ArrayList<>();
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) instanceof Number && j != 1 && j != 2 && j != 3 && j != 4 && j != 5 && j != 0) {
                    double value = ((Number) row.get(j)).doubleValue();
                    double normalizedValue = (value - (double) minValuesPerColumn.get(j)) / ((double) maxValuesPerColumn.get(j) - (double) minValuesPerColumn.get(j));

                    normalizedValue = Math.round(normalizedValue * Math.pow(10, 10)) / Math.pow(10, 10);
                    String formattedValue = String.format("%.10f", normalizedValue);
                    normalizedRow.add(formattedValue);
                } else if (row.get(j) instanceof String) {
                    String value = (String) row.get(j);
                    if(value.equals("Heat 번호")){
                        heatNumberColumnIndex = j;
                    }else if(value.equals("방법")){
                        methodColumnIndex = j;
                    }
                    // Heat번호와 방법 열의 값은 암호화하지 않음
                    if (j == heatNumberColumnIndex || j == methodColumnIndex || j == 1 || j == 2 || j == 3 || j == 4) {
                        normalizedRow.add(value);
                    }else{
                        Map<String ,String> encryptedString = createEncryptionMapping(excelData.get(1));
                        String columString = encryptedString.get(value);
                        normalizedRow.add(columString);
                    }

                } else {
                    normalizedRow.add(row.get(j));
                }
            }
            normalizedExcelData.add(normalizedRow);
        }

        for (int j = 0; j < firstRow.size(); j++) {
            decodingMaxList.add(maxValuesPerColumn.get(j));
            decodingMinList.add(minValuesPerColumn.get(j));
        }
        decodingShiftList.add(shift);

        normalizedExcelData.add(decodingMaxList);
        normalizedExcelData.add(decodingMinList);
        normalizedExcelData.add(decodingShiftList);

        return normalizedExcelData;
    }

    //엑셀 표준화
    public ArrayList<ArrayList<Object>> standardizeExcelData(ArrayList<ArrayList<Object>> excelData) {
        ArrayList<Object> decodingMeanList = new ArrayList<>();
        ArrayList<Object> decodingStdList = new ArrayList<>();
        ArrayList<Object> decodingShiftList = new ArrayList<>();
        int startIdx = 1;
        int shift = 3;

        System.out.println("체크 - "+excelData);

        int rowCount = excelData.size();
        int colCount = 0;
        for (ArrayList<Object> row : excelData) {
            colCount = Math.max(colCount, row.size());
        }

        ArrayList<Object> meanValuesPerColumn = new ArrayList<>();
        ArrayList<Object> stddevValuesPerColumn = new ArrayList<>();
        ArrayList<Integer> countPerColumn = new ArrayList<>();

        // 첫 번째 row를 처리 (제목)
        ArrayList<Object> firstRow = excelData.get(1);
        for (int j = 0; j < firstRow.size(); j++) {
            meanValuesPerColumn.add(0.0);
            stddevValuesPerColumn.add(0.0);
            countPerColumn.add(0);
        }

        // 각 열 평균 계산
        for (int i = startIdx; i < rowCount; i++) {
            ArrayList<Object> row = excelData.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (isNumeric(row.get(j))) {
                    double value = Double.parseDouble(row.get(j).toString());

                    if(j==0){
                        meanValuesPerColumn.set(j,"평균값");
                    }else{
                        meanValuesPerColumn.set(j, (double)meanValuesPerColumn.get(j) + value);
                    }
                    countPerColumn.set(j, countPerColumn.get(j) + 1);
                }
            }
        }

        for (int j = 0; j < colCount; j++) {
            if(j!=0){
                meanValuesPerColumn.set(j, (double)meanValuesPerColumn.get(j) / countPerColumn.get(j));
            }

        }

        // 각 열의 제곱합 계산
        ArrayList<Double> sumOfSquaresPerColumn = new ArrayList<>(Collections.nCopies(colCount, 0.0));
        for (int i = startIdx; i < rowCount; i++) {
            ArrayList<Object> row = excelData.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (isNumeric(row.get(j)) && j!=0) {
                    double value = Double.parseDouble(row.get(j).toString());
                    double diff = value - (Double) meanValuesPerColumn.get(j);
                    sumOfSquaresPerColumn.set(j, sumOfSquaresPerColumn.get(j) + diff * diff);
                }
            }
        }

        // 각 열의 표준편차 계산
        for (int j = 0; j < colCount; j++) {
            if(j==0){
                stddevValuesPerColumn.set(j, "표준편차");
            }else{
                stddevValuesPerColumn.set(j, Math.sqrt(sumOfSquaresPerColumn.get(j) / (countPerColumn.get(j))));
            }

        }

        ArrayList<ArrayList<Object>> standardizedExcelData = new ArrayList<>();

        // Heat번호와 방법이 열의 인덱스들에 대한 정보를 저장하세요 (예: 0번과 5번 열)
        int heatNumberColumnIndex = 0;
        int no1 = 2;
        int no2 = 3;
        int no3 = 4;
        int methodColumnIndex = 5;

        for (int i = 0; i < rowCount; i++) {
            ArrayList<Object> row = excelData.get(i);
            ArrayList<Object> standardizedRow = new ArrayList<>();
            for (int j = 0; j < row.size(); j++) {
                if (isNumeric(row.get(j))) {
                    double value = Double.parseDouble(row.get(j).toString());
                    if(j < 6){
                        standardizedRow.add(value);
                    }else if(j>5 && (double)stddevValuesPerColumn.get(j) != 0){
                        double standardizedValue = (value - (double)meanValuesPerColumn.get(j)) / (double) stddevValuesPerColumn.get(j);
                        standardizedRow.add(standardizedValue);
                    }else{
                        standardizedRow.add(value+"(원값)");
                    }

                } else if (row.get(j) instanceof String) {
                    String value = (String) row.get(j);
                    System.out.println("체크1 - "+value);


                    if(value.equals("Heat 번호")){
                        heatNumberColumnIndex = j;
                    }else if(value.equals("방법")){
                        methodColumnIndex = j;
                    }

                    // Heat번호와 방법 열의 값은 암호화하지 않음
                    if (j<6) {
                        System.out.println("체크2 - "+value);
                        standardizedRow.add(value);
                    }else {
                        Map<String ,String> encryptedString = createEncryptionMapping(excelData.get(1));
                        String columString = encryptedString.get(value);
                        standardizedRow.add(columString);
                    }
                } else {
                    standardizedRow.add(row.get(j));
                }
            }
            standardizedExcelData.add(standardizedRow);
        }

        for (int j = 0; j < firstRow.size(); j++) {
            decodingMeanList.add(meanValuesPerColumn.get(j));
            decodingStdList.add(stddevValuesPerColumn.get(j));
        }
        decodingShiftList.add(shift);

        standardizedExcelData.add(decodingMeanList);
        standardizedExcelData.add(decodingStdList);
        standardizedExcelData.add(decodingShiftList);

        return standardizedExcelData;
    }

    //csv파일 파싱
    public ArrayList<ArrayList<Object>> parseCsvFileToArrayList(MultipartFile file) throws IOException {
        ArrayList<ArrayList<Object>> csvData = new ArrayList<>();

        List<String> encodings = Arrays.asList("UTF-8", "EUC-KR");

        List<String> targetHeaders = Arrays.asList("합금철 총 투입비용", "합금철 총 투입량", "예상용강량");
        Map<String, Integer> targetColumnIndexes = new HashMap<>();

        for (String encoding : encodings) {
            try {
                CharsetDecoder decoder = Charset.forName(encoding).newDecoder();
                decoder.onMalformedInput(CodingErrorAction.REPORT);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), decoder))) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] parts = splitCsvLine(line);

                        if (targetColumnIndexes.isEmpty()) {
                            for (int i = 0; i < parts.length; i++) {
                                String part = parts[i].trim();
                                if (targetHeaders.contains(part)) {
                                    targetColumnIndexes.put(part, i);
                                }
                            }
                        }

                        ArrayList<Object> rowData = new ArrayList<>();
                        for (int i = 0; i < parts.length; i++) {
                            String part = parts[i];
                            String trimmedPart = part.trim();

                            // 큰따옴표 제거
                            if (trimmedPart.startsWith("\"") && trimmedPart.endsWith("\"")) {
                                trimmedPart = trimmedPart.substring(1, trimmedPart.length() - 1);
                            }

                            if (targetColumnIndexes.containsValue(i)) {
                                rowData.add(trimmedPart);
                            } else {
                                try {
                                    double num = Double.parseDouble(trimmedPart);
                                    rowData.add(num);
                                } catch (NumberFormatException e) {
                                    rowData.add(trimmedPart);
                                }
                            }
                        }
                        csvData.add(rowData);
                    }

                    break;
                } catch (CharacterCodingException e) {
                    csvData.clear();
                }
            } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            }
        }

        if (csvData.isEmpty()) {
            throw new IOException("파일 인코딩을 정확히 감지할 수 없습니다.");
        }

        return csvData;
    }

    private String[] splitCsvLine(String line) {
        Pattern pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String[] parts = pattern.split(line);
        return parts;
    }


    public static String caesarCipherEncrypt(String plainText, int shift) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (Character.isUpperCase(c)) {
                encrypted.append((char) (((c - 'A' + shift) % 26) + 'A'));
            } else if (Character.isLowerCase(c)) {
                encrypted.append((char) (((c - 'a' + shift) % 26) + 'a'));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }

    public static Map<String, String> createEncryptionMapping(ArrayList<Object> columns) {
        // 새로운 컬럼 이름 (AAA, BBB, CCC, ...) 생성
        List<String> newColumns = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            char baseChar = 'A';
            char firstChar = (char)(baseChar + (i / 26 / 26 % 26));
            char secondChar = (char)(baseChar + (i / 26 % 26));
            char thirdChar = (char)(baseChar + (i % 26));
            newColumns.add("" + firstChar + secondChar + thirdChar);
        }


        for (int i = 0; i < columns.size(); i++) {
            columnMapping.put((String) columns.get(i), newColumns.get(i));
        }

        return columnMapping;
    }

}





