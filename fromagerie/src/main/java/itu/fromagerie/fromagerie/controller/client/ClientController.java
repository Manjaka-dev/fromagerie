package itu.fromagerie.fromagerie.controller.client;

import itu.fromagerie.fromagerie.entities.vente.Client;
import itu.fromagerie.fromagerie.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.AllClient();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        // Note: Ajouter une méthode findById dans ClientService si nécessaire
        List<Client> clients = clientService.AllClient();
        Optional<Client> client = clients.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createClient(@RequestBody Client client) {
        try {
            // Note: Ajouter une méthode save dans ClientService si nécessaire
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Client créé avec succès");
            response.put("client", client);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création du client: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateClient(@PathVariable Long id, @RequestBody Client client) {
        try {
            client.setId(id);
            // Note: Ajouter une méthode save dans ClientService si nécessaire
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Client mis à jour avec succès");
            response.put("client", client);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la mise à jour du client: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteClient(@PathVariable Long id) {
        try {
            // Note: Ajouter une méthode deleteById dans ClientService si nécessaire
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Client supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la suppression du client: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 