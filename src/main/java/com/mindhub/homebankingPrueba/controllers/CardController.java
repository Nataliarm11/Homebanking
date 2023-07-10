package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.repositories.CardRepository;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import com.mindhub.homebankingPrueba.services.CardService;
import com.mindhub.homebankingPrueba.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;


@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    CardService cardService;


    @Autowired
    ClientService  clientService;

    // CVV
    short minCvv = 123;
    short maxCvv = 989;

    public short getRandomNumberCvv(short minCvv, short maxCvv) {
        return (short) ((Math.random() * (maxCvv - minCvv)) + minCvv);
    }

    public String generateCardNumber() {
        String cardNumber;
        boolean exists;

        do {
            cardNumber = generateRandomCardNumber();
            exists = cardService.existsByNumber(cardNumber);
        } while (exists);

        return cardNumber;
    }

    private String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardNumber.append(random.nextInt(10));
            }
            if (i < 3) {
                cardNumber.append("-");
            }
        }

        return cardNumber.toString();
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication) {
        Client client =clientService.findByEmail(authentication.getName());

        if (cardService.existsByClientAndColorAndType(client, cardColor, cardType)) {
            return new ResponseEntity<>("Excuse me, prohibited action", HttpStatus.FORBIDDEN);
        }

        String cardHolderClient = client.getFirstName() + " " + client.getLastName();

        String cardNumber = generateCardNumber();
        short numberCvv = getRandomNumberCvv(minCvv, maxCvv);
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime thruDate = fromDate.plusYears(5);

        Card cardNew = new Card(cardHolderClient, cardType, cardColor, cardNumber, numberCvv, thruDate, fromDate);
        client.addCard(cardNew);
        cardService.saveCard(cardNew);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}


