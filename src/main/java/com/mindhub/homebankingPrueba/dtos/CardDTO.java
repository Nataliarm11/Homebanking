package com.mindhub.homebankingPrueba.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebankingPrueba.models.Card;
import com.mindhub.homebankingPrueba.models.CardColor;
import com.mindhub.homebankingPrueba.models.CardType;
import com.mindhub.homebankingPrueba.models.Client;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class CardDTO {
    private long id;

    private String cardHolder;

    private CardType type;

    private CardColor color;

    private String number;

    private short cvv;

    private LocalDateTime thruDate;

    private LocalDateTime fromDate;

    private Client client;

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
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }


    public short getCvv() {
        return cvv;
    }


    public LocalDateTime getThruDate() {
        return thruDate;
    }


    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
