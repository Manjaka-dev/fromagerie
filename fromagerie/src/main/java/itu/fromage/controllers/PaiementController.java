package itu.fromage.controllers;

import itu.fromage.entities.Paiement;
import itu.fromage.services.LivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {
    private final LivraisonService livraisonService;

    @Autowired
    public PaiementController(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPaiement(@RequestBody Map<String, Object> request) {
        try {
            Integer commandeId = (Integer) request.get("commandeId");
            Double montant = (Double) request.get("montant");
            String methode = (String) request.get("methode");
            String datePaiementStr = (String) request.get("datePaiement");
            
            LocalDate datePaiement = LocalDate.parse(datePaiementStr);
            
            livraisonService.enregistrerPaiement(commandeId, montant, methode, datePaiement);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Paiement enregistré avec succès");
            response.put("commandeId", commandeId);
            response.put("montant", montant);
            response.put("methode", methode);
            response.put("datePaiement", datePaiement);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de l'enregistrement du paiement: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<Map<String, Object>> getPaiementsByCommande(@PathVariable Integer commandeId) {
        try {
            // Note: Ajouter une méthode pour récupérer les paiements par commande
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Paiements récupérés avec succès");
            response.put("commandeId", commandeId);
            // response.put("paiements", paiements);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des paiements: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 