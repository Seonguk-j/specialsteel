package com.seah.specialsteel.entity;

import com.seah.specialsteel.dto.ResultDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOriResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String HeatNo;

    @Column
    private String Name;

    @Column
    private double totalCost;

    @Column
    private double totalAmount;

    @Column
    private double expectOutput;

    @Column(name="history_id")
    private Long HistoryId;

    @Column
    private String method;






}
