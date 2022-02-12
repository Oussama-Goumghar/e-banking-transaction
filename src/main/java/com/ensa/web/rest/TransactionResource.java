package com.ensa.web.rest;

import com.ensa.domain.Transaction;
import com.ensa.domain.TransactionType;
import com.ensa.service.TransactionService;
import com.ensa.web.rest.errors.BadRequestAlertException;
import com.ensa.web.rest.proxy.AccountApiProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ensa.domain.Transaction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountApiProxy accountApiProxy;

    /**
     * {@code POST  /transactions} : Create a new transaction.
     *
     * @param transaction the transaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transaction, or with status {@code 400 (Bad Request)} if the transaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions/motif/{motifLibelle}/transactionType/{transactionType}/fraitType/{fraitType}")
    public int createTransaction(@Valid @RequestBody Transaction transaction,
                                 @PathVariable(value = "motifLibelle", required = false) final String motifLibelle,
                                 @PathVariable(value = "transactionType", required = false) final String transactionType,
                                 @PathVariable(value = "fraitType", required = false) final String fraitType) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transaction);
        return transactionService.createTransaction(transaction, motifLibelle, transactionType, fraitType);
    }

    @PostMapping("/transactions/block")
    public List<Transaction> blockTransaction(@Valid @RequestBody List<Transaction> transactions) throws URISyntaxException {
        return transactionService.blockTransaction(transactions);
    }

    @PatchMapping(value = "/transactions/servir/reference-transaction/{reference}")
    public int servirTransaction(
        @PathVariable(value = "reference", required = false) final String reference
    ) throws URISyntaxException {
        return transactionService.servirTransaction(reference);
    }

    @PatchMapping(value = "/transactions/restituer/reference-transaction/{reference}")
    public int restituerTransaction(
        @PathVariable(value = "reference", required = false) final String reference
    ) throws URISyntaxException {
        return transactionService.restituerTransaction(reference);
    }

    @PatchMapping(value = "/transactions/extourner/reference-transaction/{reference}")
    public int extournerTransaction(
        @PathVariable(value = "reference", required = false) final String reference
    ) throws URISyntaxException {
        return transactionService.extournerTransaction(reference);
    }



    /**
     * {@code GET  /transactions} : get all the transactions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        log.debug("REST request to get all Transactions");
        return transactionService.findTransactionAll();
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transaction.
     *
     * @param id the id of the transaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<Transaction> transaction = transactionService.findTransactionById(id);
        return ResponseUtil.wrapOrNotFound(transaction);
    }

    /**
     * {@code DELETE  /transactions/:id} : delete the "id" transaction.
     *
     * @param id the id of the transaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transactions/delete-by-id/{id}")
    public int deleteTransactionById(@PathVariable Long id) {
        return transactionService.deleteTransactionById(id);
    }

    @DeleteMapping("/transactions/delete-by-reference/{reference}")
    public int deleteTransactionByReference(@PathVariable String reference) {
        return transactionService.deleteTransactionByReference(reference);
    }

    @GetMapping("/transactions/test1")
    public String test1() {
        return "this the test 78787";
    }

    @GetMapping("/transactions/test2")
    public String test2() {
        return "this the test 2 ALMERD";
    }

    @GetMapping("/transactions/test3")
    public String testProxyAccount() {
        return this.accountApiProxy.test();
    }

    @GetMapping("/transactions/blocked")
    public List<Transaction> transactionBlocked() {
        return this.transactionService.findTransactionsBloqued();
    }

    @GetMapping("/transactions/not-blocked")
    public List<Transaction> transactionNotBlocked() {
        return this.transactionService.findTransactionsNotBloqued();
    }
}
