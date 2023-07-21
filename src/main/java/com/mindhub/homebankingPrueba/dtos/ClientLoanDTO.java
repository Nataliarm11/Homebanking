package com.mindhub.homebankingPrueba.dtos;


import com.mindhub.homebankingPrueba.models.ClientLoan;


public class ClientLoanDTO {

    private long id;

    private long loanId;

    private String loanName;

    private double amount;

    private int payments;

    private int remainingPayments;

    private double remainingAmount;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.remainingPayments = clientLoan.getRemainingPayments();
        this.remainingAmount = clientLoan.getRemainingAmount();
    }

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public int getRemainingPayments() {
        return remainingPayments;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }
}
