package com.mindhub.homebankingPrueba;

import com.mindhub.homebankingPrueba.models.Account;
import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.models.Transaction;
import com.mindhub.homebankingPrueba.models.TransactionType;
import com.mindhub.homebankingPrueba.repositories.AccountRepository;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import com.mindhub.homebankingPrueba.repositories.TransactionRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingPruebaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingPruebaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return args -> {

			// Creo los clientes
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Natalia", "Requena", "nrequena@gmail.com");

			// Guardo los clientes en la base de datos
			clientRepository.save(client1);
			clientRepository.save(client2);

			// Creo la primera cuenta para el cliente Melba
			Account account1 = new Account("VIN001", LocalDate.now(), 5000.0);

			// Creo la segunda cuenta para el cliente Melba
			LocalDate nextDay = LocalDate.now().plusDays(1);
			Account account2 = new Account("VIN002", nextDay, 7500.0);


			// Creo otras cuentas para el segundo cliente
			Account account3 = new Account("VIN003", LocalDate.now(), 10000.0);

			LocalDate laterDay = LocalDate.now().plusDays(3);
			Account account4 = new Account("VIN004", laterDay, 9000.0);

			// Creo las transacciones para las cuentas
			// VIN001
			Transaction transaction1 = new Transaction ( TransactionType.DEBIT,-250.0, "Paying members" , LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 1000.0, "Transfer of payment for sale", LocalDateTime.now().plusDays(2));

			//VIN002
			Transaction transaction3 = new Transaction ( TransactionType.DEBIT,-25.0, "dinner payment" , LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 10.0, "Transfer of payment", LocalDateTime.now().plusDays(3));
			Transaction transaction5 = new Transaction ( TransactionType.DEBIT,-10.0, "drink payment" , LocalDateTime.now().plusDays(4));



			//VIN003
			Transaction transaction6 = new Transaction ( TransactionType.DEBIT,-400.0, "monthly course payment" , LocalDateTime.now());
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 1950.0, "payment of salary", LocalDateTime.now());
			Transaction transaction8 = new Transaction ( TransactionType.DEBIT,-5.0, "shop on subway" , LocalDateTime.now());
			Transaction transaction9 = new Transaction(TransactionType.CREDIT, 900.0, "payment for work done", LocalDateTime.now());



			//VIN004
			Transaction transaction10 = new Transaction(TransactionType.CREDIT, 850.0, "Transfer for work", LocalDateTime.now());


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

		};

	}



}
