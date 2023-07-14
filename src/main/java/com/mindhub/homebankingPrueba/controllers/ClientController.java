package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.ClientDTO;

import com.mindhub.homebankingPrueba.models.Account;
import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.repositories.AccountRepository;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import com.mindhub.homebankingPrueba.services.AccountService;
import com.mindhub.homebankingPrueba.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public Set<ClientDTO> getClientsDTO(){
        return clientService.getClientsDTO();
    }

    @GetMapping("clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id){
        return clientService.getClientDTO(id);

    }

    int minAccount = 12341234;
    int maxAccount = 98767896;

    public int getRandomNumberAccount(int minAccount, int maxAccount) {
        return (int) ((Math.random() * (maxAccount - minAccount)) + minAccount);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){

        if (firstName.isBlank()) {
            return new ResponseEntity<>("the firstName is missing", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()) {
            return new ResponseEntity<>("the lastName is missing", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()) {
            return new ResponseEntity<>("the email is missing", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()) {
            return new ResponseEntity<>("the password is missing", HttpStatus.FORBIDDEN);
        }

        if(clientService.findByEmail(email) != null){
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client newClient =  new Client(firstName, lastName, email, passwordEncoder.encode(password));

        String newAccountNumber;
        boolean accountNumberExists;

        do {
            newAccountNumber = "VIN-" + getRandomNumberAccount(minAccount, maxAccount);
            accountNumberExists =  accountService.existsByNumber(newAccountNumber);
        } while (accountNumberExists);

        Account accountNew = new Account(newAccountNumber, LocalDate.now(), 0);

        accountService.saveAccount(accountNew);
        newClient.addAccount(accountNew);
        clientService.saveClient(newClient);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName()));

    }

}
