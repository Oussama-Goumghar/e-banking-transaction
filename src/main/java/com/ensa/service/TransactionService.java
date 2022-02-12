package com.ensa.service;

import com.ensa.domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    int createTransaction(Transaction transaction, String motifLibelle, String transactionType, String fraitType);

    int servirTransaction(String referenceTransaction);

    int extournerTransaction(String referenceTransaction);

    int restituerTransaction(String referenceTransaction);

    List<Transaction> blockTransaction(List<Transaction> transactionList);

    int deleteTransactionById(Long id);

    int deleteTransactionByReference(String reference);

    Optional<Transaction> findTransactionById(Long id);

    List<Transaction> findTransactionAll();

    List<Transaction> findTransactionsBloqued();

    List<Transaction> findTransactionsNotBloqued();


}
