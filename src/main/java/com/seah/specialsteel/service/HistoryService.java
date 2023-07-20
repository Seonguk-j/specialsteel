package com.seah.specialsteel.service;

import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.entity.RevResult;
import com.seah.specialsteel.repository.AlloyInputRepository;
import com.seah.specialsteel.repository.ExpectedResultRepository;
import com.seah.specialsteel.repository.OriResultRepository;
import com.seah.specialsteel.repository.RevResultRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Slf4j
@Service
@AllArgsConstructor
public class HistoryService {


    private final RevResultRepository revResultRepository;

    private final OriResultRepository oriResultRepository;

    private final AlloyInputRepository alloyInputRepository;

    private final ExpectedResultRepository expectedResultRepository;

    public void saveHistory(ResultDTO oriResultDTO,ResultDTO revResultDTO){
        OriResult oriResult = oriResultDTO.toOriEntity();

        //기존 알고리즘 저장
        oriResultRepository.save(oriResult);

        // 기존 알고리즘 합금철별_투입량 저장
        HashMap<String,String> oriAlloyInputs = revResultDTO.getAlloyInputs();
        for(String key : oriAlloyInputs.keySet()){
            alloyInputRepository.save(new AlloyInput(null, key, Double.parseDouble(oriAlloyInputs.get(key)),null, oriResult));
        }

        //기존 알고리즘 예상 성분 저장
        HashMap<String,String> oriExpectMaterials = revResultDTO.getExpectMaterials();
        for(String key : oriExpectMaterials.keySet()){
            expectedResultRepository.save(new ExpectedResult(null, key, Double.parseDouble(oriExpectMaterials.get(key)),null, oriResult));
        }

        revResultDTO.setOriResult(oriResult);

        RevResult revResult = revResultDTO.toRevEntity();

        //수정알고리즘 저장
        revResultRepository.save(revResult);

        //수정알고리즘 합금철별_투입량 저장
        HashMap<String,String> RevAlloyInputs = revResultDTO.getAlloyInputs();
        for(String key : RevAlloyInputs.keySet()){
             alloyInputRepository.save(new AlloyInput(null, key, Double.parseDouble(RevAlloyInputs.get(key)), revResult, null));
        }

        //수정알고리즘 예상 성분
        HashMap<String,String> RevExpectMaterials = revResultDTO.getExpectMaterials();
        for(String key : RevExpectMaterials.keySet()){
            expectedResultRepository.save(new ExpectedResult(null, key, Double.parseDouble(RevExpectMaterials.get(key)), revResult, null));
        }
    }

    public void saveAllHistory(ResultDTO oriResultDTO, List<ResultDTO> revResultDTOList) {
        OriResult oriResult = oriResultDTO.toOriEntity();

        // 기존 알고리즘 저장
        oriResultRepository.save(oriResult);

        // 기존 알고리즘 합금철별_투입량 저장
        HashMap<String, String> oriAlloyInputs = oriResultDTO.getAlloyInputs();
        for (String key : oriAlloyInputs.keySet()) {
            alloyInputRepository.save(new AlloyInput(null, key, Double.parseDouble(oriAlloyInputs.get(key)), null, oriResult));
        }

        // 기존 알고리즘 예상 성분 저장
        HashMap<String, String> oriExpectMaterials = oriResultDTO.getExpectMaterials();
        for (String key : oriExpectMaterials.keySet()) {
            expectedResultRepository.save(new ExpectedResult(null, key, Double.parseDouble(oriExpectMaterials.get(key)), null, oriResult));
        }

        for (ResultDTO revResultDTO : revResultDTOList) {
            revResultDTO.setOriResult(oriResult);
            RevResult revResult = revResultDTO.toRevEntity();

            // 수정 알고리즘 저장
            revResultRepository.save(revResult);

            // 수정 알고리즘 합금철별_투입량 저장
            HashMap<String, String> revAlloyInputs = revResultDTO.getAlloyInputs();
            for (String key : revAlloyInputs.keySet()) {
                alloyInputRepository.save(new AlloyInput(null, key, Double.parseDouble(revAlloyInputs.get(key)), revResult, null));
            }

            // 수정 알고리즘 예상 성분 저장
            HashMap<String, String> revExpectMaterials = revResultDTO.getExpectMaterials();
            for (String key : revExpectMaterials.keySet()) {
                expectedResultRepository.save(new ExpectedResult(null, key, Double.parseDouble(revExpectMaterials.get(key)), revResult, null));
            }
        }
    }


}
