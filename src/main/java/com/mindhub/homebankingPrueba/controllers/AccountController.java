package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.AccountDTO;
import com.mindhub.homebankingPrueba.dtos.ClientDTO;
import com.mindhub.homebankingPrueba.models.Account;
import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.repositories.AccountRepository;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public Set<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountDTO(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .orElse(null);
    }

    int minAccount = 12341234;
    int maxAccount = 98767896;

    public int getRandomNumberAccount(int minAccount, int maxAccount) {
        return (int) ((Math.random() * (maxAccount - minAccount)) + minAccount);
    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Excuse me, you can't have more than three accounts", HttpStatus.FORBIDDEN);
        }

        String newAccountNumber;

        newAccountNumber = "VIN-" + getRandomNumberAccount(minAccount, maxAccount);


        Account accountNew = new Account(newAccountNumber, LocalDate.now(), 0);

        accountRepository.save(accountNew);
        client.addAccount(accountNew);
        clientRepository.save(client);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

