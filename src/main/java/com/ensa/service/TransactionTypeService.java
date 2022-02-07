package com.ensa.service;

import com.ensa.domain.TransactionType;

import java.util.List;
import java.util.Optional;

public interface TransactionTypeService {
    int createTransactionType(TransactionType transactionType);

    int updateTransactionType(Long id,TransactionType transactionType);

    int partialUpdateTransactionType(Long id,TransactionType transactionType);

    int deleteTransactionType(Long id);

    Optional<TransactionType> findTransactionTypeById(Long id);

    List<TransactionType> findTransactionTypeAll();

}
