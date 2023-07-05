package com.mindhub.homebankingPrueba.repositories;

import com.mindhub.homebankingPrueba.models.Card;
import com.mindhub.homebankingPrueba.models.CardColor;
import com.mindhub.homebankingPrueba.models.CardType;
import com.mindhub.homebankingPrueba.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean  existsByClientAndColorAndType(Client client, CardColor cardColor, CardType cardType);
    boolean existsByNumber(String number);
}
