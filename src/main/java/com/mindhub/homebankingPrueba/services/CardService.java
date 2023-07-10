package com.mindhub.homebankingPrueba.services;

import com.mindhub.homebankingPrueba.models.Card;
import com.mindhub.homebankingPrueba.models.CardColor;
import com.mindhub.homebankingPrueba.models.CardType;
import com.mindhub.homebankingPrueba.models.Client;

public interface CardService {

    boolean existsByNumber(String number);

    boolean existsByClientAndColorAndType(Client client, CardColor cardColor, CardType cardType);

    public void saveCard (Card card);
}
