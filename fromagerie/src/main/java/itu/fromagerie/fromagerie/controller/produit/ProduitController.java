package itu.fromagerie.fromagerie.controller.produit;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.vente.Client;
import itu.fromagerie.fromagerie.dto.produit.ProduitSearchDTO;
import itu.fromagerie.fromagerie.dto.produit.CategorieProduitDTO;
import itu.fromagerie.fromagerie.dto.produit.ProduitStatsDTO;
import itu.fromagerie.fromagerie.dto.produit.AjustementStockDTO;
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
    
    // NOUVEAUX ENDPOINTS
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProduitsByNom(@RequestParam String nom) {
        try {
            List<ProduitSearchDTO> produits = produitService.searchProduitsByNom(nom);
            Map<String, Object> response = new HashMap<>();
            response.put("produits", produits);
            response.put("nombreResultats", produits.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la recherche: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        try {
            List<CategorieProduitDTO> categories = produitService.getAllCategories();
            Map<String, Object> response = new HashMap<>();
            response.put("categories", categories);
            response.put("nombreCategories", categories.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des catégories: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getProduitStats() {
        try {
            ProduitStatsDTO stats = produitService.getProduitStats();
            Map<String, Object> response = new HashMap<>();
            response.put("statistiques", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des statistiques: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/stock-faible")
    public ResponseEntity<Map<String, Object>> getProduitsStockFaible() {
        try {
            List<ProduitSearchDTO> produits = produitService.getProduitsStockFaible();
            Map<String, Object> response = new HashMap<>();
            response.put("produits", produits);
            response.put("nombreProduits", produits.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des produits en stock faible: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/{id}/ajuster-stock")
    public ResponseEntity<Map<String, Object>> ajusterStock(@PathVariable Long id, @RequestBody AjustementStockDTO ajustement) {
        try {
            produitService.ajusterStock(id, ajustement.getQuantite(), ajustement.getType());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock ajusté avec succès");
            response.put("produitId", id);
            response.put("quantiteAjustee", ajustement.getQuantite());
            response.put("typeAjustement", ajustement.getType());
            response.put("raison", ajustement.getRaison());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de l'ajustement du stock: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 