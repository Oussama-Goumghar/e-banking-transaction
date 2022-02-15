package com.ensa.service;

import com.ensa.domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    int createTransaction(Transaction transaction, String transactionType, String fraitType);

    int servirTransactionCompte(String referenceTransaction,String numBenificiare);

    int servirTransactionEspece(String referenceTransaction);

    int extournerTransaction(String referenceTransaction);

    int restituerTransaction(String referenceTransaction);

    List<Transaction> blockTransaction(List<Transaction> transactionList);

    List<Transaction> deBlockTransaction(List<Transaction> transactionList);

    int deleteTransactionById(Long id);

    int deleteTransactionByReference(String reference);

    Optional<Transaction> findTransactionById(Long id);

    Transaction findTransactionByReference(String reference);

    List<Transaction> findTransactionAll();

    List<Transaction> findTransactionsBloqued();

    List<Transaction> findTransactionsNotBloqued();


}
