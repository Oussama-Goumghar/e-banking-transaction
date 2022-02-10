package com.ensa.repository;

import com.ensa.domain.Transaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.awt.*;

/**
 * Spring Data SQL repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findTransactionByReference(String reference);
    Object findTransactionsByStatus(String status);
}
