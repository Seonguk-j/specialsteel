package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.entity.RevResult;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Date;
import java.util.List;

public interface ExpectedResultRepository extends JpaRepository<ExpectedResult, Long> {

    List<ExpectedResult> findByOriResultId(Long id);

    List<ExpectedResult> findByRevResultId(Long id);
//=======
//import java.util.List;
//
//public interface ExpectedResultRepository extends JpaRepository<ExpectedResult, Long> {
//    List<ExpectedResult> findByOriResult(OriResult oriResult);
//    List<ExpectedResult> findByRevResult(RevResult revResult);
//>>>>>>> bong
}
