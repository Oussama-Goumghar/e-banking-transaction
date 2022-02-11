package com.ensa.repository;

import com.ensa.domain.ParametreGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the ParametreGlobal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametreGlobalRepository extends JpaRepository<ParametreGlobal, Long> {
    ParametreGlobal findParametreGlobalByKey(String key);

    Optional<ParametreGlobal> findOneParametreGlobalByKey(String key);

}
