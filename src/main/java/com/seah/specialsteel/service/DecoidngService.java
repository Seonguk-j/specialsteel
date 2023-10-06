package com.seah.specialsteel.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.query.criteria.internal.ValueHandlerFactory.isNumeric;

@Service
@AllArgsConstructor
public class DecoidngService {


    @Autowired
    private ChangeDataService changeDataService;

    // 엑셀 데이터 복구
    public ArrayList<ArrayList<Object>> denormalizeExcelData(ArrayList<ArrayList<Object>> normalizedExcelData) {
        ArrayList<ArrayList<Object>> denormalizedExcelData = new ArrayList<>();

        // 마지막 세 줄에서 최대값, 최소값 및 shift 값을 가져옴
        int size = normalizedExcelData.size();
        ArrayList<Object> decodingMaxList = normalizedExcelData.get(size - 3);
        ArrayList<Object> decodingMinList = normalizedExcelData.get(size - 2);


        for (int i = 0; i < size - 3; i++) {
            ArrayList<Object> row = normalizedExcelData.get(i);
            ArrayList<Object> denormalizedRow = new ArrayList<>();

            for (int j = 0; j < row.size(); j++) {
                if (i >1 && j>5 && row.get(j) instanceof String && !((String)row.get(j)).equals("Heat 번호") && !((String)row.get(j)).equals("방법")) {
                    double value;
                    try{
                        value= Double.parseDouble((String)row.get(j));
                        double minVal= ((Number)decodingMinList.get(j)).doubleValue();
                        double maxVal= ((Number)decodingMaxList.get(j)).doubleValue();

                        // 원래의 값을 복구함
                        double originalValue= value * (maxVal - minVal) + minVal;
                        denormalizedRow.add(originalValue);
                    }catch(NumberFormatException e){
                        // 문자열이 암호화되었으면 복호화 함
                        //String decryptedString= caesarCipherDecrypt((String)row.get(j),shift);
                        denormalizedRow.add(row.get(j));
                    }
                }else if(row.get(j) instanceof String && i == 1){
                    ArrayList<Object> decryptedString= createDecryptionMapping(normalizedExcelData.get(1));
                    String originColum = (String) decryptedString.get(j);

                    denormalizedRow.add(originColum);
                } else{
                    // Heat번호와 방법은 그대로 둠
                    denormalizedRow.add(row.get(j));
                }
            }
            denormalizedExcelData.add(denormalizedRow);
        }

        return denormalizedExcelData;
    }

    public ArrayList<ArrayList<Object>> denormalizeStandardExcelData(ArrayList<ArrayList<Object>> standardizedData) {
        ArrayList<Object> meanList = standardizedData.get(standardizedData.size()-3);
        ArrayList<Object> stdList = standardizedData.get(standardizedData.size()-2);

        // Heat번호와 방법이 열의 인덱스들에 대한 정보를 저장하세요 (예: 0번과 5번 열)
        int heatNumberColumnIndex = 0;
        int no1 = 2;
        int no2 = 3;
        int no3 = 4;
        int methodColumnIndex = 5;

        ArrayList<ArrayList<Object>> denormalizedData = new ArrayList<>();

        for (ArrayList<Object> row : standardizedData) {
            if(row == meanList || row == stdList){
                continue; // Skip these rows
            }
            ArrayList<Object> denormalizedRow = new ArrayList<>();
            for (int j = 0; j < row.size(); j++) {
                if (isNumeric(row.get(j))) {
                    double value = Double.parseDouble(row.get(j).toString());
                    if(j <6){
                        denormalizedRow.add(value);
                    }else{
                        // 원래의 값을 복구함
                        double originalValue= value * (double)stdList.get(j) + (double)meanList.get(j);
                        denormalizedRow.add(originalValue);
                    }
                }else if(row.get(j) instanceof String && standardizedData.get(1) == row){
                    ArrayList<Object> decryptedString= createDecryptionMapping(standardizedData.get(1));
                    String originColum = (String) decryptedString.get(j);
                    denormalizedRow.add(originColum);
                }
                else{
                    // Heat번호와 방법은 그대로 둠
                    denormalizedRow.add(row.get(j));
                }
            }
            denormalizedData.add(denormalizedRow);
        }

        return denormalizedData;
    }


    public static ArrayList<Object> createDecryptionMapping(ArrayList<Object> encryptionMap){

        Map<String,String> decryptionMap = createDecryptionMap(ChangeDataService.columnMapping);
        ArrayList<Object> originalCol = new ArrayList<>();
        for(int i = 0; i<encryptionMap.size(); i++){
            if(i<6){
                originalCol.add(encryptionMap.get(i));
            }else{
                originalCol.add(decryptionMap.get(encryptionMap.get(i)));
            }

        }
        return originalCol;
    }


    public static Map<String, String> createDecryptionMap(Map<String,String> encryptionMap){
        Map<String,String> decryptionMap1=new HashMap<>();
        for(Map.Entry<String,String> entry : encryptionMap.entrySet()){
            decryptionMap1.put(entry.getValue(), entry.getKey());
        }
        return decryptionMap1;
    }



}
