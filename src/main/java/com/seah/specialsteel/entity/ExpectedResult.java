package com.seah.specialsteel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExpectedResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RevResult_id")
    private RevResult revResult;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OriResult_id")
    private OriResult oriResult;
}
