package com.mindhub.homebankingPrueba;

import com.mindhub.homebankingPrueba.models.*;
import com.mindhub.homebankingPrueba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;


@SpringBootApplication
public class HomebankingPruebaApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingPruebaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return args -> {


			// Creo los clientes
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("Melba12345"));
			Client client2 = new Client("Natalia", "Requena", "nrequena@gmail.com", passwordEncoder.encode("Nat12345"));
			Client admin = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("admin12345"));

			// Guardo los clientes en la base de datos
			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(admin);

			// Creo la primera cuenta para el cliente Melba
			Account account1 = new Account("VIN001", LocalDate.now(), 5000.0, true, AccountType.SAVINGS);

			// Creo la segunda cuenta para el cliente Melba
			LocalDate nextDay = LocalDate.now().plusDays(1);
			Account account2 = new Account("VIN002", nextDay, 7500.0, true, AccountType.CURRENT);

			// Creo otras cuentas para el segundo cliente
			Account account3 = new Account("VIN003", LocalDate.now(), 10000.0, true, AccountType.SAVINGS);

			LocalDate laterDay = LocalDate.now().plusDays(3);
			Account account4 = new Account("VIN004", laterDay, 9000.0, true, AccountType.CURRENT);

			// Creo las transacciones para las cuentas
			// VIN001
			Transaction transaction1 = new Transaction ( TransactionType.DEBIT,-250.0, "Paying members" , LocalDateTime.now(), 4.000, true);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 1000.0, "Transfer of payment for sale", LocalDateTime.now().plusDays(2), 5.000, true);

			//VIN002
			Transaction transaction3 = new Transaction ( TransactionType.DEBIT,-25.0, "dinner payment" , LocalDateTime.now(), 7.625, true);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 10.0, "Transfer of payment", LocalDateTime.now().plusDays(3),7.600 , true);
			Transaction transaction5 = new Transaction ( TransactionType.DEBIT,-10.0, "drink payment" , LocalDateTime.now().plusDays(4), 7.500, true);

			//VIN003
			Transaction transaction6 = new Transaction ( TransactionType.DEBIT,-400.0, "monthly course payment" , LocalDateTime.now(), 500.0, true);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 1950.0, "payment of salary", LocalDateTime.now(), 1.500, true);
			Transaction transaction8 = new Transaction ( TransactionType.DEBIT,-5.0, "shop on subway" , LocalDateTime.now(), 5.000, true);
			Transaction transaction9 = new Transaction(TransactionType.CREDIT, 900.0, "payment for work done", LocalDateTime.now(), 1.000, true);

			//VIN004
			Transaction transaction10 = new Transaction(TransactionType.CREDIT, 850.0, "Transfer for work", LocalDateTime.now(), 1.000, true);


			// Crear tres prestamos y guardarlos en la base de datos
			Loan loan1 = new Loan("Mortgage",500000.00, Arrays.asList(12,24,36,48,60),15);

			Loan loan2 = new Loan("Personal", 100000.00, Arrays.asList(6,12,24), 25 );

			Loan loan3 = new Loan("Auto", 300000.00, Arrays.asList(6,12,24,36), 35);


			// ClientLoan  asocio con los clientes y préstamos correspondientes

			ClientLoan mortgageMelba = new ClientLoan(400000.00, 60, 42, 300000.0);
			ClientLoan personalMelba = new ClientLoan(50000.00, 12, 12,300000.0 );
			ClientLoan personalNatalia = new ClientLoan(100000.0, 24, 12, 12211);
			ClientLoan autoNatalia = new ClientLoan(200000.0, 36, 12, 12211);

			// Crear tarjeta de débito GOLD para Melba
			LocalDate fromDate = LocalDate.now();
			LocalDate thruDate = fromDate.plusYears(5);
			LocalDate expCard = fromDate.minusDays(10);

			//Crear tarjeta de débito GOLD para Melba
			String cardHolderMelba = client1.getFirstName() + " " + client1.getLastName();
			Card debitCard1 = new Card(cardHolderMelba, CardType.DEBIT, CardColor.GOLD, "4000-0012-3456-7899", (short) 693, thruDate, fromDate, true);

			// Crear tarjeta de crédito Titanium para Melba
			Card creditCard1 = new Card(cardHolderMelba, CardType.CREDIT, CardColor.TITANIUM, "5303-6101-4684-9428", (short) 369, expCard, fromDate, true);

			// Crear tarjeta de crédito Silver para el segundo cliente
			String cardHolderNatalia = client2.getFirstName() + " " + client2.getLastName();
			Card silverCard1 = new Card(cardHolderNatalia, CardType.CREDIT, CardColor.SILVER, "3694-4842-4753-2348", (short) 789, thruDate, fromDate, true);


			//Asocio las cuentas
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			//Asocio las transacciones con las cuentas
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account2.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account3.addTransaction(transaction7);
			account3.addTransaction(transaction8);
			account3.addTransaction(transaction9);
			account4.addTransaction(transaction10);


			// Guardo las cuentas en la base de datos
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			//Guardo las transacciones en la base de datos
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);


			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);


			//asocio clientLoan con client

			client1.addClientLoan(mortgageMelba);
			loan1.addClientLoan(mortgageMelba);

			client1.addClientLoan(personalMelba);
			loan2.addClientLoan(personalMelba);

			client2.addClientLoan(personalNatalia);
			loan2.addClientLoan(personalNatalia);

			client2.addClientLoan(autoNatalia);
			loan3.addClientLoan(autoNatalia);

			// Guardo en base
			clientLoanRepository.save(mortgageMelba);
			clientLoanRepository.save(personalMelba);
			clientLoanRepository.save(personalNatalia);
			clientLoanRepository.save(autoNatalia);

			//Asocio y guardo
			client1.addCard(debitCard1);
			cardRepository.save(debitCard1);
			client1.addCard(creditCard1);
			cardRepository.save(creditCard1);
			client2.addCard(silverCard1);
			cardRepository.save(silverCard1);


		};

	}


}
