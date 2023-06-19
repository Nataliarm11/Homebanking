package com.mindhub.homebankingPrueba.controllers;

import com.mindhub.homebankingPrueba.dtos.ClientDTO;

import com.mindhub.homebankingPrueba.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;
import static java.util.stream.Collectors.toSet;


@RequestMapping("/api") @RestController
public class ClientController {

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
}
