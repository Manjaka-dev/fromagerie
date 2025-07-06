package itu.fromagerie.fromagerie.controller.produit;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.vente.Client;
import itu.fromagerie.fromagerie.service.client.ClientService;
import itu.fromagerie.fromagerie.service.produit.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;
    private final ClientService clientService;
    
    @Autowired
    public ProduitController(ProduitService produitService, ClientService clientService) {
        this.produitService = produitService;
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProduits() {
        List<Produit> produits = produitService.AllProduit();

        List<Integer> quantites = produits.stream()
                .map(produit -> produitService.getQuantiteProduit(produit.getId()))
                .collect(Collectors.toList());

        List<Client> clients = clientService.AllClient();

        Map<String, Object> response = new HashMap<>();
        response.put("produits", produits);
        response.put("quantiteDisponible", quantites);
        response.put("listeClient", clients);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProduitById(@PathVariable Long id) {
        try {
            Produit produit = produitService.getProduitById(id);
            if (produit != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("produit", produit);
                response.put("quantiteDisponible", produitService.getQuantiteProduit(id));
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Produit non trouvé avec l'ID: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération du produit: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduit(@RequestBody Produit produit) {
        try {
            Produit savedProduit = produitService.saveProduit(produit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Produit créé avec succès");
            response.put("produit", savedProduit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création du produit: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        try {
            produit.setId(id);
            Produit updatedProduit = produitService.updateProduit(produit);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Produit mis à jour avec succès");
            response.put("produit", updatedProduit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la mise à jour du produit: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduit(@PathVariable Long id) {
        try {
            produitService.deleteProduit(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Produit supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la suppression du produit: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 