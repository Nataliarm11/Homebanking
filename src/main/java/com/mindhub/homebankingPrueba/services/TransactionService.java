package com.mindhub.homebankingPrueba.services;

import com.mindhub.homebankingPrueba.models.Transaction;

import java.util.List;

public interface TransactionService {
    void saveTransaction (Transaction transaction);

    void saveAllTransactions (List<Transaction> transactions);
}
