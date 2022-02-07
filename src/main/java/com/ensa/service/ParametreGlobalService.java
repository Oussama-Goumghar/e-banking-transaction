package com.ensa.service;

import com.ensa.domain.ParametreGlobal;

import java.util.List;
import java.util.Optional;

public interface ParametreGlobalService {
    int createParametreGlobal(ParametreGlobal parametreGlobal);

    int updateParametreGlobal(Long id,ParametreGlobal parametreGlobal);

    int partialUpdateParametreGlobal(Long id, ParametreGlobal parametreGlobal);

    int deleteParametreGlobal(Long id);

    Optional<ParametreGlobal> findParametreGlobalById(Long id);

    List<ParametreGlobal> findParametreGlobalAll();
}
