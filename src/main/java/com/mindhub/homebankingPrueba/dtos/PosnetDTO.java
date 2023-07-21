package com.mindhub.homebankingPrueba.dtos;

public class PosnetDTO {

    private String number;

    private int cvv;

    private double amount;

    private String description;

    public PosnetDTO() {

    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }


}
