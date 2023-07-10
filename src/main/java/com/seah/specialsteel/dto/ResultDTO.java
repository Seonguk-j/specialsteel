package com.seah.specialsteel.dto;

import com.seah.specialsteel.entity.Result;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
@Data
@NoArgsConstructor
public class ResultDTO {

    public Long id;
    public double totalCost;                       // 합금철 총 투입비용
    public double totalAmount;                     // 합금철 총 투입량
    public HashMap<String, String> alloyInputs;    // 합금철별 투입량
    public double expectOutput;                    // 예상 용강량
    public HashMap<String, String> expectMaterials; // result 예상 성분
    public String method;                          // 방법

    public Result toEntity(){
        return new Result(id, totalCost, totalAmount, expectOutput, method);
    }
}
