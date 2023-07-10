package com.mindhub.homebankingPrueba.services.implement;

import com.mindhub.homebankingPrueba.models.Transaction;
import com.mindhub.homebankingPrueba.repositories.TransactionRepository;
import com.mindhub.homebankingPrueba.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
