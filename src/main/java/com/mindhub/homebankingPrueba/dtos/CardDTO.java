package com.mindhub.homebankingPrueba.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebankingPrueba.models.Card;
import com.mindhub.homebankingPrueba.models.CardColor;
import com.mindhub.homebankingPrueba.models.CardType;
import com.mindhub.homebankingPrueba.models.Client;

import java.time.LocalDate;

public class CardDTO {

    private long id;

    private String cardHolder;

    private CardType type;

    private CardColor color;

    private String number;

    private short cvv;

    private LocalDate thruDate;

    private LocalDate fromDate;

    private Client client;

    private boolean activeCard;

    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.id=card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType() ;
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.activeCard = card.getActiveCard();
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public short getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public boolean getActiveCard() {
        return activeCard;
    }

}
