package com.ascoproject.ascoproject.repository;

import com.ascoproject.ascoproject.entity.InfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InfoEntityRepository extends JpaRepository<InfoEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE info_entity RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateInfoEntity();
}
