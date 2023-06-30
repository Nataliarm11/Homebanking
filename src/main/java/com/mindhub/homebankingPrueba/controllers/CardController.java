package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.repositories.CardRepository;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
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
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    //CVV
    short minCvv = 123;
    short maxCvv = 989;

    public short getRandomNumberCvv(short minCvv, short maxCvv) {
        return (short) ((Math.random() * (maxCvv - minCvv)) + minCvv);
    }

    public String generateCardNumber() {
        String cardNumber = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardNumber += random.nextInt(10);
            }
            if (i < 3) {
                cardNumber += "-";
            }
        }
        return cardNumber;
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCards( @RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication ){
        Client client = clientRepository.findByEmail(authentication.getName());
        if (cardRepository.findByClientAndColorAndType(client, cardColor, cardType)!=null){
            return new ResponseEntity<>("Excuse me, forbidden", HttpStatus.FORBIDDEN);
       }

//        Set<Card> cards = client.getCards();
//        List <Card> typeCards = cards.stream()
//                .filter(card -> card.getType() == cardType)
//                .collect(Collectors.toList());
//        List<Card> typeColor=cards.stream()
//                .filter(card ->card.getColor() == cardColor)
//                .collect(Collectors.toList());
//
//        if(cards.size()>=6){
//            return new ResponseEntity<>("Excuse me, you can't have more than six cards.",HttpStatus.FORBIDDEN);
//        }
//
//        if(typeCards.size() == 3){
//            return new ResponseEntity<>("Excuse me, you can not have three cards of the same type", HttpStatus.FORBIDDEN);
//        }
//
//        if(typeColor.size() == 1){
//            return new ResponseEntity<>("Excuse me, you can not have three cards of the same type", HttpStatus.FORBIDDEN);
//        }

        String cardHolderClient = client.getFirstName() + " " + client.getLastName();
        String cardNumber = generateCardNumber();
        short numberCvv = getRandomNumberCvv(minCvv, maxCvv);
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime thruDate = fromDate.plusYears(5);

        Card cardNew = new Card(cardHolderClient, cardType, cardColor, cardNumber,numberCvv, thruDate, fromDate);
        client.addCard(cardNew);
        cardRepository.save(cardNew);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

