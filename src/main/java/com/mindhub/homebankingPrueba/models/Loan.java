package com.mindhub.homebankingPrueba.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;


@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;

    private double maxAmount;

    private double percentageLoan;

    @ElementCollection
    private List<Integer> payments;

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {
    }

    public Loan( String name, double maxAmount, List<Integer> payments, double percentageLoan) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentageLoan = percentageLoan;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public double getPercentageLoan() {
        return percentageLoan;
    }

    public void setPercentageLoan(double percentageLoan) {
        this.percentageLoan = percentageLoan;
    }

    @JsonIgnore
    public Set<Client> getClients() {
        return clientLoans.stream()
                .map(clientLoan -> clientLoan.getClient())
                .collect(Collectors.toSet());
    }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxAmount=" + maxAmount +
                ", payments=" + payments +
                '}';
    }
}
