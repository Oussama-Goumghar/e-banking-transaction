package com.ensa.service;

import com.ensa.domain.ParametreGlobal;

import java.util.List;
import java.util.Optional;

public interface ParametreGlobalService {
    int createParametreGlobal(ParametreGlobal parametreGlobal);

    int updateParametreGlobal(Long id, ParametreGlobal parametreGlobal);

    int updateParametreGlobal(String key, ParametreGlobal parametreGlobal);

    int partialUpdateParametreGlobal(Long id, ParametreGlobal parametreGlobal);

    int partialUpdateParametreGlobal(String key, ParametreGlobal parametreGlobal);

    int deleteParametreGlobal(Long id);

    int deleteParametreGlobal(String key);

    Optional<ParametreGlobal> findParametreGlobalById(Long id);

    List<ParametreGlobal> findParametreGlobalAll();
}
