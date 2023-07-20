package com.seah.specialsteel.entity;

import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.repository.AlloyInputRepository;
import com.seah.specialsteel.service.HistoryService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.LinkedHashMap;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RevResult extends BaseEntity{

//    private final AlloyInputRepository alloyInputRepository;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OriResult_id")
    private OriResult oriResult;

    public ResultDTO entityToDTO() {
        return new ResultDTO(id, totalCost, totalAmount, null, expectOutput, null, method, 0, comment, 0, null, null);
    }
}
