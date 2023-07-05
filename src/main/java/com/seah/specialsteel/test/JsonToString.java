package com.seah.specialsteel.test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonToString {
    public static void main(String[] args) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        FileInputStream fileInputStream = new FileInputStream("C:/path/ResponseData_N29790.json");
        InputStreamReader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);


        JSONObject jsonObject = (JSONObject) parser.parse(reader);
//        System.out.println(jsonObject.toJSONString());

        System.out.println(jsonObject.toJSONString());

        JSONArray jsonArray = (JSONArray) jsonObject.get("합금철별_투입량");


        Map<String, BigDecimal> map = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray keyValueArray = (JSONArray) jsonArray.get(i);
            String key = keyValueArray.get(0).toString();

            BigDecimal number = new BigDecimal(String.valueOf(keyValueArray.get(1)));

            map.put(key, number);
            System.out.println(key);
            System.out.println(map.get(key));
        }
    }
}