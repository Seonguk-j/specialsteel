package com.seah.specialsteel.test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonToString {
    public static void main(String[] args) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        Reader reader = new FileReader("C:\\jeongTest\\ResponseData_N29790.json");

        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        double totalCost = (Double) jsonObject.get("합금철_총_투입비용");
        double totalAmount = (Double) jsonObject.get("합금철_총_투입량");

        JSONArray jsonArray = (JSONArray) jsonObject.get("합금철별_투입량");

        double expectOutput = (Double) jsonObject.get("예상_용강량");
        Object expectMaterial = jsonObject.get("result_예상_성분");
        String method = (String) jsonObject.get("방법");

        System.out.println("합금철 총 투입비용 : " + totalCost);
        System.out.println("합금철 총 투입량 : " + totalAmount);
//
//        List<Map<String, Object>> list = JsonUtils.getListMapFromJsonArray(jsonArray);

//        System.out.println(list.get(0).toString());


//
//        JSONObject testobj = new JSONObject();
//        jsonArray.


        for(int i=0; i< jsonArray.size(); i++){
            JSONObject testobj =  (JSONObject) jsonArray.get(i);
            String name = testobj.toString();

        }
//        System.out.println(input.getClass());
//        for(Map<String, Object> input : inputs)
//            System.out.println(input.toString());

    }
}
