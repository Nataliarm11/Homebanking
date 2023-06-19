package com.mindhub.homebankingPrueba.controllers;


import com.mindhub.homebankingPrueba.dtos.AccountDTO;
import com.mindhub.homebankingPrueba.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api") @RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/accounts")
    public Set<AccountDTO> getaccountsDTO(){
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccountDTO (@PathVariable Long id){
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .orElse(null);
    }
}
