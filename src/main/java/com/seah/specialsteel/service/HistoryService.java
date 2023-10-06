package com.seah.specialsteel.service;

import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.entity.*;
import com.seah.specialsteel.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
@Slf4j
@Service
@AllArgsConstructor
public class HistoryService {


    private final RevResultRepository revResultRepository;

    private final OriResultRepository oriResultRepository;

    private final AlloyInputRepository alloyInputRepository;

    private final ExpectedResultRepository expectedResultRepository;

    private final HistoryRepository historyRepository;


    public void saveAllHistory(String title, List<ResultDTO> oriResultDTOList, List<ResultDTO> revResultDTOList) {

        History history = historyRepository.save(new History(null, title, null));

        for(ResultDTO oriResultDTO : oriResultDTOList) {
            oriResultDTO.setHistory(history);
            OriResult oriResult = oriResultDTO.toOriEntity();
            // 기존 알고리즘 저장
            oriResultRepository.save(oriResult);

            // 기존 알고리즘 합금철별_투입량 저장
            LinkedHashMap<String, String> oriAlloyInputs = oriResultDTO.getAlloyInputs();
            for (String key : oriAlloyInputs.keySet()) {
                alloyInputRepository.save(new AlloyInput(null, key, Double.parseDouble(oriAlloyInputs.get(key)), null, oriResult));
            }

            // 기존 알고리즘 예상 성분 저장
            LinkedHashMap<String, String> oriExpectMaterials = oriResultDTO.getExpectMaterials();
            for (String key : oriExpectMaterials.keySet()) {
                expectedResultRepository.save(new ExpectedResult(null, key, Double.parseDouble(oriExpectMaterials.get(key)), null, oriResult));
            }
        }

        for (ResultDTO revResultDTO : revResultDTOList) {
            revResultDTO.setHistory(history);
            RevResult revResult = revResultDTO.toRevEntity();

            // 수정 알고리즘 저장
            revResultRepository.save(revResult);

            // 수정 알고리즘 합금철별_투입량 저장
            LinkedHashMap<String, String> revAlloyInputs = revResultDTO.getAlloyInputs();
            for (String key : revAlloyInputs.keySet()) {
                alloyInputRepository.save(new AlloyInput(null, key, Double.parseDouble(revAlloyInputs.get(key)), revResult, null));
            }

            // 수정 알고리즘 예상 성분 저장
            LinkedHashMap<String, String> revExpectMaterials = revResultDTO.getExpectMaterials();
            for (String key : revExpectMaterials.keySet()) {
                expectedResultRepository.save(new ExpectedResult(null, key, Double.parseDouble(revExpectMaterials.get(key)), revResult, null));
            }
        }
    }


}
