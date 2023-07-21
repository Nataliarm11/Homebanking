package com.mindhub.homebankingPrueba.services;

import com.mindhub.homebankingPrueba.models.ClientLoan;

public interface ClientLoanService {
    void save(ClientLoan clientLoan);

    ClientLoan findById(Long id);
}
