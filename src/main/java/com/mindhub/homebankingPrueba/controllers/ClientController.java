package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.ClientDTO;

import com.mindhub.homebankingPrueba.models.Client;
import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import static java.util.stream.Collectors.toSet;


@RequestMapping("/api")
@RestController
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/clients")
    public Set<ClientDTO> getClientsDTO(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toSet());
    }

    @RequestMapping("clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id){
        return clientRepository.findById(id)
                .map(ClientDTO::new)
                .orElse(null);

    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){

        if(firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(clientRepository.findByEmail(email) != null){
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));

    }





}
