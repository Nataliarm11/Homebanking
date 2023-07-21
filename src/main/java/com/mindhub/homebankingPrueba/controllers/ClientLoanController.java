package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.ClientLoanDTO;
import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.services.AccountService;
import com.mindhub.homebankingPrueba.services.ClientLoanService;
import com.mindhub.homebankingPrueba.services.ClientService;
import com.mindhub.homebankingPrueba.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

@RequestMapping("/api")
@RestController
public class ClientLoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/client/loan/payments")
    public ResponseEntity<Object> payLoan(Authentication authentication, @RequestParam Long id,
                                          @RequestParam String account) {

        if (authentication == null) {
            return new ResponseEntity<>("The client is not authenticated", HttpStatus.FORBIDDEN);
        }

        if (id == null || account.isBlank()) {
            return new ResponseEntity<>("Missing loan information or account", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>("The client does not exist, please check again", HttpStatus.FORBIDDEN);
        }

        Account accountPayment = accountService.findByNumber(account);
        if (accountPayment == null) {
            return new ResponseEntity<>("The account does not exist, please check again", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = clientLoanService.findById(id);
        if (clientLoan == null) {
            return new ResponseEntity<>("The loan does not exist, please check again", HttpStatus.FORBIDDEN);
        }
        if (clientLoan.getRemainingAmount() <= 0) {
            return new ResponseEntity<>("Loan paid in full", HttpStatus.FORBIDDEN);
        }

        Double payment = clientLoan.getAmount() / clientLoan.getPayments();
        if (accountPayment.getBalance() < payment) {
            return new ResponseEntity<>("your amount is insufficient", HttpStatus.FORBIDDEN);
        }

        Transaction newTransaction = new Transaction(
                TransactionType.DEBIT,
                payment,
                "Loan Payment",
                LocalDateTime.now(),
                accountPayment.getBalance() - payment,
                true
        );
        accountPayment.setBalance(accountPayment.getBalance() - payment);
        accountPayment.addTransaction(newTransaction);
        accountService.saveAccount(accountPayment);

        clientLoan.setRemainingPayments(clientLoan.getRemainingPayments() - 1);
        clientLoan.setRemainingAmount(clientLoan.getRemainingAmount() - payment);
        clientLoanService.save(clientLoan);

        transactionService.saveTransaction(newTransaction);

        return new ResponseEntity<>("Loan payment successful", HttpStatus.OK);
    }
    @GetMapping("/client/loans/{id}")
    public ResponseEntity<Object> getOneLoan(@PathVariable Long id, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        ClientLoan loan = clientLoanService.findById(id);
        if (loan == null){
            return new ResponseEntity<>("not loan", HttpStatus.NOT_FOUND);
        }

        if (!client.getLoans().contains(loan)) {
            return new ResponseEntity<>("the loan does not belong to you", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new ClientLoanDTO(loan), HttpStatus.OK);
    }
}


