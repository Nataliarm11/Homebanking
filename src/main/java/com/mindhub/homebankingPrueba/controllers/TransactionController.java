package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.TransactionNewDTO;
import com.mindhub.homebankingPrueba.models.Account;
import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.models.Transaction;
import com.mindhub.homebankingPrueba.models.TransactionType;
import com.mindhub.homebankingPrueba.services.AccountService;
import com.mindhub.homebankingPrueba.services.ClientService;
import com.mindhub.homebankingPrueba.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestBody TransactionNewDTO transactionRequest) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientService.findByEmail(authentication.getName());
        Account destinationAccount = accountService.findByNumber(transactionRequest.getDestinationNumber());
        Account originAccount = accountService.findByNumber(transactionRequest.getOriginNumber());

        if (transactionRequest.getAmount() == 0) {
            return new ResponseEntity<>("the amount is zero", HttpStatus.FORBIDDEN);
        }

        if (transactionRequest.getDescription().isBlank()) {
            return new ResponseEntity<>("La descripción está vacía", HttpStatus.FORBIDDEN);
        }

        if (transactionRequest.getDestinationNumber().isBlank()) {
            return new ResponseEntity<>("description is empty", HttpStatus.FORBIDDEN);
        }

        if (transactionRequest.getOriginNumber().isBlank()) {
            return new ResponseEntity<>("The source account number is empty", HttpStatus.FORBIDDEN);
        }

        if (transactionRequest.getOriginNumber().equals(transactionRequest.getDestinationNumber())) {
            return new ResponseEntity<>("Sender and receiver accounts are the same", HttpStatus.FORBIDDEN);
        }

        if (originAccount == null) {
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(originAccount)) {
            return new ResponseEntity<>("Origin account does not belong to the authenticated client", HttpStatus.UNAUTHORIZED);
        }

        if (destinationAccount == null) {
            return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if (originAccount.getBalance() < transactionRequest.getAmount()) {
            return new ResponseEntity<>("Insufficient funds in the origin account", HttpStatus.BAD_REQUEST);
        }

        // Crear dos transacciones: una con el tipo de transacción "DEBIT" asociada a la cuenta de origen
        // y otra con el tipo de transacción "CREDIT" asociada a la cuenta de destino.
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT,  ( -1 * (transactionRequest.getAmount())),
                transactionRequest.getDescription() + " to " + transactionRequest.getDestinationNumber(), LocalDateTime.now(), originAccount.getBalance() - transactionRequest.getAmount(),true);
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, transactionRequest.getAmount(),
                transactionRequest.getDescription() + " from " + transactionRequest.getOriginNumber(), LocalDateTime.now(), destinationAccount.getBalance() + transactionRequest.getAmount(), true);

        originAccount.addTransaction(transactionDebit);
        destinationAccount.addTransaction(transactionCredit);

        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);

        originAccount.setBalance(originAccount.getBalance() - transactionRequest.getAmount());
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionRequest.getAmount());

        accountService.saveAccount(originAccount);
        accountService.saveAccount(destinationAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

