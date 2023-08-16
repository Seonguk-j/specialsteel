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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.CellType.*;
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


    static int decodingShift;
    static double decodingMax;
    static double decodingMin;
    static int decodingMean;
    static int decodingStdDev;


    public void saveData(Map<String, Object> dataMapList) {

        System.out.println(dataMapList);

        ArrayList<ArrayList<Object>> dataList = (ArrayList<ArrayList<Object>>) dataMapList.get("dataKeyList");
        String mode = (String) dataMapList.get("mode");




        if (dataList.isEmpty()) {
            return;
        }


            List<Object> rowData = dataList.get(2); // 첫 번째 행의 데이터를 가져옵니다.
            List<Object> rowData1 = dataList.get(3);
            List<Object> rowData2 = dataList.get(4);
            List<Object> rowData3 = dataList.get(5);
            List<Object> rowData4 = dataList.get(6);
            List<Object> rowData5 = dataList.get(7);

            String title = String.valueOf(rowData.get(0));
            String totalCost = String.valueOf(rowData.get(1));
            double totalAmount = Double.parseDouble(String.valueOf(rowData.get(2)));
            double expectOutput = Double.parseDouble(String.valueOf(rowData.get(3)));
            String method = String.valueOf(rowData.get(4));
            String comment = String.valueOf(rowData.get(5));


            ChangeOriResult changeOriResult = new ChangeOriResult();
            changeOriResult.setTitle(title);
            changeOriResult.setTotalCost(totalCost);
            changeOriResult.setTotalAmount(totalAmount);
            changeOriResult.setExpectOutput(expectOutput);
            changeOriResult.setMethod(method);
            changeOriResult.setComment(comment);


            changeOriResultRepository.save(changeOriResult);


            for (int e = 0; e < rowData1.size(); e++) {
                ChangeAlloyInput changeAlloyInput = new ChangeAlloyInput();
                String name = (String) rowData1.get(e);
                Double amount = Double.valueOf(String.valueOf(rowData2.get(e)));

                changeAlloyInput.setName(name);
                changeAlloyInput.setAmount(amount);

                // changeOriResult 참조를 설정
                changeAlloyInput.setChangeOriResult(changeOriResult);

                changeAlloyInputRepository.save(changeAlloyInput);

            }

            for (int e = 0; e < rowData3.size(); e++) {
                ChangeExpectedResult changeExpectedResult = new ChangeExpectedResult();
                String name = (String) rowData3.get(e);
                Double amount = Double.valueOf(String.valueOf(rowData4.get(e)));

                changeExpectedResult.setName(name);
                changeExpectedResult.setAmount(amount);

                // changeOriResult 참조를 설정
                changeExpectedResult.setChangeOriResult(changeOriResult);

                changeExpectedRepository.save(changeExpectedResult);

            }

        if(mode.equals("standardizationRadio")){
            System.out.println("표준화 디코딩 저장 들어옴");
            DeCodingKey deCodingKey = new DeCodingKey();
            deCodingKey.setMean((Double) rowData5.get(0));
            deCodingKey.setStdDev((Double) rowData5.get(1));
            deCodingKey.setShift((Integer) rowData5.get(2));

            // changeOriResult 참조를 설정
            deCodingKey.setChangeOriResult(changeOriResult);

            decodingKeyRepository.save(deCodingKey);

        }else if(mode.equals("normalizationRadio")){
            System.out.println("정규화 디코딩 저장 들어옴");

            DeCodingKey deCodingKey = new DeCodingKey();
            deCodingKey.setMax((Double) rowData5.get(0));
            if(rowData5.get(1).equals(0)){
                deCodingKey.setMin(0.0);
            }else{
                deCodingKey.setMin((Double) rowData5.get(1));
            }

            deCodingKey.setShift((Integer) rowData5.get(2));

            // changeOriResult 참조를 설정
            deCodingKey.setChangeOriResult(changeOriResult);

            decodingKeyRepository.save(deCodingKey);
        }
        }


    public void saveStandardDecodingkey(List<Object> keyList) {

        if (keyList.isEmpty()) {
            return;
        }
        System.out.println(keyList);

        DeCodingKey deCodingKey = new DeCodingKey();
        deCodingKey.setMean((Double) keyList.get(0));
        deCodingKey.setStdDev((Double) keyList.get(1));
        deCodingKey.setShift((Integer) keyList.get(2));


        decodingKeyRepository.save(deCodingKey);


    }

    public void saveNormalDecodingkey(List<Object> keyList) {

        if (keyList.isEmpty()) {
            return;
        }
        System.out.println(keyList);
        ChangeOriResult changeOriResult = new ChangeOriResult();
        DeCodingKey deCodingKey = new DeCodingKey();
        deCodingKey.setMax((Double) keyList.get(0));
        deCodingKey.setMin((Double) keyList.get(1));
        deCodingKey.setShift((Integer) keyList.get(2));

        decodingKeyRepository.save(deCodingKey);
    }


    public ArrayList<ArrayList<Object>> parseExcelFileToArrayList(MultipartFile file) {
        ArrayList<ArrayList<Object>> data = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                ArrayList<Object> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
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


    //정규화
    public ArrayList<ArrayList<Object>> normalizeExcelData(ArrayList<ArrayList<Object>> excelData) {
        ArrayList<ArrayList<Object>> normalizedExcelData = new ArrayList<>();
        ArrayList<Object> decodingKeyList = new ArrayList<>();
        ArrayList<String> decodingKeyName = new ArrayList<>();

        // 시작 인덱스: 첫 번째 row는 제목이므로 두 번째 row부터 시작
        int startIdx = 1;

        //문자 암호화
        int shift = 3;

        decodingShift = shift;

        // 데이터의 최소값과 최대값을 찾기 위한 초기화
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;

        // 최소값과 최대값을 찾기
        for (int i = startIdx; i < excelData.size(); i++) {
            ArrayList<Object> row = excelData.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) instanceof Number) {
                    double value = ((Number) row.get(j)).doubleValue();
                    minValue = Math.min(minValue, value);
                    maxValue = Math.max(maxValue, value);
                }
            }
        }



        // min-max 정규화를 적용하여 normalizedExcelData에 저장
        for (int i = 0; i < excelData.size(); i++) {
            ArrayList<Object> row = excelData.get(i);
            ArrayList<Object> normalizedRow = new ArrayList<>();
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) instanceof Number) {
                    double value = ((Number) row.get(j)).doubleValue();
                    double normalizedValue = (value - minValue) / (maxValue - minValue);

                    // 소수점 10자리까지 반올림
                    normalizedValue = Math.round(normalizedValue * Math.pow(10, 10)) / Math.pow(10, 10);
                    // 소수점 표기법으로 문자열로 변환하여 저장
                    String formattedValue = String.format("%.10f", normalizedValue);

                    normalizedRow.add(formattedValue);
                } else if (row.get(j) instanceof String) {
                    // 문자열인 경우 오른쪽으로 3만큼 시프트하여 암호화
                    String encryptedString = caesarCipherEncrypt((String) row.get(j), shift);
                    normalizedRow.add(encryptedString);
                }  else {
                    normalizedRow.add(row.get(j));
                }
            }

            normalizedExcelData.add(normalizedRow);
        }

        decodingKeyList.add(maxValue);
        decodingKeyList.add(minValue);
        decodingKeyList.add(shift);

        normalizedExcelData.add(decodingKeyList);

        return normalizedExcelData;
    }


    //표준화
    public ArrayList<ArrayList<Object>> standardizeExcelData(ArrayList<ArrayList<Object>> excelData) {

        ArrayList<Object> decodingKeyList = new ArrayList<>();
        // 입력 데이터의 행과 열 크기 구하기
        int rowCount = excelData.size();

        // 시작 인덱스: 첫 번째 row는 제목이므로 두 번째 row부터 시작
        int startIdx = 1;

        //문자 암호화
        int shift = 3;

        // 최대 열 크기 구하기
        int colCount = 0;
        for (ArrayList<Object> row : excelData) {
            colCount = Math.max(colCount, row.size());
        }

        double meanValue = 0;
        double stddevValue = 0;
        int count = 0;

        // 각 열 평균, 표준편차 계산
        for (int i = startIdx; i < rowCount; i++) {
            ArrayList<Object> row = excelData.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (j < row.size() && isNumeric(row.get(j))) {
                    double value = Double.parseDouble(row.get(j).toString());
                    meanValue += value;
                    count++;
                }
            }
        }

        meanValue /= count;

        // 각 열 표준편차 계산
        for (int i = startIdx; i < rowCount; i++) {
            ArrayList<Object> row = excelData.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (j < row.size() && isNumeric(row.get(j))) {
                    double value = Double.parseDouble(row.get(j).toString());
                    double diff = value - meanValue;
                    stddevValue += diff * diff;
                }
            }
        }

        stddevValue = Math.sqrt(stddevValue / (count - 1));

        // 표준화된 배열 초기화
        ArrayList<ArrayList<Object>> standardizedExcelData = new ArrayList<>();

        // 각 셀의 데이터를 표준화하여 채워넣기
        for (int i = 0; i < rowCount; i++) {
            ArrayList<Object> row = new ArrayList<>();
            int currentColCount = excelData.get(i).size(); // 이 행의 열 개수를 확인
            for (int j = 0; j < currentColCount; j++) {
                if (isNumeric(excelData.get(i).get(j))) {
                    double value = Double.parseDouble(excelData.get(i).get(j).toString());
                    // 표준편차가 0인 경우 표준화를 건너뛰고 원래의 값을 사용합니다.
                    if (stddevValue != 0) {
                        double standardizedValue = (value - meanValue) / stddevValue;
                        row.add(standardizedValue);
                    }else{
                        row.add(value);
                    }
                }else if (excelData.get(i).get(j) instanceof String) {
                    // 문자열인 경우 오른쪽으로 3만큼 시프트하여 암호화
                    String encryptedString = caesarCipherEncrypt((String) excelData.get(i).get(j), shift);
                    row.add(encryptedString);
                }
                else{
                    row.add(excelData.get(i).get(j));
                }
            }

            standardizedExcelData.add(row);
        }
        decodingKeyList.add(meanValue);
        decodingKeyList.add(stddevValue);
        decodingKeyList.add(shift);
        standardizedExcelData.add(decodingKeyList);

        return standardizedExcelData;
    }


    //csv파일 파싱
    public ArrayList<ArrayList<Object>> parseCsvFileToArrayList(MultipartFile file) throws IOException {
        ArrayList<ArrayList<Object>> csvData = new ArrayList<>();

        // 인코딩 목록을 정의합니다.
        List<String> encodings = Arrays.asList("UTF-8", "EUC-KR");

        for (String encoding : encodings) {
            try {
                // 주어진 인코딩으로 파일을 시도합니다.
                CharsetDecoder decoder = Charset.forName(encoding).newDecoder();
                decoder.onMalformedInput(CodingErrorAction.REPORT);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), decoder))) {
                    String line;
                    int rowIndex = 1;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = splitCsvLine(line);

                        // 빈 행이거나 빈 문자열로만 이루어진 행일 경우 건너뜁니다.
                        if (parts.length == 0 || (parts.length == 1 && parts[0].trim().isEmpty())) {
                            continue;
                        }

                        ArrayList<Object> rowData = new ArrayList<>();
                        for (String part : parts) {
                            // 쌍따옴표 제거
                            //part = part.replace("\"", "");
                            if (rowIndex == 3) {
                                rowData.add(part);
                            } else {
                                try {
                                    double num = Double.parseDouble(part);
                                    rowData.add(num);
                                } catch (NumberFormatException e) {
                                    rowData.add(part);
                                }
                            }
                        }
                        csvData.add(rowData);
                        rowIndex++;
                    }
                    // 성공적으로 완료되었습니다.
                    break;
                } catch (CharacterCodingException e) {
                    // 인코딩에 실패했습니다. 다음 인코딩을 시도합니다.
                    csvData.clear();
                }
            } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
                // 인코딩이 지원되지 않거나 없으면 다음 인코딩을 시도합니다.
            }
        }

        if(csvData.isEmpty()) {
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

    public static String caesarCipherDecrypt(String encryptedText, int shift) {
        return caesarCipherEncrypt(encryptedText, 26 - (shift % 26));
    }











}





