package com.ascoproject.ascoproject.repository;

import com.ascoproject.ascoproject.entity.TaxInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaxInfoRepository extends JpaRepository<TaxInfoEntity, Long> {
    @Query(value = "select * from tax_info ORDER BY id", nativeQuery = true)
    List<TaxInfoEntity> findAllTaxInfo();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tax_info WHERE id = :id", nativeQuery = true)
    void deleteTaxInfo(@Param("id") Long id);

    TaxInfoEntity findTaxInfoEntityById(Long id);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE tax_info RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateTaxInfo();

    List<TaxInfoEntity> findAllByTaxType(String typeTax);

    @Query("SELECT t.taxType FROM TaxInfoEntity t WHERE t.taxType IS NOT NULL")
    List<String> findAllTaxTypes();


}
