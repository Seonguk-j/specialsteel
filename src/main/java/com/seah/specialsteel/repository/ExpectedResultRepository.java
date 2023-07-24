package com.seah.specialsteel.repository;


import com.seah.specialsteel.entity.ExpectedResult;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ExpectedResultRepository extends JpaRepository<ExpectedResult, Long> {

    List<ExpectedResult> findByOriResultId(Long id);

    List<ExpectedResult> findByRevResultId(Long id);


}
