package com.ensa.service;

import com.ensa.domain.Frait;

import java.util.List;
import java.util.Optional;

public interface FraitService {
    int createFrait(Frait frait);

    int updateFrait(Long id,Frait frait);

    int partialUpdateFrait(Long id, Frait frait);

    int deleteFrait(Long id);

    Optional<Frait> findFraitById(Long id);

    List<Frait> findFraitAll();
}
