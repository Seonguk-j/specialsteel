package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.History;
import com.seah.specialsteel.entity.OriResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<History> findByTitleContaining(String keyword);


}
