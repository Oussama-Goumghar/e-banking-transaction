package com.ensa.service.impl;

import com.ensa.domain.TransactionType;
import com.ensa.repository.TransactionTypeRepository;
import com.ensa.service.TransactionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionTypeServiceImpl implements TransactionTypeService {

    @Autowired
    TransactionTypeRepository repository;

    @Override
    public int createTransactionType(TransactionType transactionType) {
        TransactionType transactionTypeCheck = repository.findTransactionTypeByType(transactionType.getType());
        if (transactionTypeCheck == null) {
            repository.save(transactionType);
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int updateTransactionType(Long id, TransactionType transactionType) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.save(transactionType);
            return 1;
        }
    }

    @Override
    public int partialUpdateTransactionType(Long id, TransactionType transactionType) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.findById(id)
                .map(existingTransactionType -> {
                    if (transactionType.getType() != null) {
                        existingTransactionType.setType(transactionType.getType());
                    }
                    if (transactionType.getPlafondTransaction() != null) {
                        existingTransactionType.setPlafondTransaction(transactionType.getPlafondTransaction());
                    }
                    if (transactionType.getPlafondAnnuel() != null) {
                        existingTransactionType.setPlafondAnnuel(transactionType.getPlafondAnnuel());
                    }

                    return existingTransactionType;
                })
                .map(repository::save);
            return 1;
        }
    }

    @Override
    public int deleteTransactionType(Long id) {
        if (!repository.existsById(id)) {
            return -1;
        } else {
            repository.deleteById(id);
            return 1;
        }
    }

    @Override
    public Optional<TransactionType> findTransactionTypeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<TransactionType> findTransactionTypeAll() {
        return repository.findAll();
    }
}
