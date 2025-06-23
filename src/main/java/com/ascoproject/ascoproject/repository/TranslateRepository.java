package com.ascoproject.ascoproject.repository;

import com.ascoproject.ascoproject.entity.TranslateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TranslateRepository extends JpaRepository<TranslateEntity, Integer> {
    Optional<TranslateEntity> findByUz(String uz);
    Optional<TranslateEntity> findByRu(String ru);
}
