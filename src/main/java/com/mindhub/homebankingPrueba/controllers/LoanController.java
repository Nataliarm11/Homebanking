package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.LoanApplicationDTO;
import com.mindhub.homebankingPrueba.dtos.LoanDTO;
import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.repositories.*;
import com.mindhub.homebankingPrueba.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ClientLoanRepository clientLoanRepository;


    @RequestMapping("/loans")
    public List <LoanDTO> getLoanDTO () {
        return loanService.getLoansDTO();
    }


    @Transactional
    @RequestMapping(path = "/loans",method = RequestMethod.POST)
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanRequest){
        Client client = clientService.findByEmail(authentication.getName());
        if (loanRequest.getAmount() == null || loanRequest.getPayment()==  null ||loanRequest.getLoanId() == null ||loanRequest.getDestinationAccountNumber()== null) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        Loan loanType = loanService.findById(loanRequest.getLoanId());
        Account account = accountService.findByNumber(loanRequest.getDestinationAccountNumber());
        double maxLoanAmount = loanService.findById(loanRequest.getLoanId()).getMaxAmount();


        // Verificar que los parámetros no estén vacíos
        if (loanRequest.getAmount() == 0) {
            return new ResponseEntity<>("the amount is zero", HttpStatus.FORBIDDEN);
        }

        if (loanRequest.getPayment() == 0) {
            return new ResponseEntity<>("the payments is zero", HttpStatus.FORBIDDEN);
        }

        if (loanRequest.getDestinationAccountNumber().isBlank()) {
            return new ResponseEntity<>("description is blank", HttpStatus.FORBIDDEN);
        }

        //Verificar que el préstamo exista
        if (loanType == null) {
            return new ResponseEntity<>("The loan does not exist", HttpStatus.FORBIDDEN);
        } //SI

        //Verificar que el monto solicitado no exceda el monto máximo del préstamo
        if (loanRequest.getAmount()>maxLoanAmount){
            return new ResponseEntity<>("The amount required exceeds the limit", HttpStatus.FORBIDDEN);
        }//SI

        //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if (!loanType.getPayments().contains(loanRequest.getPayment())){
            return new ResponseEntity<>("The payments is incorrect", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de destino exista
        if (account == null){
            return new ResponseEntity<>("The destination account is not valid", HttpStatus.FORBIDDEN);
        }//SI

        //Verificar que la cuenta de destino pertenezca al cliente autenticado
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Destiny account doesn't belong to authenticated client", HttpStatus.FORBIDDEN);
        }

        //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        double percentageAmount = ((loanRequest.getAmount()*20/100)+loanRequest.getAmount());
        ClientLoan loanNew = new ClientLoan(percentageAmount, loanRequest.getPayment());
        Transaction transaction = new Transaction(TransactionType.CREDIT,loanRequest.getAmount(), loanType.getName() + " " + " loan approved", LocalDateTime.now());
        account.setBalance(account.getBalance() + loanRequest.getAmount());
        account.addTransaction(transaction);
        client.addClientLoan(loanNew);
        loanType.addClientLoan(loanNew);
        transactionService.saveTransaction(transaction);
        clientLoanRepository.save(loanNew);
        accountService.saveAccount(account);
        loanService.saveLoan(loanType);
        clientService.saveClient(client);




        return new ResponseEntity<>("Your loan approved",HttpStatus.CREATED);
    }
}
