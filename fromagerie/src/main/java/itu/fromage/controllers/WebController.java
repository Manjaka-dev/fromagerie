package itu.fromage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/web")
public class WebController {

    @GetMapping("/home")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenue sur l'API de la Fromagerie");
        response.put("version", "1.0");
        response.put("endpoints", new String[]{
            "/api/web/commandes",
            "/api/web/livraisons", 
            "/api/web/produits"
        });
        return ResponseEntity.ok(response);
    }

    @GetMapping("/commandes")
    public ResponseEntity<Map<String, Object>> commandes() {
        Map<String, Object> response = new HashMap<>();
        response.put("page", "commandes");
        response.put("title", "Gestion des Commandes");
        response.put("description", "Interface de gestion des commandes clients");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/livraisons")
    public ResponseEntity<Map<String, Object>> livraisons() {
        Map<String, Object> response = new HashMap<>();
        response.put("page", "livraisons");
        response.put("title", "Gestion des Livraisons");
        response.put("description", "Interface de gestion des livraisons");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/produits")
    public ResponseEntity<Map<String, Object>> produits() {
        Map<String, Object> response = new HashMap<>();
        response.put("page", "produits");
        response.put("title", "Catalogue des Produits");
        response.put("description", "Interface de consultation des produits");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/create/livraison/{id}")
    public ResponseEntity<Map<String, Object>> createLivraison(@PathVariable("id") int id) {
        Map<String, Object> response = new HashMap<>();
        response.put("page", "livraison-form");
        response.put("title", "Créer une Livraison");
        response.put("commandeId", id);
        response.put("description", "Formulaire de création de livraison pour la commande " + id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirmation-paiement/{livraisonId}")
    public ResponseEntity<Map<String, Object>> confirmationPaiement(@PathVariable("livraisonId") Integer livraisonId) {
        Map<String, Object> response = new HashMap<>();
        response.put("page", "confirmation-paiement");
        response.put("title", "Confirmation de Paiement");
        response.put("livraisonId", livraisonId);
        response.put("description", "Page de confirmation de paiement pour la livraison " + livraisonId);
        return ResponseEntity.ok(response);
    }
} 