package com.ensa.service;

import com.ensa.domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    int createTransaction(Transaction transaction, Long motifId, Long transactionTypeId, Long fraitId);

    int deleteTransaction(Long id);

    Optional<Transaction> findTransactionById(Long id);

    List<Transaction> findTransactionAll();
}
