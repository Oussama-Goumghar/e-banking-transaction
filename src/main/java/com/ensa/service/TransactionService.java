package com.ensa.service;

import com.ensa.domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    int createTransaction(Transaction transaction, String motifLibelle, String transactionType, String fraitType);

    int servirTransaction(String referenceTransaction);

    int extournerTransaction(String referenceTransaction);

    int restituerTransaction(String referenceTransaction);

    int deleteTransaction(Long id);

    Optional<Transaction> findTransactionById(Long id);

    List<Transaction> findTransactionAll();
}
