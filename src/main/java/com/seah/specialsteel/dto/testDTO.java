package com.seah.specialsteel.dto;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class testDTO {
    public testDTO(String fileName) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        Reader reader = new FileReader(fileName);

        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        reader.close();
    }
}
