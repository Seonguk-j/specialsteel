package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.entity.RevResult;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Date;
import java.util.List;

public interface AlloyInputRepository extends JpaRepository<AlloyInput, Long> {

    List<AlloyInput> findByOriResultId(Long id);

    List<AlloyInput> findByRevResultId(Long id);
//=======
//import java.util.List;
//
//public interface AlloyInputRepository extends JpaRepository<AlloyInput, Long> {
//    List<AlloyInput> findByOriResult(OriResult oriResult);
//    List<AlloyInput> findByRevResult(RevResult revResult);
//>>>>>>> bong
}
