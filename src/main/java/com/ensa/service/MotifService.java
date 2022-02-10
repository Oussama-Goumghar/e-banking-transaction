package com.ensa.service;


import com.ensa.domain.Motif;

import java.util.List;
import java.util.Optional;

public interface MotifService {
    int createMotif(Motif motif);

    int updateMotif(Long id, Motif motif);

    int updateMotif(String libelle, Motif motif);

    int partialUpdateMotif(Long id, Motif motif);

    int partialUpdateMotif(String libelle, Motif motif);

    int deleteMotif(Long id);

    int deleteMotif(String libelle);


    Optional<Motif> findMotifById(Long id);

    List<Motif> findMotifAll();
}
