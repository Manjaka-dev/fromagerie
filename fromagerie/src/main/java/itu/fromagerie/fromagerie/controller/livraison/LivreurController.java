package itu.fromagerie.fromagerie.controller.livraison;

import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import itu.fromagerie.fromagerie.service.livraison.LivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/livreurs")

@Tag(name = "Livreurs", description = "API pour gérer les livreurs")
public class LivreurController {
    private final LivraisonService livraisonService;

    @Autowired
    public LivreurController(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les livreurs", description = "Renvoie la liste complète des livreurs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des livreurs récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Livreur.class)))
    })
    public ResponseEntity<List<Livreur>> getAllLivreurs() {
        List<Livreur> livreurs = livraisonService.listeLivreur();
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un livreur par ID", description = "Renvoie un livreur spécifique par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livreur trouvé"),
        @ApiResponse(responseCode = "404", description = "Livreur non trouvé")
    })
    public ResponseEntity<Livreur> getLivreurById(
            @Parameter(description = "ID du livreur à récupérer") @PathVariable Long id) {
        List<Livreur> livreurs = livraisonService.listeLivreur();
        return livreurs.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau livreur", description = "Enregistre un nouveau livreur dans le système")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livreur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides ou erreur lors de la création")
    })
    public ResponseEntity<Map<String, Object>> createLivreur(
            @Parameter(description = "Données du livreur à créer") @RequestBody Livreur livreur) {
        try {
            // Enregistrement du livreur avec la méthode saveLivreur du service
            Livreur savedLivreur = livraisonService.saveLivreur(livreur);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livreur créé avec succès");
            response.put("livreur", savedLivreur);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création du livreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un livreur", description = "Met à jour les informations d'un livreur existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livreur mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides ou erreur lors de la mise à jour"),
        @ApiResponse(responseCode = "404", description = "Livreur non trouvé")
    })
    public ResponseEntity<Map<String, Object>> updateLivreur(
            @Parameter(description = "ID du livreur à mettre à jour") @PathVariable Long id, 
            @Parameter(description = "Nouvelles données du livreur") @RequestBody Livreur livreur) {
        try {
            livreur.setId(id);
            Livreur updatedLivreur = livraisonService.updateLivreur(livreur);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livreur mis à jour avec succès");
            response.put("livreur", updatedLivreur);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la mise à jour du livreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livreur", description = "Supprime un livreur par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livreur supprimé avec succès"),
        @ApiResponse(responseCode = "400", description = "Erreur lors de la suppression"),
        @ApiResponse(responseCode = "404", description = "Livreur non trouvé")
    })
    public ResponseEntity<Map<String, Object>> deleteLivreur(
            @Parameter(description = "ID du livreur à supprimer") @PathVariable Long id) {
        try {
            boolean deleted = livraisonService.deleteLivreur(id);
            
            if (!deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Livreur non trouvé avec l'ID: " + id);
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livreur supprimé avec succès");
            response.put("id", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la suppression du livreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/fix-sequence")
    @Operation(summary = "Corriger la séquence d'ID des livreurs", description = "Résout les problèmes de séquence PostgreSQL pour la table livreur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Séquence corrigée avec succès"),
        @ApiResponse(responseCode = "500", description = "Erreur lors de la correction de la séquence")
    })
    public ResponseEntity<Map<String, Object>> fixSequence() {
        try {
            livraisonService.fixLivreurSequence();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Séquence des livreurs corrigée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la correction de la séquence: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}