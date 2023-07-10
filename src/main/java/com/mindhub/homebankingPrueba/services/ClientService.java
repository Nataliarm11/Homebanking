package com.mindhub.homebankingPrueba.services;

import com.mindhub.homebankingPrueba.dtos.ClientDTO;
import com.mindhub.homebankingPrueba.models.Client;


import java.util.Set;

public interface ClientService {
    Set<ClientDTO> getClientsDTO();
    ClientDTO getClientDTO(Long id);
    void saveClient(Client client);
    Client findById(Long id);
    Client findByEmail(String email);

}
