package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.ChangeOriResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeOriResultRepository extends JpaRepository<ChangeOriResult, Long> {


    @Query("SELECT MAX(HistoryId) FROM ChangeOriResult")
    Long findMaxHistoryId();
}
