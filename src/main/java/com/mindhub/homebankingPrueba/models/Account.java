package com.mindhub.homebankingPrueba.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String number;

    private LocalDate creationDate;

    private double balance;

    private boolean activeAccount;

    private AccountType accountType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="client_id ")
    private Client client;

    @OneToMany (mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();


    public Account (){ }

    public Account ( String number, LocalDate creationDate, double balance, boolean activeAccount, AccountType accountType ){
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.activeAccount = activeAccount;
        this.accountType = accountType;

    }


    public long getId() {
        return id;
    }


    public Set<Transaction> getTransactions() {
        return transactions;
    }


    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction ( Transaction transaction ) {
        transaction.setAccount(this);
        this.transactions.add(transaction);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean getActiveAccount() {
        return activeAccount;
    }

    public void setActiveAccount(boolean activeAccount) {
        this.activeAccount = activeAccount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient (Client client) {
        this.client = client;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                '}';
    }



}
