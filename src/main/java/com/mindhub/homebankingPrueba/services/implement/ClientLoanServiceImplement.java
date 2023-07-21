package com.mindhub.homebankingPrueba.services.implement;

import com.mindhub.homebankingPrueba.models.ClientLoan;
import com.mindhub.homebankingPrueba.repositories.ClientLoanRepository;
import com.mindhub.homebankingPrueba.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {
    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public void save(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public ClientLoan findById(Long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }
}
