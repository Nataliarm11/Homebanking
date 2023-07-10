package com.mindhub.homebankingPrueba.services;

import com.mindhub.homebankingPrueba.dtos.LoanDTO;
import com.mindhub.homebankingPrueba.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoansDTO ();

    Loan findById(Long id);

    void saveLoan(Loan loan);
}
