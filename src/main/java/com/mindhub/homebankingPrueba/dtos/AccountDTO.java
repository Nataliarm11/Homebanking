package com.mindhub.homebankingPrueba.dtos;

import com.mindhub.homebankingPrueba.models.Account;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;

    private List<TransactionDTO> transactions;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream()
                .map(transaction -> new TransactionDTO(transaction))
                .sorted(Comparator.comparing(TransactionDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }


    public double getBalance() {
        return balance;
    }

}




