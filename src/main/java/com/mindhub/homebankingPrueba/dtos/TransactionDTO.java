package com.mindhub.homebankingPrueba.dtos;

import com.mindhub.homebankingPrueba.models.Transaction;
import com.mindhub.homebankingPrueba.models.TransactionType;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class TransactionDTO {
    private long id;

    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription()  ;
        this.date = transaction.getDate();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
