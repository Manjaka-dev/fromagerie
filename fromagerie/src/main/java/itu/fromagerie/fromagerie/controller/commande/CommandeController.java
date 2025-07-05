package itu.fromagerie.fromagerie.controller.commande;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.service.commande.CommandeService;
import itu.fromagerie.fromagerie.service.commande.LigneCommandeService;
import itu.fromagerie.fromagerie.service.produit.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCommandes() {
        try {
            List<Commande> commandes = commandeService.getAllCommandes();
            Map<String, Object> response = new HashMap<>();
            response.put("commandes", commandes);
            response.put("count", commandes.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des commandes: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCommandeById(@PathVariable Long id) {
        try {
            Optional<Commande> commande = commandeService.getCommandeById(id);
            if (commande.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("commande", commande.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Commande non trouvée avec l'ID: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération de la commande: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{clientId}")
    public ResponseEntity<Map<String, Object>> createCommande(@PathVariable Long clientId, @RequestBody Map<String, String> formData) {
        try {
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

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveCommande(@RequestBody Map<String, Object> request) {
        try {
            Long clientId = Long.valueOf((Integer) request.get("clientId"));
            Commande commande = commandeService.saveCommande(clientId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Commande sauvegardée avec succès");
            response.put("commande", commande);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la sauvegarde de la commande: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCommande(@PathVariable Long id, @RequestBody Commande commande) {
        try {
            commande.setId(id);
            Commande updatedCommande = commandeService.updateCommande(commande);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Commande mise à jour avec succès");
            response.put("commande", updatedCommande);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la mise à jour de la commande: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCommande(@PathVariable Long id) {
        try {
            commandeService.deleteCommande(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Commande supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la suppression de la commande: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
