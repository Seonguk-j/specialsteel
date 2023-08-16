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
public class DeCodingKey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChangeOriResult_id")
    private ChangeOriResult changeOriResult;

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
