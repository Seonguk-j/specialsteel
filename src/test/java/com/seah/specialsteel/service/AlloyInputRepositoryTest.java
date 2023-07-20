package com.seah.specialsteel.service;

import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.repository.AlloyInputRepository;
import com.seah.specialsteel.repository.OriResultRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AlloyInputRepositoryTest {

    @Autowired
    private AlloyInputRepository alloyInputRepository;

    @Autowired
    private OriResultRepository oriResultRepository;

    @Test
    public void asdfsadf(){
        OriResult oriResult = oriResultRepository.getOne(1L);

        List<AlloyInput> alloyInputs = alloyInputRepository.findByOriResult(oriResult);
        System.out.println(alloyInputs.size());

        for(AlloyInput alloyInput:alloyInputs) {
            System.out.println(alloyInput.getName());
        }
    }
}