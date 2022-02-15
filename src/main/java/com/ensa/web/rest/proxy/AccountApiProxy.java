package com.ensa.web.rest.proxy;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accountapi")
public interface AccountApiProxy {

    @GetMapping("/api/agences/test")
    String test();

    @GetMapping("/api/clients/credit-compte/num-client/{numClient}/montant/{montant}")
    int crediteCompteClient(@PathVariable("numClient") String numClient, @PathVariable("montant") double montant);

    @GetMapping("/api/clients/debit-compte/num-client/{numClient}/montant/{montant}")
    int debitCompteClient(@PathVariable("numClient") String numClient, @PathVariable("montant") double montant);

    @GetMapping("/api/agents/credit-account/{login}/montant/{montant}")
    int creditAccountAgent(@PathVariable("login") String login, @PathVariable("montant") double montant);

    @GetMapping("/api/agents/debit-account/{login}/montant/{montant}")
    int debitAccountAgent(@PathVariable("login") String login, @PathVariable("montant") double montant);
}
