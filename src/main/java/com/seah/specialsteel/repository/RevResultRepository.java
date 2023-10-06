package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.RevResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevResultRepository extends JpaRepository<RevResult, Long> {
    List<RevResult> findByHistoryId(Long HistoryId);

}
