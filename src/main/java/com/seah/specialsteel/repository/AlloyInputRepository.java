package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.AlloyInput;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface AlloyInputRepository extends JpaRepository<AlloyInput, Long> {

    List<AlloyInput> findByOriResultId(Long id);

    List<AlloyInput> findByRevResultId(Long id);

}
