package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.AlloyInput;
import com.seah.specialsteel.entity.OriResult;
import com.seah.specialsteel.entity.RevResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OriResultRepository extends JpaRepository<OriResult, Long> {
    List<OriResult> findByHistoryId(Long id);


//    List<OriResult> findByModDateBetween(LocalDateTime startDate, LocalDateTime endDate);

//    List<OriResult> findByTitle(String Title);
//    List<OriResult> findByTitleContaining(String keyword);

}
