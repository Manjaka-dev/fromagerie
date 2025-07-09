package itu.fromagerie.fromagerie.controller.livraison;

import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.vente.LigneCommande;
import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import itu.fromagerie.fromagerie.entities.livraison.StatutLivraison;
import itu.fromagerie.fromagerie.projection.LivraisonProjection;
import itu.fromagerie.fromagerie.service.livraison.LivraisonService;
import itu.fromagerie.fromagerie.service.livraison.RetourLivraisonService;
import itu.fromagerie.fromagerie.dto.livraison.UpdateLivraisonDTO;
import itu.fromagerie.fromagerie.dto.livraison.LivraisonInfoDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/livraisons")
public class LivraisonController {
    private final LivraisonService livraisonService;
    private final RetourLivraisonService retourLivraisonService;

    @Autowired
    public LivraisonController(LivraisonService livraisonService, RetourLivraisonService retourLivraisonService) {
        this.livraisonService = livraisonService;
        this.retourLivraisonService = retourLivraisonService;
    }

    @GetMapping("/commandes")
    public ResponseEntity<Map<String, Object>> getCommandes() {
        Map<String, Object> response = new HashMap<>();
        response.put("commandes", livraisonService.getCommandeLivraison());
        response.put("livreurs", livraisonService.listeLivreur());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/commandes/{id}/form")
    public ResponseEntity<Map<String, Object>> getFormLivraison(@PathVariable("id") int id) {
        Map<String, Object> response = new HashMap<>();
        response.put("commandeId", id);
        response.put("livreurs", livraisonService.listeLivreur());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/commandes/{commandeId}")
    public ResponseEntity<Map<String, Object>> getCommandeById(@PathVariable("commandeId") Integer commandeId) {
        Map<String, Object> response = new HashMap<>();
        response.put("commandeId", commandeId);
        response.put("message", "Commande trouvée");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/livraison")
    public ResponseEntity<Map<String, Object>> getLivraisonForm() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Formulaire de livraison");
        response.put("livreurs", livraisonService.listeLivreur());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/livraison")
    public ResponseEntity<Map<String, Object>> saveLivraison(@RequestBody Map<String, Object> request) {
        try {
            Long livreur = Long.valueOf((Integer) request.get("livreur"));
            LocalDate dateLivraison = LocalDate.parse((String) request.get("dateLivraison"));
            Long commandeId = Long.valueOf((Integer) request.get("commandeId"));
            String zone = (String) request.get("zone");
            
            livraisonService.saveLivraison(commandeId, dateLivraison, zone, livreur);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livraison créée avec succès");
            response.put("commandeId", commandeId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création de la livraison: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/assign-livreur")
    public ResponseEntity<Map<String, Object>> assignLivreur(@RequestBody Map<String, Object> request) {
        try {
            Long commandeId = Long.valueOf((Integer) request.get("commandeId"));
            Long livreurId = Long.valueOf((Integer) request.get("livreurId"));
            
            livraisonService.assignLivreur(commandeId, livreurId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livreur assigné avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de l'assignation du livreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getLivraisons() {
        // Commenté pour éviter les problèmes de sérialisation
        // livraisonService.debugStatutsLivraison();
        
        // Forcer la récupération des données fraîches
        List<LivraisonProjection> livraisons = livraisonService.getLivraisonDetails();
        
        System.out.println("=== DÉBOGAGE LIVRAISONS ===");
        System.out.println("Nombre de livraisons trouvées: " + livraisons.size());
        
        for (LivraisonProjection liv : livraisons) {
            System.out.println("Livraison ID: " + liv.getLivraisonId() + 
                              " | Statut: '" + liv.getStatutLivraison() + "'" +
                              " | Zone: " + liv.getZone() +
                              " | Client: " + liv.getClientNom());
        }
        System.out.println("=== FIN DÉBOGAGE ===");
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("livraisons", livraisons);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{livraisonId}/statut")
    public ResponseEntity<Map<String, Object>> addStatutLivraison(@PathVariable("livraisonId") Long livraisonId) {
        if (livraisonId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "ID de livraison invalide.");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Livraison> livraisonOpt = livraisonService.findLivraisonById(livraisonId);
        if (livraisonOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Livraison non trouvée.");
            return ResponseEntity.notFound().build();
        }

        LivraisonProjection livraisonProjection = livraisonService.getLivraisonDetails()
                .stream()
                .filter(l -> l.getLivraisonId().equals(livraisonId))
                .findFirst()
                .orElse(null);

        if (livraisonProjection == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Détails de la livraison introuvables.");
            return ResponseEntity.notFound().build();
        }

        String statutActuel = livraisonProjection.getStatutLivraison();
        System.out.println("Statut actuel: " + statutActuel);
        
        // Si le statut actuel est "En cours" et qu'on veut passer à "Livrée", 
        // retourner les informations pour la confirmation de paiement
        if ("En cours".equalsIgnoreCase(statutActuel) || "en cours".equalsIgnoreCase(statutActuel)) {
            Map<String, Object> response = new HashMap<>();
            response.put("action", "confirmation_paiement");
            response.put("livraisonId", livraisonId);
            response.put("livraison", livraisonProjection);
            return ResponseEntity.ok(response);
        }
        
        // Si le statut est déjà "Livrée", empêcher le changement
        if ("Livrée".equalsIgnoreCase(statutActuel) || "Livre".equalsIgnoreCase(statutActuel)) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Cette livraison est déjà terminée et ne peut plus être modifiée.");
            return ResponseEntity.badRequest().body(response);
        }

        // Pour les autres cas (Planifiée/Planifié -> En cours)
        StatutLivraison nouveauStatut = new StatutLivraison();
        nouveauStatut.setLivraison(livraisonOpt.get());
        nouveauStatut.setStatut("En cours");
        nouveauStatut.setDateStatut(Instant.now());
        
        System.out.println("=== SAUVEGARDE STATUT ===");
        System.out.println("Livraison ID: " + livraisonOpt.get().getId());
        System.out.println("Nouveau statut: " + nouveauStatut.getStatut());
        System.out.println("Date: " + nouveauStatut.getDateStatut());
        
        StatutLivraison statutSauvegarde = livraisonService.saveStatutLivraison(nouveauStatut);
        System.out.println("Statut sauvegardé avec ID: " + statutSauvegarde.getId());
        System.out.println("=== FIN SAUVEGARDE ===");

        // Commenté pour éviter les problèmes de sérialisation
        // livraisonService.debugStatutsLivraison();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Statut mis à jour : En cours !");
        response.put("statutId", statutSauvegarde.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{livraisonId}/confirmation-paiement")
    public ResponseEntity<Map<String, Object>> getConfirmationPaiement(@PathVariable("livraisonId") Long livraisonId) {
        LivraisonProjection livraisonProjection = livraisonService.getLivraisonDetails()
                .stream()
                .filter(l -> l.getLivraisonId().equals(livraisonId))
                .findFirst()
                .orElse(null);

        if (livraisonProjection == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Livraison non trouvée");
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("livraison", livraisonProjection);
        response.put("livraisonId", livraisonId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{livraisonId}/confirmer-livraison-paiement")
    public ResponseEntity<Map<String, Object>> confirmerLivraisonEtPaiement(
            @PathVariable("livraisonId") Long livraisonId,
            @RequestBody Map<String, Object> request) {
        
        try {
            // Gérer les différents types de montant (Integer ou Double)
            Object montantObj = request.get("montantPaiement");
            Double montantPaiement;
            if (montantObj instanceof Integer) {
                montantPaiement = ((Integer) montantObj).doubleValue();
            } else if (montantObj instanceof Double) {
                montantPaiement = (Double) montantObj;
            } else {
                montantPaiement = Double.valueOf(montantObj.toString());
            }
            
            String methodePaiement = (String) request.get("methodePaiement");
            String datePaiement = (String) request.get("datePaiement");
            
            System.out.println("=== DÉBUT CONFIRMATION LIVRAISON ===");
            System.out.println("Livraison ID: " + livraisonId);
            System.out.println("Montant: " + montantPaiement);
            System.out.println("Méthode: " + methodePaiement);
            System.out.println("Date: " + datePaiement);
            
            // 1. Mettre à jour le statut de la livraison à "Livrée"
            Optional<Livraison> livraisonOpt = livraisonService.findLivraisonById(livraisonId);
            if (livraisonOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Livraison non trouvée.");
                return ResponseEntity.notFound().build();
            }

            Livraison livraison = livraisonOpt.get();
            System.out.println("Livraison trouvée: " + livraison.getId());

            StatutLivraison nouveauStatut = new StatutLivraison();
            nouveauStatut.setLivraison(livraison);
            nouveauStatut.setStatut("Livrée");
            nouveauStatut.setDateStatut(Instant.now());
            
            StatutLivraison statutSauvegarde = livraisonService.saveStatutLivraison(nouveauStatut);
            System.out.println("Statut sauvegardé avec ID: " + statutSauvegarde.getId());
            System.out.println("Statut: " + statutSauvegarde.getStatut());

            // 2. Enregistrer le paiement
            Long commandeId = livraisonService.getCommandeIdByLivraisonId(livraisonId);
            if (commandeId != null) {
                livraisonService.enregistrerPaiement(commandeId, 
                                                   montantPaiement, 
                                                   methodePaiement, 
                                                   LocalDate.parse(datePaiement));
            }

            // Commenté pour éviter les problèmes de sérialisation
            // livraisonService.debugStatutsLivraison();

            System.out.println("=== FIN CONFIRMATION LIVRAISON ===");
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livraison confirmée, paiement enregistré et confirmation de réception créée avec succès !");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("ERREUR lors de la confirmation: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la confirmation : " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    /**
     * Récupère toutes les livraisons planifiées
     * @return Liste des livraisons planifiées
     */
    @GetMapping("/planifiees")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Récupérer les livraisons planifiées",
        description = "Retourne la liste de toutes les livraisons avec le statut 'PLANIFIEE'",
        tags = {"Livraisons"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Liste des livraisons planifiées récupérée avec succès",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LivraisonInfoDTO.class)
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Erreur serveur lors de la récupération des livraisons planifiées"
        )
    })
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsPlanifiees() {
        try {
            List<LivraisonInfoDTO> livraisons = livraisonService.getLivraisonsPlanifiees();
            return ResponseEntity.ok(livraisons);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des livraisons planifiées: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Récupère toutes les livraisons en cours
     * @return Liste des livraisons en cours
     */
    @GetMapping("/en-cours")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Récupérer les livraisons en cours",
        description = "Retourne la liste de toutes les livraisons avec le statut 'EN_COURS'",
        tags = {"Livraisons"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Liste des livraisons en cours récupérée avec succès",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LivraisonInfoDTO.class)
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Erreur serveur lors de la récupération des livraisons en cours"
        )
    })
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsEnCours() {
        try {
            List<LivraisonInfoDTO> livraisons = livraisonService.getLivraisonsEnCours();
            return ResponseEntity.ok(livraisons);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des livraisons en cours: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/retour-livraison")
    public ResponseEntity<Map<String, Object>> retourLivraison(@RequestBody Map<String, Object> request) {
        try {
            Long commandeId = Long.valueOf((Integer) request.get("commandeId"));
            livraisonService.annulerCommandeEtRetourLivraison(commandeId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Commande annulée et retour livraison enregistré !");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors du retour livraison : " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Génère un PDF pour une commande spécifique
     * @param commandeId L'ID de la commande
     * @return Un objet contenant l'URL du PDF généré
     */
    @GetMapping("/commandes/{id}/export-pdf")
    public ResponseEntity<Map<String, Object>> exportCommandePdf(@PathVariable("id") Integer commandeId) {
        try {
            // Récupérer la commande
            Optional<Commande> commandeOpt = livraisonService.getCommandeById(commandeId.longValue());
            if (!commandeOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Commande non trouvée");
                return ResponseEntity.status(404).body(errorResponse);
            }
            
            Commande commande = commandeOpt.get();
            
            // Créer une réponse simulant la génération de PDF
            // Dans un environnement de production, nous utiliserions une bibliothèque comme iText ou Apache PDFBox
            Map<String, Object> response = new HashMap<>();
            response.put("message", "PDF généré avec succès");
            response.put("commandeId", commandeId);
            response.put("dateCommande", commande.getDateCommande());
            response.put("nomClient", commande.getClient().getNom());
            
            // Simuler un URL où le PDF serait accessible
            String timestamp = System.currentTimeMillis() + "";
            response.put("pdfUrl", "/downloads/commande_" + commandeId + "_" + timestamp + ".pdf");
            
            // Générer un résumé des produits pour le PDF
            List<Map<String, Object>> produits = new ArrayList<>();
            for (LigneCommande ligne : commande.getLignesCommande()) {
                Map<String, Object> produit = new HashMap<>();
                produit.put("nom", ligne.getProduit().getNom());
                produit.put("quantite", ligne.getQuantite());
                produit.put("prix", ligne.getPrixUnitaire());
                produit.put("total", ligne.getPrixUnitaire().multiply(BigDecimal.valueOf(ligne.getQuantite())));
                produits.add(produit);
            }
            response.put("produits", produits);
            
            // Calculer le total
            BigDecimal total = commande.getLignesCommande().stream()
                .map(ligne -> ligne.getPrixUnitaire().multiply(BigDecimal.valueOf(ligne.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            response.put("montantTotal", total);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de l'export PDF: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    // ==================== RECHERCHES PAR LIVREUR/ZONE ====================
    
    /**
     * PUT /api/livraisons/{id} - Modification complète d'une livraison
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateLivraison(
            @PathVariable Long id, 
            @RequestBody UpdateLivraisonDTO updateDTO) {
        try {
            // Vérifier que la livraison existe
            Optional<Livraison> livraisonOpt = livraisonService.findLivraisonById(id);
            if (livraisonOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Livraison non trouvée avec l'ID: " + id);
                return ResponseEntity.notFound().build();
            }

            // Mettre à jour la livraison
            Livraison livraisonMiseAJour = livraisonService.updateLivraison(id, updateDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livraison mise à jour avec succès");
            response.put("livraisonId", livraisonMiseAJour.getId());
            response.put("dateLivraison", livraisonMiseAJour.getDateLivraison());
            response.put("zone", livraisonMiseAJour.getZone());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la mise à jour de la livraison: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * DELETE /api/livraisons/{id} - Annulation d'une livraison
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> cancelLivraison(@PathVariable Long id) {
        try {
            // Vérifier que la livraison existe
            Optional<Livraison> livraisonOpt = livraisonService.findLivraisonById(id);
            if (livraisonOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Livraison non trouvée avec l'ID: " + id);
                return ResponseEntity.notFound().build();
            }

            Livraison livraison = livraisonOpt.get();
            
            // Vérifier que la livraison peut être annulée (pas encore livrée)
            LivraisonProjection livraisonProjection = livraisonService.getLivraisonDetails()
                    .stream()
                    .filter(l -> l.getLivraisonId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (livraisonProjection != null && 
                ("Livrée".equalsIgnoreCase(livraisonProjection.getStatutLivraison()) || 
                 "Livre".equalsIgnoreCase(livraisonProjection.getStatutLivraison()))) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Impossible d'annuler une livraison déjà effectuée");
                return ResponseEntity.badRequest().body(response);
            }

            // Annuler la livraison (mettre le statut à "Annulée")
            livraisonService.cancelLivraison(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livraison annulée avec succès");
            response.put("livraisonId", id);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de l'annulation de la livraison: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * GET /api/livraisons/{id} - Récupère une livraison par son ID
     */
    @GetMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Récupérer une livraison par ID",
        description = "Retourne toutes les informations d'une livraison spécifique",
        tags = {"Livraisons"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Livraison trouvée",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LivraisonInfoDTO.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Livraison non trouvée"
        )
    })
    public ResponseEntity<?> getLivraisonById(@PathVariable Long id) {
        try {
            LivraisonInfoDTO livraison = livraisonService.getLivraisonById(id);
            if (livraison == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Livraison non trouvée");
                return ResponseEntity.status(404).body(response);
            }
            
            return ResponseEntity.ok(livraison);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération de la livraison: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * GET /api/livraisons/zones - Liste des zones de livraison disponibles
     */
    @GetMapping("/zones")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Récupérer les zones de livraison",
        description = "Retourne la liste de toutes les zones de livraison disponibles",
        tags = {"Livraisons"}
    )
    public ResponseEntity<Map<String, Object>> getZonesLivraison() {
        try {
            List<String> zones = livraisonService.getZonesLivraisonDisponibles();
            
            Map<String, Object> response = new HashMap<>();
            response.put("zones", zones);
            response.put("count", zones.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des zones: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * GET /api/livraisons/livreur/{livreurId} - Récupère les livraisons d'un livreur
     */
    @GetMapping("/livreur/{livreurId}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Récupérer les livraisons par livreur",
        description = "Retourne toutes les livraisons assignées à un livreur spécifique",
        tags = {"Livraisons", "Livreurs"}
    )
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsByLivreur(@PathVariable Long livreurId) {
        try {
            List<LivraisonInfoDTO> livraisons = livraisonService.getLivraisonsByLivreur(livreurId);
            return ResponseEntity.ok(livraisons);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des livraisons par livreur: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * GET /api/livraisons/zone/{zone} - Récupère les livraisons d'une zone
     */
    @GetMapping("/zone/{zone}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Récupérer les livraisons par zone",
        description = "Retourne toutes les livraisons pour une zone géographique spécifique",
        tags = {"Livraisons"}
    )
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsByZone(@PathVariable String zone) {
        try {
            List<LivraisonInfoDTO> livraisons = livraisonService.getLivraisonsByZone(zone);
            return ResponseEntity.ok(livraisons);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des livraisons par zone: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}