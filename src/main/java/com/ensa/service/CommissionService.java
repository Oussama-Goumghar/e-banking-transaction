package com.ensa.service;

import com.ensa.domain.Commission;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommissionService {
    int createCommission(Commission commission);

    int updateCommission(Long id,Commission commission);

    int updateCommission(String numClient, LocalDate dateRetrait,Commission commission);

    int partialUpdateCommission(Long id, Commission commission);

    int partialUpdateCommission(String numClient, LocalDate dateRetrait,Commission commission);

    int deleteCommission(Long id);

    int deleteCommission(String numClient, LocalDate dateRetrait);

    Optional<Commission> findCommissionById(Long id);

    List<Commission> findCommissionAll();

}
