package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.ExpectedResult;
import com.seah.specialsteel.entity.RevResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RevResultRepository extends JpaRepository<RevResult, Long> {
    List<RevResult> findByModDateBetween(LocalDateTime startDate, LocalDateTime endDate);

//    List<RevResult> findByOriResultId(Long oriResultId);
    List<RevResult> findByHistoryId(Long HistoryId);

}
