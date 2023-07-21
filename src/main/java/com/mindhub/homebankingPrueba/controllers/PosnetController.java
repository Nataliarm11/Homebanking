package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.PosnetDTO;
import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.services.AccountService;
import com.mindhub.homebankingPrueba.services.CardService;
import com.mindhub.homebankingPrueba.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api")
public class PosnetController {

    @Autowired
    AccountService accountService;
    @Autowired
    CardService cardService;

    @Autowired
    TransactionService transactionService;

    @PostMapping("/payments")
    @Transactional
    public ResponseEntity<String> makePayment(@RequestBody PosnetDTO posnetDTO) {

        Card card = cardService.findByNumber(posnetDTO.getNumber());

        if (card == null ) {
            return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
        }

        if (!card.getActiveCard()) {
            return new ResponseEntity<>("Card  inactive", HttpStatus.NOT_FOUND);
        }

        Client client = card.getClient();
        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        Account account = client.getAccounts().stream()
                .filter(Account::getActiveAccount)
                .filter(acc -> acc.getBalance() >= posnetDTO.getAmount())
                .findFirst()
                .orElse(null);

        if (account == null) {
            return new ResponseEntity<>("Account not found or insufficient funds", HttpStatus.NOT_FOUND);
        }

        if (!client.getCards().contains(card) || !client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Card or Account doesn't belong to current client", HttpStatus.FORBIDDEN);
        }

        if (LocalDate.now().isAfter(card.getThruDate())) {
            return new ResponseEntity<>("Card has expired", HttpStatus.FORBIDDEN);
        }

        if (card.getCvv() != posnetDTO.getCvv()) {
            return new ResponseEntity<>("Card CVV is incorrect", HttpStatus.FORBIDDEN);
        }


        Transaction transaction = new Transaction(TransactionType.DEBIT, posnetDTO.getAmount(), posnetDTO.getDescription(), LocalDateTime.now(), account.getBalance() - posnetDTO.getAmount(), true);

        transaction.setAccount(account);

        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance() - posnetDTO.getAmount());
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}


