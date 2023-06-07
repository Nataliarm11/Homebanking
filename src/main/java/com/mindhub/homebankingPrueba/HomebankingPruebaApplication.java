package com.mindhub.homebankingPrueba;

import com.mindhub.homebankingPrueba.models.Account;
import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.repositories.AccountRepository;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;

@SpringBootApplication
public class HomebankingPruebaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingPruebaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
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

			//Asocio las cuentas
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			// Guardo las cuentas en la base de datos
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

		};
	}



}
