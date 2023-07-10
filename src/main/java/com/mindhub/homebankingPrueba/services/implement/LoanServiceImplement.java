package com.mindhub.homebankingPrueba.services.implement;

import com.mindhub.homebankingPrueba.dtos.LoanDTO;
import com.mindhub.homebankingPrueba.models.Loan;
import com.mindhub.homebankingPrueba.repositories.LoanRepository;
import com.mindhub.homebankingPrueba.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<LoanDTO> getLoansDTO() {
        return loanRepository.findAll()
                .stream()
                .map(LoanDTO :: new)
                .collect(Collectors.toList());
    }

    @Override
    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
