package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.AccountDTO;
import com.mindhub.homebankingPrueba.dtos.ClientDTO;
import com.mindhub.homebankingPrueba.models.Account;
import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.repositories.AccountRepository;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import com.mindhub.homebankingPrueba.services.AccountService;
import com.mindhub.homebankingPrueba.services.ClientService;
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
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public Set<AccountDTO> getAccountsDTO() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountDTO(@PathVariable Long id) {
        return  accountService.getAccountDTO(id);

    }

    int minAccount = 12341234;
    int maxAccount = 98767896;

    public int getRandomNumberAccount(int minAccount, int maxAccount) {
        return (int) ((Math.random() * (maxAccount - minAccount)) + minAccount);
    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());


        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Excuse me, you can't have more than three accounts", HttpStatus.FORBIDDEN);
        }

        String newAccountNumber;

        do {
            newAccountNumber = "VIN-" + getRandomNumberAccount(minAccount, maxAccount);
        } while (accountService.existsByNumber(newAccountNumber));


        Account accountNew = new Account(newAccountNumber, LocalDate.now(), 0);

        accountService.saveAccount(accountNew);
        client.addAccount(accountNew);
        clientService.saveClient(client);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

