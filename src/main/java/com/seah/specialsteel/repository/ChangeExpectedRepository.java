package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.ChangeExpectedResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeExpectedRepository extends JpaRepository<ChangeExpectedResult, Long> {
}
