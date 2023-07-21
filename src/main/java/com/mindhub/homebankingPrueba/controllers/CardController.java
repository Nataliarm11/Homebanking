package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.CardDTO;
import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.services.CardService;
import com.mindhub.homebankingPrueba.services.ClientService;
import com.mindhub.homebankingPrueba.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCardsTrue(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
         return client.getCards().stream()
                .filter(card -> card.getActiveCard())
                .map(card -> new CardDTO(card))
                .collect(Collectors.toList());
    }

    private final short minCvv = 123;
    private final short maxCvv = 989;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCards(
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType,
            Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        if (client.getCards().stream().filter(card -> card.getType()== cardType && card.getColor() == cardColor && card.getActiveCard()).count() >= 1) {
            return new ResponseEntity<>("Excuse me, prohibited action", HttpStatus.FORBIDDEN);
        }

        String cardHolderClient = client.getFirstName() + " " + client.getLastName();
        String cardNumber = CardUtils.generateCardNumber();
        short numberCvv = CardUtils.getRandomNumberCvv(minCvv, maxCvv);
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(5);

        Card cardNew = new Card(cardHolderClient, cardType, cardColor, cardNumber, numberCvv, thruDate, fromDate, true);
        client.addCard(cardNew);
        cardService.saveCard(cardNew);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(@RequestParam Long id, Authentication authentication) {

        if (id == null) {
            return new ResponseEntity<>("Missing card data", HttpStatus.FORBIDDEN);
        }

        if (authentication == null) {
            return new ResponseEntity<>("Client not authenticated", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        Card card = cardService.findById(id);
        if (card == null) {
            return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
        }

        if (!client.getCards().contains(card)) {
            return new ResponseEntity<>("This card is not associated with the authenticated client", HttpStatus.FORBIDDEN);
        }

        card.setActiveCard(false);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card deleted", HttpStatus.OK);
    }

    @PostMapping("/clients/cards/renew")
    public ResponseEntity<Object> renewCards(
            @RequestParam Long id, Authentication authentication) {

        if (id == null) {
            return new ResponseEntity<>("Missing card data", HttpStatus.FORBIDDEN);
        }

        if (authentication == null) {
            return new ResponseEntity<>("Client not authenticated", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        Card card = cardService.findById(id);
        if (card == null) {
            return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
        }

        if (!client.getCards().contains(card)) {
            return new ResponseEntity<>("This card is not associated with the authenticated client", HttpStatus.FORBIDDEN);
        }
        if (!card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("Card is not expired yet", HttpStatus.BAD_REQUEST);
        }

        card.setActiveCard(false);
        cardService.saveCard(card);

        createCards(card.getColor(),card.getType(), authentication);
        return new ResponseEntity<>("Card renewed",HttpStatus.CREATED);
    }

}




