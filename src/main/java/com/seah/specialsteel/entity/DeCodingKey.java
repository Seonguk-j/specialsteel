package com.seah.specialsteel.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeCodingKey  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChangeOriResult_id")
    private ChangeOriResult changeOriResultId;

    @Column
    private Long changeOriResultHistoryId;

    @Column
    private String name;

    @Column
    private Double max;

    @Column
    private Double min;

    @Column
    private Double mean;

    @Column
    private Double stdDev;

    @Column
    private int shift;
}
