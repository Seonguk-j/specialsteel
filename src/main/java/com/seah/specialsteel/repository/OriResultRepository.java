package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.OriResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OriResultRepository extends JpaRepository<OriResult, Long> {
    List<OriResult> findByHistoryId(Long id);

}
