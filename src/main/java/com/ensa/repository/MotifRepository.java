package com.ensa.repository;

import com.ensa.domain.Motif;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Motif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotifRepository extends JpaRepository<Motif, Long> {
    Motif findMotifByLibelle(String libelle);

    Optional<Motif> findOneMotifByLibelle(String libelle);

    Motif findMotifById(Long id);
}
