package com.seah.specialsteel.entity;

import com.seah.specialsteel.dto.ResultDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OriResult extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double totalCost;

    @Column
    private double totalAmount;

    @Column
    private double expectOutput;

    @Column
    private String method;

    @Column
    private String comment;

    @Column
    private String title;

    public ResultDTO entityToDTO() {
        return new ResultDTO(id, totalCost, totalAmount, null, expectOutput, null, method, 0, comment, 0, null, title);
    }

}
