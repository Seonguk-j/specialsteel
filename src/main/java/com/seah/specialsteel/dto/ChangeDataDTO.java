package com.seah.specialsteel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDataDTO {

    private String title;
    private List<List<String>> data;
    private List<Double> amounts;

}
