package com.mindhub.homebankingPrueba;

import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    //Loan
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    //Account
    @Test
    public void existAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));
    }

    @Test
    public void testExistsByNumber() {
        boolean exists = accountRepository.existsByNumber("VIN002");
        assertTrue(exists);
    }

    //Client

    @Test
    public void testFindByEmail() {
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        assertEquals("melba@mindhub.com", client.getEmail());
    }

    @Test
    public void testClientHasCard() {
        Client client = clientRepository.findByEmail("nrequena@gmail.com");
        assertFalse(client.getCards().isEmpty());
    }


    //Card
    @Test
    public void testAllCardsHaveEightDigits() {
        List<Card> cards = cardRepository.findAll();
        String cardNumber = cards.get(0).getNumber();
        assertEquals(19, cardNumber.length());
    }

    @Test
    public void testExistsByClientAndColorAndType() {
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        boolean exists = cardRepository.existsByClientAndColorAndType(client, CardColor.GOLD, CardType.DEBIT);
        assertTrue(exists);
    }


    //Transaction
    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("description", is("Paying members"))));
    }

    @Test
    public void existTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }


}
