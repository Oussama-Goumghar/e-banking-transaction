package com.ensa.repository;

import com.ensa.domain.Commission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Commission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    Commission findCommissionByDateRetraitAndNumAgent(LocalDate dateRetrait, String numeroAgent);

    Optional<Commission> findOneCommissionByDateRetraitAndNumAgent(LocalDate dateRetrait, String numeroAgent);
}
