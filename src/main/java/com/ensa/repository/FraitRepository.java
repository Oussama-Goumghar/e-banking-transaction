package com.ensa.repository;

import com.ensa.domain.Frait;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Frait entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FraitRepository extends JpaRepository<Frait, Long> {}
