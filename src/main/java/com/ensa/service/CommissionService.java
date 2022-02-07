package com.ensa.service;

import com.ensa.domain.Commission;

import java.util.List;
import java.util.Optional;

public interface CommissionService {
    int createCommission(Commission commission);

    int updateCommission(Long id,Commission commission);

    int partialUpdateCommission(Long id, Commission commission);

    int deleteCommission(Long id);

    Optional<Commission> findCommissionById(Long id);

    List<Commission> findCommissionAll();

}
