package com.mindhub.homebankingPrueba.dtos;

public class TransactionNewDTO {
    private double amount;

    private String description;

    private String destinationNumber;

    private String originNumber;

    public TransactionNewDTO(double amount, String description, String destinationNumber, String originNumber) {
        this.amount = amount;
        this.description = description;
        this.destinationNumber = destinationNumber;
        this.originNumber = originNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public String getOriginNumber() {
        return originNumber;
    }
}
