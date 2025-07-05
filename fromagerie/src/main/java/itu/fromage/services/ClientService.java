package itu.fromage.services;

import itu.fromage.entities.Client;
import itu.fromage.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> AllClient(){
        return clientRepository.findAll();
    }
}
