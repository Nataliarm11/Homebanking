package com.mindhub.homebankingPrueba.dtos;

import com.mindhub.homebankingPrueba.models.Client;

import java.util.List;
import java.util.stream.Collectors;

public class ClientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private List<AccountDTO> accounts;

    private List<ClientLoanDTO> loans;


    public ClientDTO() {  }


    public ClientDTO(Client client) {

        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();;
        this.accounts = client.getAccounts().stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());
        this.loans = client.getLoans().stream()
                .map(loan -> new ClientLoanDTO(loan))
                .collect(Collectors.toList());
    }

    public long getId(){
        return id;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public void setLoans(List<ClientLoanDTO> loans) {
        this.loans = loans;
    }
}

