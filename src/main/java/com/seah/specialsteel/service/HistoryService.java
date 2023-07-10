package com.seah.specialsteel.service;

import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.Result;
import com.seah.specialsteel.repository.AlloyInputRepository;
import com.seah.specialsteel.repository.ExpectedResultRepository;
import com.seah.specialsteel.repository.ResultRepository;
import com.seah.specialsteel.tools.ExtractJson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
@AllArgsConstructor
public class HistoryService {

    private final ExtractJson extractJson = new ExtractJson();

    private final ResultRepository resultRepository;

    private final AlloyInputRepository alloyInputRepository;

    private final ExpectedResultRepository expectedResultRepository;

    public void saveHistory(String fileName, ResultDTO resultDTO){

        try {
            //로컬에 있는 json파일 불러와서 JSONObject에 저장
            JSONObject jsonObject = extractJson.extract(fileName);

            resultDTO.setTotalCost((Double) jsonObject.get("합금철_총_투입비용"));
            resultDTO.setTotalAmount((Double) jsonObject.get("합금철_총_투입량"));
            resultDTO.setExpectOutput( (Double) jsonObject.get("예상_용강량"));
            resultDTO.setMethod((String) jsonObject.get("방법"));

            Result result = resultDTO.toEntity();

            Result getResult = resultRepository.save(result);

            //합금철별_투입량 저장
            JSONArray jsonArray = (JSONArray) jsonObject.get("합금철별_투입량");

            HashMap<String,String> alloyInputs = extractJson.jsonArrayToHashMap(jsonArray);
            for(String key : alloyInputs.keySet()){
                 alloyInputRepository.save(new AlloyInput(null, key, Double.parseDouble(alloyInputs.get(key)), getResult));
            }
            //예상 결과
            jsonArray = (JSONArray) jsonObject.get("result_예상_성분");
            HashMap<String,String> expectMaterials = extractJson.jsonArrayToHashMap(jsonArray);
            for(String key : alloyInputs.keySet()){
                expectedResultRepository.save(new ExpectedResult(null, key, Double.parseDouble(alloyInputs.get(key)), getResult));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
