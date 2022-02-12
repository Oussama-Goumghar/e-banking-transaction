package com.ensa.repository;

import com.ensa.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findTransactionByReference(String reference);

    Optional<Transaction> findOneTransactionByReference(String reference);

    List<Transaction> findTransactionsByStatus(String status);

    List<Transaction> findTransactionsByStatusNot(String status);

}
