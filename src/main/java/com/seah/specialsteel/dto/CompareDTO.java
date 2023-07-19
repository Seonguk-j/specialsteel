package com.seah.specialsteel.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.LinkedHashMap;

@JsonSerialize
public class CompareDTO {
    ResultDTO ori;
    ResultDTO rev;
    public LinkedHashMap<String, Double> diffAlloyInputs;
    public LinkedHashMap<String, Double> oriAlloyInputs;
    public LinkedHashMap<String, Double> revAlloyInputs;
    public LinkedHashMap<String, Double> diffMaterials;
    public LinkedHashMap<String, Double> oriMaterials;
    public LinkedHashMap<String, Double> revMaterials;

    public CompareDTO(ResultDTO ori, ResultDTO rev) {
        this.ori = ori;
        this.rev = rev;
        compareAlloyInputs();
        compareMaterials();
    }

    // 합금철별 투입량
    void compareAlloyInputs() {
        LinkedHashMap<String, String> oriHash = ori.getAlloyInputs();
        LinkedHashMap<String, String> revHash = rev.getAlloyInputs();

        diffAlloyInputs = new LinkedHashMap<>();
        oriAlloyInputs = new LinkedHashMap<>();
        revAlloyInputs = new LinkedHashMap<>();

        // 기존 알고리즘에 존재하는 모든 합금철이름 사용
        for(String key : oriHash.keySet()) {
            // 수정알고리즘에 값이 존재할 경우
            if(revHash.containsKey(key)) {
                // 기존알고리즘과 수정알고리즘의 값에 차이가 있을 경우 내용 추가
                if (!oriHash.get(key).equals(revHash.get(key))) {
                    diffAlloyInputs.put(key, Double.parseDouble(oriHash.get(key)) - Double.parseDouble(revHash.get(key)));
                    oriAlloyInputs.put(key, Double.parseDouble(oriHash.get(key)));
                    revAlloyInputs.put(key, Double.parseDouble(revHash.get(key)));
                }
            }
            // 기존알고리즘에 값이 있으나 수정알고리즘에 없을 경우
            else {
                diffAlloyInputs.put(key, Double.parseDouble(oriHash.get(key)));
                oriAlloyInputs.put(key, Double.parseDouble(oriHash.get(key)));
                revAlloyInputs.put(key, 0.0);

            }
        }
        // 기존알고리즘에 값이 없으나 수정알고리즘에 값이 있을 경우
        for(String key : revHash.keySet()) {
            if(!oriHash.containsKey(key)) {
                diffAlloyInputs.put(key, Double.parseDouble(revHash.get(key)));
                oriAlloyInputs.put(key, 0.0);
                revAlloyInputs.put(key, Double.parseDouble(revHash.get(key)));
            }
        }
    }

    // result 예상 성분
     void compareMaterials() {
         LinkedHashMap<String, String> oriHash = ori.getExpectMaterials();
         LinkedHashMap<String, String> revHash = rev.getExpectMaterials();

         diffMaterials = new LinkedHashMap<>();
         oriMaterials = new LinkedHashMap<>();
         revMaterials = new LinkedHashMap<>();

         // 기존 알고리즘에 존재하는 모든 합금철이름 사용
         for(String key : oriHash.keySet()) {
             // 수정알고리즘에 값이 존재할 경우
             if(revHash.containsKey(key)) {
                 // 기존알고리즘과 수정알고리즘의 값에 차이가 있을 경우 내용 추가
                 if (!oriHash.get(key).equals(revHash.get(key))) {
                     diffMaterials.put(key, Double.parseDouble(oriHash.get(key)) - Double.parseDouble(revHash.get(key)));
                     oriMaterials.put(key, Double.parseDouble(oriHash.get(key)));
                     revMaterials.put(key, Double.parseDouble(revHash.get(key)));
                 }
             }
             // 기존알고리즘에 값이 있으나 수정알고리즘에 없을 경우
             else {
                 diffMaterials.put(key, Double.parseDouble(oriHash.get(key)));
                 oriMaterials.put(key, Double.parseDouble(oriHash.get(key)));
                 revMaterials.put(key, 0.0);

             }
         }
         // 기존알고리즘에 값이 없으나 수정알고리즘에 값이 있을 경우
         for(String key : revHash.keySet()) {
             if(!oriHash.containsKey(key)) {
                 diffMaterials.put(key, Double.parseDouble(revHash.get(key)));
                 oriMaterials.put(key, 0.0);
                 revMaterials.put(key, Double.parseDouble(revHash.get(key)));
             }
         }
    }

}