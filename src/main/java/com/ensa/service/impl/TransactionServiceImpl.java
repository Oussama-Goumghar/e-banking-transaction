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
    public int createTransaction(Transaction transaction, String motifLibelle, String transactionType, String fraitType) {

        Motif motifCheck = motifRepository.findMotifByLibelle(motifLibelle);
        TransactionType transactionTypeCheck = transactionTypeRepository.findTransactionTypeByType(transactionType);
        Frait fraitCheck = fraitRepository.findFraitByType(fraitType);

        if (transactionTypeCheck != null && motifCheck != null && fraitCheck != null) {
            transaction.setTransactionType(transactionTypeCheck);
            transaction.setFrait(fraitCheck);
            transaction.setMotif(motifCheck);
            transaction.setStatus(TransactionStatus.ASERVIR.toString());
            transactionRepository.save(transaction);
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int servirTransaction(String referenceTransaction) {
        Transaction transactionCheck = transactionRepository.findTransactionByReference(referenceTransaction);
        if (transactionCheck == null) {
            return -1;
        } else {
            if (transactionCheck.getStatus().equals(TransactionStatus.SERVIE.toString())) {
                return -2;
            } else {
                if (transactionCheck.getStatus().equals(TransactionStatus.BLOQUE.toString())) {
                    return -3;
                } else {
                    if (transactionCheck.getStatus().equals(TransactionStatus.EXTOURNE.toString())) {
                        return -4;
                    } else {
                        if (transactionCheck.getStatus().equals(TransactionStatus.RETITUER.toString())) {
                            return -5;
                        } else {
                            transactionCheck.setStatus(TransactionStatus.SERVIE.toString());
                            transactionRepository.save(transactionCheck);
                            return 1;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int extournerTransaction(String referenceTransaction) {
        Transaction transactionCheck = transactionRepository.findTransactionByReference(referenceTransaction);
        if (transactionCheck == null) {
            return -1;
        } else {
            if (transactionCheck.getStatus().equals(TransactionStatus.EXTOURNE.toString())) {
                return -2;
            } else {
                if (transactionCheck.getStatus().equals(TransactionStatus.BLOQUE.toString())) {
                    return -3;
                } else {
                    if (transactionCheck.getStatus().equals(TransactionStatus.SERVIE.toString())) {
                        return -4;
                    } else {
                        transactionCheck.setStatus(TransactionStatus.EXTOURNE.toString());
                        transactionRepository.save(transactionCheck);
                        return 1;
                    }
                }
            }
        }
    }

    @Override
    public int restituerTransaction(String referenceTransaction) {
        Transaction transactionCheck = transactionRepository.findTransactionByReference(referenceTransaction);
        if (transactionCheck == null) {
            return -1;
        } else {
            if (transactionCheck.getStatus().equals(TransactionStatus.RETITUER.toString())) {
                return -2;
            } else {
                if (transactionCheck.getStatus().equals(TransactionStatus.BLOQUE.toString())) {
                    return -3;
                } else {
                    if (transactionCheck.getStatus().equals(TransactionStatus.SERVIE.toString())) {
                        return -4;
                    } else {
                        transactionCheck.setStatus(TransactionStatus.RETITUER.toString());
                        transactionRepository.save(transactionCheck);
                        return 1;
                    }
                }
            }
        }
    }

    @Override
    public List<Transaction> blockTransaction(List<Transaction> transactionList) {
        return null;
    }


    @Override
    public int deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            return -1;
        } else {
            transactionRepository.deleteById(id);
            return 1;
        }
    }

    @Override
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findTransactionAll() {
        return transactionRepository.findAll();
    }

}
