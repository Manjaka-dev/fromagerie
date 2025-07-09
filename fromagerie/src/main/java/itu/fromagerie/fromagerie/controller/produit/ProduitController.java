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
import java.util.Objects;
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
    
    // ===== NOUVEAUX ENDPOINTS =====
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProduitsByNom(@RequestParam String nom) {
        try {
            List<Produit> produits = produitService.AllProduit().stream()
                    .filter(p -> p.getNom().toLowerCase().contains(nom.toLowerCase()))
                    .collect(Collectors.toList());
            
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
            // Récupérer les catégories uniques des produits existants
            List<String> categories = produitService.AllProduit().stream()
                    .map(p -> p.getCategorie() != null ? p.getCategorie().getNom() : null)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            
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
            List<Produit> produits = produitService.AllProduit();
            
            Map<String, Object> response = new HashMap<>();
            response.put("nombreTotalProduits", produits.size());
            response.put("nombreCategories", produits.stream()
                    .map(p -> p.getCategorie() != null ? p.getCategorie().getNom() : null)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count());
            response.put("prixMoyen", produits.stream()
                    .filter(p -> p.getPrixVente() != null)
                    .mapToDouble(p -> p.getPrixVente().doubleValue())
                    .average()
                    .orElse(0.0));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors du calcul des statistiques: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/stock-faible")
    public ResponseEntity<Map<String, Object>> getProduitsStockFaible() {
        try {
            List<Produit> produits = produitService.AllProduit();
            List<Map<String, Object>> produitsStockFaible = new ArrayList<>();
            
            for (Produit produit : produits) {
                int quantite = produitService.getQuantiteProduit(produit.getId());
                if (quantite < 10) { // Seuil d'alerte arbitraire
                    Map<String, Object> produitInfo = new HashMap<>();
                    produitInfo.put("produit", produit);
                    produitInfo.put("quantiteDisponible", quantite);
                    produitsStockFaible.add(produitInfo);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("produits", produitsStockFaible);
            response.put("nombreProduits", produitsStockFaible.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des produits à stock faible: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/{id}/ajuster-stock")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Ajuster le stock d'un produit",
        description = "Modifie la quantité en stock d'un produit et enregistre l'opération",
        tags = {"Produits", "Stock"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Stock ajusté avec succès"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Erreur lors de l'ajustement du stock"
        )
    })
    public ResponseEntity<Map<String, Object>> ajusterStock(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID du produit") 
            @PathVariable Long id, 
            @io.swagger.v3.oas.annotations.Parameter(description = "Informations d'ajustement (quantite, raison)") 
            @RequestBody Map<String, Object> ajustement) {
        try {
            // Utiliser le service pour effectuer l'ajustement
            Map<String, Object> result = produitService.ajusterStock(id, ajustement);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur lors de l'ajustement du stock: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 