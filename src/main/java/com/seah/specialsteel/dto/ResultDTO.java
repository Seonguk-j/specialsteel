package com.seah.specialsteel.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.entity.RevResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;

@Data
@NoArgsConstructor
@JsonSerialize
public class ResultDTO {

    public Long id;
    public double totalCost;                       // 합금철 총 투입비용
    public double totalAmount;                     // 합금철 총 투입량
    public LinkedHashMap<String, String> alloyInputs;    // 합금철별 투입량
    public double expectOutput;                    // 예상 용강량
    public LinkedHashMap<String, String> expectMaterials; // result 예상 성분
    public String method;                          // 방법
    public int index;                           //인덱스
    public String comment = "";                         //메모
    public int length;                              //사이즈
    public OriResult oriResult;                     //저장용

    public RevResult toRevEntity(){
        return new RevResult(id, totalCost, totalAmount, expectOutput, method, comment, oriResult);
    }
    public OriResult toOriEntity(){
        return new OriResult(id, totalCost, totalAmount, expectOutput, method, comment);
    }
    public ResultDTO(String fileName) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        Reader reader = new FileReader(fileName);

        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        this.setTotalCost((Double) jsonObject.get("합금철_총_투입비용"));
        this.setTotalAmount((Double) jsonObject.get("합금철_총_투입량"));
        this.setExpectOutput( (Double) jsonObject.get("예상_용강량"));
        this.setMethod((String) jsonObject.get("방법"));

        //합금철별_투입량 저장
        JSONArray jsonArray = (JSONArray) jsonObject.get("합금철별_투입량");

        this.setAlloyInputs(jsonArrayToHashMap(jsonArray));

        //예상 결과
        jsonArray = (JSONArray) jsonObject.get("result_예상_성분");

        this.setExpectMaterials(jsonArrayToHashMap(jsonArray));

        reader.close();
    }

    public LinkedHashMap<String, String> jsonArrayToHashMap(JSONArray jsonArray) {
        LinkedHashMap<String, String> output = new LinkedHashMap<>();

        for(int i=0; i < jsonArray.size(); i++){
            JSONArray arr = (JSONArray) jsonArray.get(i);
            String key = arr.get(0).toString();
            String value = arr.get(1).toString();
            output.put(key, value);
        }

        return output;
    }
}
