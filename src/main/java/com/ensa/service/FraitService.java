package com.ensa.service;

import com.ensa.domain.Frait;

import java.util.List;
import java.util.Optional;

public interface FraitService {
    int createFrait(Frait frait);

    int updateFrait(Long id,Frait frait);

    int updateFrait(String type,Frait frait);

    int partialUpdateFrait(Long id, Frait frait);

    int partialUpdateFrait(String type, Frait frait);

    int deleteFraitById(Long id);

    int deleteFraitByType(String type);

    Optional<Frait> findFraitById(Long id);

    List<Frait> findFraitAll();
}
