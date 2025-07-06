package itu.fromagerie.fromagerie.controller.commande;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.vente.LigneCommande;
import itu.fromagerie.fromagerie.dto.commande.CommandeDTO;
import itu.fromagerie.fromagerie.service.commande.CommandeService;
import itu.fromagerie.fromagerie.service.commande.LigneCommandeService;
import itu.fromagerie.fromagerie.service.produit.ProduitService;
import itu.fromagerie.fromagerie.service.livraison.LivraisonService;
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
    private final LivraisonService livraisonService;
    
    @Autowired
    public CommandeController(ProduitService produitService, LigneCommandeService ligneCommandeService, CommandeService commandeService, LivraisonService livraisonService) {
        this.produitService = produitService;
        this.ligneCommandeService = ligneCommandeService;
        this.commandeService = commandeService;
        this.livraisonService = livraisonService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCommandes() {
        try {
            List<CommandeDTO> commandes = commandeService.getAllCommandesAsDTO();
            
            // Debug: afficher les données des commandes
            System.out.println("=== DEBUG COMMANDES DTO ===");
            for (CommandeDTO commande : commandes) {
                System.out.println("Commande ID: " + commande.getId());
                System.out.println("  - Client: " + (commande.getClient() != null ? commande.getClient().getNom() : "NULL"));
                System.out.println("  - Lignes: " + (commande.getLignesCommande() != null ? commande.getLignesCommande().size() : "NULL"));
                if (commande.getLignesCommande() != null) {
                    for (CommandeDTO.LigneCommandeDTO ligne : commande.getLignesCommande()) {
                        System.out.println("    - Produit: " + (ligne.getProduit() != null ? ligne.getProduit().getNom() : "NULL"));
                        System.out.println("    - Quantité: " + ligne.getQuantite());
                    }
                }
            }
            System.out.println("=== FIN DEBUG ===");
            
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

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCommande(@RequestBody Map<String, Object> request) {
        try {
            Long clientId = Long.valueOf(request.get("clientId").toString());
            String dateCommande = (String) request.get("dateCommande");
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> produits = (List<Map<String, Object>>) request.get("produits");
            
            Commande commande = commandeService.createCommandeWithProducts(clientId, dateCommande, produits);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Commande créée avec succès");
            response.put("commande", commande);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création de la commande: " + e.getMessage());
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

    @GetMapping("/livreurs")
    public ResponseEntity<Map<String, Object>> getLivreurs() {
        try {
            List<itu.fromagerie.fromagerie.entities.livraison.Livreur> livreurs = livraisonService.listeLivreur();
            Map<String, Object> response = new HashMap<>();
            response.put("livreurs", livreurs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des livreurs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{commandeId}/livraison")
    public ResponseEntity<Map<String, Object>> createLivraison(@PathVariable Long commandeId, @RequestBody Map<String, Object> request) {
        try {
            Long livreurId = Long.valueOf(request.get("livreurId").toString());
            String zone = (String) request.get("zone");
            String dateLivraisonStr = (String) request.get("dateLivraison");
            
            java.time.LocalDate dateLivraison = java.time.LocalDate.parse(dateLivraisonStr);
            
            livraisonService.saveLivraison(commandeId, dateLivraison, zone, livreurId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livraison créée avec succès");
            response.put("commandeId", commandeId);
            response.put("livreurId", livreurId);
            response.put("zone", zone);
            response.put("dateLivraison", dateLivraison);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création de la livraison: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
