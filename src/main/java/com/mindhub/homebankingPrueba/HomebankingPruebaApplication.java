package com.mindhub.homebankingPrueba;

import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingPruebaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingPruebaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData (ClientRepository clientRepository){
		return args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Natalia", "Requena", "nrequena@gmail.com");

			clientRepository.save(client1);
			clientRepository.save(client2);
		};
	}


}
