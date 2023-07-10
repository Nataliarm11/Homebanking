package com.mindhub.homebankingPrueba.services.implement;

import com.mindhub.homebankingPrueba.dtos.AccountDTO;
import com.mindhub.homebankingPrueba.models.Account;
import com.mindhub.homebankingPrueba.repositories.AccountRepository;
import com.mindhub.homebankingPrueba.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;


    @Override
    public boolean existsByNumber(String account) {
        return accountRepository.existsByNumber(account);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);

    }

    @Override
    public Set<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public AccountDTO getAccountDTO(Long id) {
        return new AccountDTO(this.findById(id));
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
}
