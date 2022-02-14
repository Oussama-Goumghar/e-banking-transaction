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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public int createTransaction(Transaction transaction, String transactionType, String fraitType) {
        if (transactionRepository.findTransactionByReference(transaction.getReference()) != null) {
            return -1;
        } else {
            Motif motifCheck = motifRepository.findMotifByLibelle("motif 1");
            TransactionType transactionTypeCheck = transactionTypeRepository.findTransactionTypeByType(transactionType);
            Frait fraitCheck = fraitRepository.findFraitByType(fraitType);
            if (transactionTypeCheck == null  && fraitCheck == null) {
                return -2;
            } else {
                transaction.setTransactionType(transactionTypeCheck);
                transaction.setFrait(fraitCheck);
                transaction.setMotif(motifCheck);
                transaction.setStatus(TransactionStatus.ASERVIR.toString());
                transaction.setDateEmission(LocalDate.now());
                transactionRepository.save(transaction);
                return 1;
            }
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
                            TransactionType transactionType = transactionCheck.getTransactionType();
                            Frait frait = transactionCheck.getFrait();

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
        return transactionRepository.saveAll(
            transactionList.stream()
                .map(Transaction::getReference)
                .filter(value -> value != null && !value.isEmpty())
                .map(transactionRepository::findOneTransactionByReference)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(transaction -> transaction.setStatus(TransactionStatus.BLOQUE.toString()))
                .collect(Collectors.toList()));
    }

    @Override
    public List<Transaction> deBlockTransaction(List<Transaction> transactionList) {
        return transactionRepository.saveAll(
            transactionList.stream()
                .map(Transaction::getReference)
                .filter(value -> value != null && !value.isEmpty())
                .map(transactionRepository::findOneTransactionByReference)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(transaction -> transaction.setStatus(TransactionStatus.ASERVIR.toString()))
                .collect(Collectors.toList()));
    }


    @Override
    public int deleteTransactionById(Long id) {
        if (!transactionRepository.existsById(id)) {
            return -1;
        } else {
            transactionRepository.deleteById(id);
            return 1;
        }
    }

    @Override
    public int deleteTransactionByReference(String reference) {
        Transaction transactionToDelete = transactionRepository.findTransactionByReference(reference);
        if (transactionToDelete == null) {
            return -1;
        } else {
            transactionRepository.delete(transactionToDelete);
            return 1;
        }
    }

    @Override
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction findTransactionByReference(String reference) {
        return transactionRepository.findTransactionByReference(reference);
    }

    @Override
    public List<Transaction> findTransactionAll() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> findTransactionsBloqued() {
        return transactionRepository.findTransactionsByStatus(TransactionStatus.BLOQUE.name());
    }

    @Override
    public List<Transaction> findTransactionsNotBloqued() {
        return transactionRepository.findTransactionsByStatusNot(TransactionStatus.BLOQUE.name());
    }


}
