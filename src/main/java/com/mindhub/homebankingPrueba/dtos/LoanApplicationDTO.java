package com.mindhub.homebankingPrueba.dtos;

import javax.persistence.criteria.CriteriaBuilder;

public class LoanApplicationDTO {

    private Long loanId;
    private Double amount;
    private Integer payment;
    private String destinationAccountNumber;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long loanId, double amount, int payment, String destinationAccountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payment = payment;
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public Long getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayment() {
        return payment;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
}
