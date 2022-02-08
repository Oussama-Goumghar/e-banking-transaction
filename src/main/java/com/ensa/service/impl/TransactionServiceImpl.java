package com.ensa.service.impl;

import com.ensa.domain.Frait;
import com.ensa.domain.Motif;
import com.ensa.domain.Transaction;
import com.ensa.domain.TransactionType;
import com.ensa.repository.FraitRepository;
import com.ensa.repository.MotifRepository;
import com.ensa.repository.TransactionRepository;
import com.ensa.repository.TransactionTypeRepository;
import com.ensa.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionTypeRepository transactionTypeRepository;

    @Autowired
    MotifRepository motifRepository;

    @Autowired
    FraitRepository fraitRepository;

    @Override
    public int createTransaction(Transaction transaction,  String motifLibelle, String transactionType, String fraitType) {

        Motif motifCheck = motifRepository.findMotifByLibelle(motifLibelle);
        TransactionType transactionTypeCheck = transactionTypeRepository.findTransactionTypeByType(transactionType);
        Frait fraitCheck = fraitRepository.findFraitByType(fraitType);

        if (transactionTypeCheck != null && motifCheck != null && fraitCheck != null) {
            transaction.setTransactionType(transactionTypeCheck);
            transaction.setFrait(fraitCheck);
            transaction.setMotif(motifCheck);
            transactionRepository.save(transaction);
            return 1;
        } else {
            return -1;
        }
    }


    @Override
    public int deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            return -1;
        } else {
            transactionRepository.deleteById(id);
            return 1;
        }    }

    @Override
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findTransactionAll() {
        return transactionRepository.findAll();
    }

}
