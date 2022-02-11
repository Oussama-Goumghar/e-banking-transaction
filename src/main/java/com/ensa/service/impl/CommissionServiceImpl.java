package com.ensa.service.impl;

import com.ensa.domain.Commission;
import com.ensa.repository.CommissionRepository;
import com.ensa.service.CommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommissionServiceImpl implements CommissionService {

    @Autowired
    CommissionRepository repository;

    @Override
    public int createCommission(Commission commission) {
        Commission commissionCheck = repository.findCommissionByDateRetraitAndNumAgent(commission.getDateRetrait(), commission.getNumAgent());
        if (commissionCheck == null) {
            repository.save(commission);
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int updateCommission(Long id,Commission commission) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.save(commission);
            return 1;
        }
    }

    @Override
    public int updateCommission(String numClient, LocalDate dateRetrait, Commission commission) {
        Commission commissionToUpdate = repository.findCommissionByDateRetraitAndNumAgent(dateRetrait,numClient);
        if (commissionToUpdate == null) {
            return -1;
        } else {
            repository.delete(commissionToUpdate);
            return 1;
        }
    }

    @Override
    public int partialUpdateCommission(Long id, Commission commission) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.findById(id)
                .map(existingCommission -> {
                    if (commission.getNumAgent() != null) {
                        existingCommission.setNumAgent(commission.getNumAgent());
                    }
                    if (commission.getDateRetrait() != null) {
                        existingCommission.setDateRetrait(commission.getDateRetrait());
                    }
                    if (commission.getValue() != null) {
                        existingCommission.setValue(commission.getValue());
                    }

                    return existingCommission;
                })
                .map(repository::save);
            return 1;
        }
    }

    @Override
    public int partialUpdateCommission(String numClient, LocalDate dateRetrait, Commission commission) {
        Optional<Commission> commissionToUpdate = repository.findOneCommissionByDateRetraitAndNumAgent(dateRetrait,numClient);
        if (commissionToUpdate.isEmpty()) {
            return -1;
        } else {
            commissionToUpdate
                .map(existingCommission -> {
                    if (commission.getNumAgent() != null) {
                        existingCommission.setNumAgent(commission.getNumAgent());
                    }
                    if (commission.getDateRetrait() != null) {
                        existingCommission.setDateRetrait(commission.getDateRetrait());
                    }
                    if (commission.getValue() != null) {
                        existingCommission.setValue(commission.getValue());
                    }

                    return existingCommission;
                })
                .map(repository::save);
            return 1;
        }
    }

    @Override
    public int deleteCommission(Long id) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.deleteById(id);
            return 1;
        }
    }

    @Override
    public int deleteCommission(String numClient, LocalDate dateRetrait) {
        Commission commissionToDelete = repository.findCommissionByDateRetraitAndNumAgent(dateRetrait,numClient);
        if (commissionToDelete == null) {
            return -1;
        } else {
            repository.delete(commissionToDelete);
            return 1;
        }
    }

    @Override
    public Optional<Commission> findCommissionById(Long id) {
        return repository.findById(id);
    }


    @Override
    public List<Commission> findCommissionAll() {
        return repository.findAll();
    }
}
