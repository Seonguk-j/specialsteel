package com.seah.specialsteel.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonSerialize
public class DataDTO {


    private LocalDateTime date;
    private String comment;
    private String name;
    private Double amount;
    private Long id;

}
