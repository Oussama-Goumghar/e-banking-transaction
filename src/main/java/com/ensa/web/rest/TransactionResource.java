package com.ensa.web.rest;

import com.ensa.domain.Transaction;
import com.ensa.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
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


    /**
     * {@code POST  /transactions} : Create a new transaction.
     *
     * @param transaction the transaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transaction, or with status {@code 400 (Bad Request)} if the transaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions/transactionType/{transactionType}/fraitType/{fraitType}")
    public int createTransaction(@Valid @RequestBody Transaction transaction,
                                 @PathVariable(value = "transactionType", required = false) final String transactionType,
                                 @PathVariable(value = "fraitType", required = false) final String fraitType) {
        return transactionService.createTransaction(transaction, transactionType, fraitType);
    }

    @PostMapping("/transactions/block")
    public List<Transaction> blockTransaction(@Valid @RequestBody List<Transaction> transactions) throws URISyntaxException {
        return transactionService.blockTransaction(transactions);
    }

    @PostMapping("/transactions/unblock")
    public List<Transaction> unblockTransaction(@Valid @RequestBody List<Transaction> transactions) {
        return transactionService.deBlockTransaction(transactions);
    }

    @PutMapping(value = "/transactions/servir-compte/reference-transaction/{reference}/num-benificiair/{numBenificiair}")
    public int servirTransactionCompte(
        @PathVariable(value = "reference", required = false) final String reference,
        @PathVariable(value = "numBenificiair", required = false) final String numBenificiair
    ) {
        return transactionService.servirTransactionCompte(reference,numBenificiair);
    }

    @PutMapping(value = "/transactions/servir-espece/reference-transaction/{reference}")
    public int servirTransactionEspece(
        @PathVariable(value = "reference", required = false) final String reference) {
        return transactionService.servirTransactionEspece(reference);
    }

    @PutMapping(value = "/transactions/restituer-espece/reference-transaction/{reference}")
    public int restituerTransactionEspece(
        @PathVariable(value = "reference", required = false) final String reference
    ) throws URISyntaxException {
        return transactionService.restituerTransactionEspece(reference);
    }

    @PutMapping(value = "/transactions/restituer-compte/reference-transaction/{reference}")
    public int restituerTransactionCompte(
        @PathVariable(value = "reference", required = false) final String reference
    ) throws URISyntaxException {
        return transactionService.restituerTransactionCompte(reference);
    }

    @PutMapping(value = "/transactions/extourner-espece/reference-transaction/{reference}")
    public int extournerTransactionEspece(
        @PathVariable(value = "reference", required = false) final String reference
    ) throws URISyntaxException {
        return transactionService.extournerTransactionEspece(reference);
    }
    @PutMapping(value = "/transactions/extourner-compte/reference-transaction/{reference}")
    public int extournerTransactionCompte(
        @PathVariable(value = "reference", required = false) final String reference
    ) throws URISyntaxException {
        return transactionService.extournerTransactionCompte(reference);
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

    @GetMapping("/transactions/reference/{reference}")
    public Transaction findTransactionByReference(@PathVariable String reference) {
        return this.transactionService.findTransactionByReference(reference);
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
