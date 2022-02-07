package com.ensa.repository;

import com.ensa.domain.Commission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

/**
 * Spring Data SQL repository for the Commission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    Commission findCommissionByDateRetraitAndIdAgent(LocalDate dateRetrait, Long idAgent);
}
