package com.mindhub.homebankingPrueba.services.implement;

import com.mindhub.homebankingPrueba.models.Card;
import com.mindhub.homebankingPrueba.models.CardColor;
import com.mindhub.homebankingPrueba.models.CardType;
import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.repositories.CardRepository;
import com.mindhub.homebankingPrueba.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {
     @Autowired
    CardRepository cardRepository;

    @Override
    public boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public boolean existsByClientAndColorAndType(Client client, CardColor cardColor, CardType cardType) {
        return cardRepository.existsByClientAndColorAndType(client, cardColor, cardType) ;
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);

    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number) ;
    }
}
