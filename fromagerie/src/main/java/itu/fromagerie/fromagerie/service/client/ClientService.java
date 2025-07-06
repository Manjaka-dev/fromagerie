package itu.fromagerie.fromagerie.service.client;

import itu.fromagerie.fromagerie.entities.vente.Client;
import itu.fromagerie.fromagerie.repository.vente.ClientRepository;
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
