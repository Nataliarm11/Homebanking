package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.TransactionNewDTO;
import com.mindhub.homebankingPrueba.models.Account;
import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.models.Transaction;
import com.mindhub.homebankingPrueba.models.TransactionType;
import com.mindhub.homebankingPrueba.repositories.AccountRepository;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import com.mindhub.homebankingPrueba.repositories.TransactionRepository;
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
import java.util.Set;

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
    @RequestMapping(path = "/transactions",method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestBody TransactionNewDTO transactionRequest) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientService.findByEmail(authentication.getName());
        Account destinationAccount = accountService.findByNumber(transactionRequest.getDestinationNumber());
        Account originAccount = accountService.findByNumber(transactionRequest.getOriginNumber());

        // Verificar que los parámetros no estén vacíos
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


        // Verificar que los números de cuenta no sean iguales
        if (transactionRequest.getOriginNumber().equals(transactionRequest.getDestinationNumber())) {
            return new ResponseEntity<>("Sender and receiver accounts are the same", HttpStatus.FORBIDDEN);
        }

        // Verificar que exista la cuenta de origen
        if (originAccount == null) {
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de origen pertenezca al cliente autenticado
        if (!client.getAccounts().contains(originAccount)) {
            return new ResponseEntity<>("Origin account does not belong to the authenticated client", HttpStatus.UNAUTHORIZED);
        }

        // Verificar que exista la cuenta de destino
        if (destinationAccount == null) {
            return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de origen tenga el monto disponible
        if (originAccount.getBalance() < transactionRequest.getAmount()) {
            return new ResponseEntity<>("Insufficient funds in the origin account", HttpStatus.BAD_REQUEST);
        }

        // Crear dos transacciones: una con el tipo de transacción "DEBIT" asociada a la cuenta de origen
        // y otra con el tipo de transacción "CREDIT" asociada a la cuenta de destino.
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT,  ( -1 * (transactionRequest.getAmount())),
                transactionRequest.getDescription() + " to " + transactionRequest.getDestinationNumber(), LocalDateTime.now());
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, transactionRequest.getAmount(),
                transactionRequest.getDescription() + " from " + transactionRequest.getOriginNumber(), LocalDateTime.now());

        originAccount.addTransaction(transactionDebit);
        destinationAccount.addTransaction(transactionCredit);

        // Guardar las transacciones en el repositorio de transacciones
        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);


        // Actualizar los saldos de las cuentas
        originAccount.setBalance(originAccount.getBalance() - transactionRequest.getAmount());
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionRequest.getAmount());

        // Guardar las cuentas actualizadas en el repositorio de cuentas
        accountService.saveAccount(originAccount);
        accountService.saveAccount(destinationAccount);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

