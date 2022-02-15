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
import com.ensa.service.proxy.AccountApiProxy;
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

    @Autowired
    private AccountApiProxy accountApiProxy2;

    @Override
    public int createTransaction(Transaction transaction, String transactionType, String fraitType) {
        if (transactionRepository.findTransactionByReference(transaction.getReference()) != null) {
            return -1;
        } else {
            Motif motifCheck = motifRepository.findMotifByLibelle("motif 1");
            TransactionType transactionTypeCheck = transactionTypeRepository.findTransactionTypeByType(transactionType);
            Frait fraitCheck = fraitRepository.findFraitByType(fraitType);
            if (transactionTypeCheck == null && fraitCheck == null) {
                return -2;
            } else {
                transaction.setTransactionType(transactionTypeCheck);
                transaction.setFrait(fraitCheck);
                transaction.setMotif(motifCheck);

                if (transactionType.equals("espece")) {
                    System.out.println(String.format("%s %s %s", "transaction-benificiare".equals(fraitCheck.getType()), transaction.getMontant(), fraitCheck.getMontant()));

                    if (!"transaction-benificiare".equals(fraitCheck.getType())) {
                        transaction.setMontant(transaction.getMontant() - fraitCheck.getMontant());
                    }
                    if (transaction.getNotify() == true) {
                        transaction.setMontant(transaction.getMontant() - 5);
                    }

                    System.out.println(transaction.getMontant());
                    accountApiProxy2.creditAccountAgent(transaction.getLoginAgent(), transaction.getMontant());

                }
                if (transactionType.equals("compte")) {
                    if (transaction.getNotify() == true) {
                        transaction.setMontant(transaction.getMontant() + 5);
                    }
                    accountApiProxy2.debitCompteClient(transaction.getNumClient(), transaction.getMontant());
                }

                transaction.setStatus(TransactionStatus.ASERVIR.toString());
                transaction.setDateEmission(LocalDate.now());
                transactionRepository.save(transaction);
                return 1;
            }
        }
    }

    @Override
    public int servirTransactionCompte(String referenceTransaction, String numBenificiare) {
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
                            Frait frait = transactionCheck.getFrait();

                            int resCreditCompteClient = 0;
                            if (frait.getType().equals("transaction-benificiare")) {
                                resCreditCompteClient = accountApiProxy2.crediteCompteClient(numBenificiare, transactionCheck.getMontant() - frait.getMontant());
                            }
                            if (frait.getType().equals("transaction-donneur")) {
                                accountApiProxy2.debitCompteClient(transactionCheck.getNumClient(), frait.getMontant());
                                resCreditCompteClient = accountApiProxy2.crediteCompteClient(numBenificiare, transactionCheck.getMontant());
                            }
                            if (frait.getType().equals("transaction-partege")) {
                                accountApiProxy2.debitCompteClient(transactionCheck.getNumClient(), frait.getMontant());
                                resCreditCompteClient = accountApiProxy2.crediteCompteClient(numBenificiare, transactionCheck.getMontant() - frait.getMontant());
                            }
                            if (resCreditCompteClient == 1) {
                                transactionCheck.setStatus(TransactionStatus.SERVIE.toString());
                                transactionRepository.save(transactionCheck);
                                return 1;
                            } else {
                                return -6;
                            }

                        }
                    }
                }
            }
        }
    }


    @Override
    public int servirTransactionEspece(String referenceTransaction) {
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
                            Frait frait = transactionCheck.getFrait();

                            if (!frait.getType().equals("transaction-donneur")) {
                                transactionCheck.setMontant(transactionCheck.getMontant() - frait.getMontant());
                            }
                            if (transactionCheck.getTransactionType().getType().equals("espece")) {
                                accountApiProxy2.debitAccountAgent(transactionCheck.getLoginAgent(), transactionCheck.getMontant());
                            }
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
    public int extournerTransactionEspece(String referenceTransaction) {
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
                        int res = accountApiProxy2.debitAccountAgent(transactionCheck.getLoginAgent(), transactionCheck.getMontant());
                        if (res==1) {
                            transactionCheck.setStatus(TransactionStatus.EXTOURNE.toString());
                            transactionRepository.save(transactionCheck);
                            return 1;
                        } else return -5;
                    }
                }
            }
        }
    }

    @Override
    public int extournerTransactionCompte(String referenceTransaction) {
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
                        int res = accountApiProxy2.crediteCompteClient(transactionCheck.getNumClient(), transactionCheck.getMontant());
                        if (res==1) {
                            transactionCheck.setStatus(TransactionStatus.EXTOURNE.toString());
                            transactionRepository.save(transactionCheck);
                            return 1;
                        } else return -5;
                    }
                }
            }
        }
    }

    @Override
    public int restituerTransactionEspece(String referenceTransaction) {
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
                        int res = accountApiProxy2.debitAccountAgent(transactionCheck.getLoginAgent(), transactionCheck.getMontant());
                        if (res==1) {
                            transactionCheck.setStatus(TransactionStatus.RETITUER.toString());
                            transactionRepository.save(transactionCheck);
                            return 1;
                        } else return -5;
                    }
                }
            }
        }
    }

    @Override
    public int restituerTransactionCompte(String referenceTransaction) {
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
                        int res = accountApiProxy2.crediteCompteClient(transactionCheck.getNumClient(), transactionCheck.getMontant());
                        if (res==1) {
                            transactionCheck.setStatus(TransactionStatus.RETITUER.toString());
                            transactionRepository.save(transactionCheck);
                            return 1;
                        } else return -5;
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
