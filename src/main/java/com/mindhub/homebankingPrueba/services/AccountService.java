package com.mindhub.homebankingPrueba.services;

import com.mindhub.homebankingPrueba.dtos.AccountDTO;
import com.mindhub.homebankingPrueba.dtos.ClientDTO;
import com.mindhub.homebankingPrueba.models.Account;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

public interface AccountService {

    boolean existsByNumber (String account);

    void saveAccount (Account account);

    Set<AccountDTO> getAccountsDTO();

    AccountDTO getAccountDTO(@PathVariable Long id);

    Account findById(Long id);

    Account findByNumber (String number);



}
