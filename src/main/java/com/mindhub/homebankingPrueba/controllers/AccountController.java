package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.AccountDTO;
import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.utils.AccountUtils;
import com.mindhub.homebankingPrueba.services.AccountService;
import com.mindhub.homebankingPrueba.services.ClientService;
import com.mindhub.homebankingPrueba.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/accounts")
    public Set<AccountDTO> getAccountsDTO() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccountDTO(@PathVariable Long id,
                                                Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);

        if (!client.getAccounts().contains(account) || !account.getActiveAccount()) {
            return new ResponseEntity<>("Account not found or unauthorized access", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType) {
        Client client = clientService.findByEmail(authentication.getName());

        if ( client.getAccounts().stream().filter(account -> !account.getActiveAccount()).count() > 3 ) {
            return new ResponseEntity<>("Excuse me, you can't have more than three accounts", HttpStatus.FORBIDDEN);
        }

        if ( accountType == null ) {
            return new ResponseEntity<>("You must choose an account type", HttpStatus.FORBIDDEN);
        }

        String newAccountNumber;
        int minAccount = 12341234;
        int maxAccount = 98767896;

        do {
            newAccountNumber = "VIN-" +  AccountUtils.getRandomNumberAccount(minAccount, maxAccount);
        } while (accountService.existsByNumber(newAccountNumber));

        Account accountNew = new Account(newAccountNumber, LocalDate.now(), 0, true, accountType);

        accountService.saveAccount(accountNew);
        client.addAccount(accountNew);
        clientService.saveClient(client);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/accounts")
    public List<AccountDTO> getAccountsTrue(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        return client.getAccounts().stream()
                .filter(account -> account.getActiveAccount())
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());
    }

    @PatchMapping("/accounts/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam Long id, Authentication authentication) {

        if (id == null) {
            return new ResponseEntity<>("Missing account data", HttpStatus.FORBIDDEN);
        }

        if (authentication == null) {
            return new ResponseEntity<>("Client not authenticated", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        Account account = accountService.findById(id);
        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (account.getBalance() != 0) {
            return new ResponseEntity<>("Account balance must be 0 to delete", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("This account is not associated with the authenticated client", HttpStatus.FORBIDDEN);
        }

        List<Transaction> transactions = account.getTransactions().stream()
                .filter(Transaction -> Transaction.getActiveTransaction())
                .peek(transaction -> transaction.setActiveTransaction(false))
                .collect(Collectors.toList());

        account.setActiveAccount(false);
        accountService.saveAccount(account);
        transactionService.saveAllTransactions(transactions);
        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }
}

