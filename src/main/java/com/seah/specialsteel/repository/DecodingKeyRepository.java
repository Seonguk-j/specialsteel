package com.seah.specialsteel.repository;

import com.seah.specialsteel.entity.ChangeOriResult;
import com.seah.specialsteel.entity.DeCodingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecodingKeyRepository extends JpaRepository<DeCodingKey, Long> {
}
