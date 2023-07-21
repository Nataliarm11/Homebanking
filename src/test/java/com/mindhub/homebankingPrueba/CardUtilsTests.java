package com.mindhub.homebankingPrueba;

import com.mindhub.homebankingPrueba.services.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.mindhub.homebankingPrueba.utils.CardUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@SpringBootTest
public class CardUtilsTests {

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.generateCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }
}
