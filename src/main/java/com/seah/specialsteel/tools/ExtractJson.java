package com.seah.specialsteel.tools;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

// JSON의 값들을 추출해서 저장하는 클래스
@Getter
@Setter
public class ExtractJson {

//    private double totalCost;                       // 합금철 총 투입비용
//    private double totalAmount;                     // 합금철 총 투입량
//    private HashMap<String, String> alloyInputs;    // 합금철별 투입량
//    private double expectOutput;                    // 예상 용강량
//    private HashMap<String, String> expectMaterials; // result 예상 성분
//    private String method;                          // 방법




//        totalCost = (Double) jsonObject.get("합금철_총_투입비용");
//        totalAmount = (Double) jsonObject.get("합금철_총_투입량");
//
//        JSONArray jsonArray = (JSONArray) jsonObject.get("합금철별_투입량");



//        alloyInputs = jsonArrayToHashMap(jsonArray);

//        expectOutput = (Double) jsonObject.get("예상_용강량");
//
//        jsonArray = (JSONArray) jsonObject.get("result_예상_성분");
//        expectMaterials = jsonArrayToHashMap(jsonArray);
//
//        method = (String) jsonObject.get("방법")

    public JSONObject extract(String fileName)throws IOException, ParseException{
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(fileName);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);



        return jsonObject;
    }

    public HashMap<String, String> jsonArrayToHashMap(JSONArray jsonArray) {
        HashMap<String, String> output = new HashMap<>();

        for(int i=0; i < jsonArray.size(); i++){
            JSONArray arr = (JSONArray) jsonArray.get(i);
            String key = arr.get(0).toString();
            String value = arr.get(1).toString();
            output.put(key, value);
        }

        return output;
    }

}
