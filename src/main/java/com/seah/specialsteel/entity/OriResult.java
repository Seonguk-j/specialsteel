package com.seah.specialsteel.entity;

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

}
