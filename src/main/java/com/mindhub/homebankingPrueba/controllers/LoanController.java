package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.ClientLoanDTO;
import com.mindhub.homebankingPrueba.dtos.LoanApplicationDTO;
import com.mindhub.homebankingPrueba.dtos.LoanDTO;
import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private  ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List <LoanDTO> getLoanDTO () {
        return loanService.getLoansDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanRequest){

        Client client = clientService.findByEmail(authentication.getName());

        if (loanRequest.getAmount() == null || loanRequest.getPayment()==  null ||loanRequest.getLoanId() == null ||loanRequest.getDestinationAccountNumber()== null) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        Loan loanType = loanService.findById(loanRequest.getLoanId());
        Account account = accountService.findByNumber(loanRequest.getDestinationAccountNumber());
        double maxLoanAmount = loanService.findById(loanRequest.getLoanId()).getMaxAmount();

        if (loanRequest.getAmount() == 0) {
            return new ResponseEntity<>("the amount is zero", HttpStatus.FORBIDDEN);
        }

        if (loanRequest.getPayment() <= 0) {
            return new ResponseEntity<>("Invalid payment", HttpStatus.FORBIDDEN);
        }

        if (loanRequest.getDestinationAccountNumber().isBlank()) {
            return new ResponseEntity<>("Invalid destination account number", HttpStatus.FORBIDDEN);
        }

        if (loanType == null) {
            return new ResponseEntity<>("The loan does not exist", HttpStatus.FORBIDDEN);
        }

        if (loanRequest.getAmount()>maxLoanAmount){
            return new ResponseEntity<>("The amount required exceeds the limit", HttpStatus.FORBIDDEN);
        }

        if (!loanType.getPayments().contains(loanRequest.getPayment())){
            return new ResponseEntity<>("The payments is incorrect", HttpStatus.FORBIDDEN);
        }

        if (account == null){
            return new ResponseEntity<>("The destination account is not valid", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Destiny account doesn't belong to authenticated client", HttpStatus.FORBIDDEN);
        }

        if (!account.getActiveAccount()) {
            return new ResponseEntity<>("account Inactive", HttpStatus.FORBIDDEN);
        }

        double percentageAmount = ((loanRequest.getAmount() * (loanType.getPercentageLoan() / 100.0)) + loanRequest.getAmount());
        ClientLoan loanNew = new ClientLoan(percentageAmount, loanRequest.getPayment(), loanRequest.getPayment(), percentageAmount);
        Transaction transaction = new Transaction(TransactionType.CREDIT,loanRequest.getAmount(), loanType.getName() + " " + " loan approved", LocalDateTime.now(), account.getBalance() + loanRequest.getAmount(), true);
        account.setBalance(account.getBalance() + loanRequest.getAmount());
        account.addTransaction(transaction);
        client.addClientLoan(loanNew);
        loanType.addClientLoan(loanNew);
        transactionService.saveTransaction(transaction);
        clientLoanService.save(loanNew);
        accountService.saveAccount(account);
        loanService.saveLoan(loanType);
        clientService.saveClient(client);

        return new ResponseEntity<>("Your loan approved",HttpStatus.CREATED);
    }

    @PostMapping("/loans/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> createLoanAdmin(Authentication authentication, @RequestBody LoanDTO loanDTO){

        Client client = clientService.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        if (loanDTO.getName() == null ) {
            return new ResponseEntity<>("Invalid loan name", HttpStatus.BAD_REQUEST);
        }

        if ( loanDTO.getName().isBlank()) {
            return new ResponseEntity<>("Invalid loan name", HttpStatus.BAD_REQUEST);
        }

        if (loanDTO.getMaxAmount() <= 0 ) {
            return new ResponseEntity<>("Invalid loan maxAmount", HttpStatus.BAD_REQUEST);
        }

        if (loanDTO.getPayments() == null ) {
            return new ResponseEntity<>("Invalid payments loan ", HttpStatus.BAD_REQUEST);
        }

        if (loanDTO.getPayments().isEmpty()) {
            return new ResponseEntity<>("Invalid payments loan", HttpStatus.BAD_REQUEST);
        }

        if (loanDTO.getPercentageLoan() <= 0.0) {
            return new ResponseEntity<>("Invalid percentage loan", HttpStatus.BAD_REQUEST);
        }

        Loan newLoan = new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getPayments(), loanDTO.getPercentageLoan());
        loanService.saveLoan(newLoan);

        return new ResponseEntity<>("Loan created successfully",HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/loans")
    public List<ClientLoanDTO> getLoans(Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        return client.getLoans().stream()
                .map(loan -> new ClientLoanDTO(loan))
                .collect(Collectors.toList());
    }
}
