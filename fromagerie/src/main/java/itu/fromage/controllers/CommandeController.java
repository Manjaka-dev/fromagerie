package itu.fromage.controllers;

import itu.fromage.entities.Commande;
import itu.fromage.services.CommandeService;
import itu.fromage.services.LigneCommandeService;
import itu.fromage.services.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {
    private final ProduitService produitService;
    private final LigneCommandeService ligneCommandeService;
    private final CommandeService commandeService;
    
    @Autowired
    public CommandeController(ProduitService produitService, LigneCommandeService ligneCommandeService, CommandeService commandeService) {
        this.produitService = produitService;
        this.ligneCommandeService = ligneCommandeService;
        this.commandeService = commandeService;
    }

    @PostMapping("/panier")
    public ResponseEntity<Map<String, Object>> ajouterAuPanier(@RequestBody Map<String, Object> request) {
        try {
            Integer clientId = (Integer) request.get("clientId");
            Map<String, String> formData = new HashMap<>();
            
            // Convertir les données du request body en Map<String, String>
            for (Map.Entry<String, Object> entry : request.entrySet()) {
                if (!entry.getKey().equals("clientId")) {
                    formData.put(entry.getKey(), entry.getValue().toString());
                }
            }
            
            commandeService.creerCommandeAvecLignes(clientId, formData);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Commande créée avec succès");
            response.put("clientId", clientId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création de la commande: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
