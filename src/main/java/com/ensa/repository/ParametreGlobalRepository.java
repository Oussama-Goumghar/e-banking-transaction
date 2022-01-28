package com.ensa.repository;

import com.ensa.domain.ParametreGlobal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ParametreGlobal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametreGlobalRepository extends JpaRepository<ParametreGlobal, Long> {}
