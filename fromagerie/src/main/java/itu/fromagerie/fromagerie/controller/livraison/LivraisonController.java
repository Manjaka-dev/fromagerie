package itu.fromagerie.fromagerie.controller.livraison;

import itu.fromagerie.fromagerie.dto.livraison.*;
import itu.fromagerie.fromagerie.entities.livraison.StatutLivraisonEnum;
import itu.fromagerie.fromagerie.service.livraison.LivraisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livraisons")
@CrossOrigin(origins = "*")
@Tag(name = "Livraisons", description = "API de gestion des livraisons de la fromagerie")
public class LivraisonController {
    
    @Autowired
    private LivraisonService livraisonService;
    
    // ==================== CRUD OPERATIONS ====================
    
    @Operation(
        summary = "Créer une nouvelle livraison",
        description = "Crée une nouvelle livraison avec les produits à livrer et assigne un livreur"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Livraison créée avec succès",
                    content = @Content(schema = @Schema(implementation = LivraisonInfoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Données de livraison invalides"),
        @ApiResponse(responseCode = "404", description = "Commande ou livreur non trouvé")
    })
    @PostMapping
    public ResponseEntity<LivraisonInfoDTO> createLivraison(
        @Parameter(description = "Données de la livraison à créer", required = true)
        @RequestBody CreateLivraisonDTO createDTO) {
        try {
            LivraisonInfoDTO livraison = livraisonService.createLivraison(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(livraison);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(
        summary = "Récupérer une livraison par ID",
        description = "Récupère les détails complets d'une livraison spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livraison trouvée",
                    content = @Content(schema = @Schema(implementation = LivraisonInfoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Livraison non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LivraisonInfoDTO> getLivraisonById(
        @Parameter(description = "ID de la livraison", required = true)
        @PathVariable Long id) {
        Optional<LivraisonInfoDTO> livraison = livraisonService.getLivraisonById(id);
        return livraison.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(
        summary = "Lister toutes les livraisons",
        description = "Récupère la liste de toutes les livraisons avec leurs détails"
    )
    @ApiResponse(responseCode = "200", description = "Liste des livraisons récupérée avec succès")
    @GetMapping
    public ResponseEntity<List<LivraisonInfoDTO>> getAllLivraisons() {
        List<LivraisonInfoDTO> livraisons = livraisonService.getAllLivraisons();
        return ResponseEntity.ok(livraisons);
    }
    
    /**
     * PUT /api/livraisons/{id} - Met à jour une livraison
     */
    @PutMapping("/{id}")
    public ResponseEntity<LivraisonInfoDTO> updateLivraison(
            @PathVariable Long id, 
            @RequestBody UpdateLivraisonDTO updateDTO) {
        try {
            LivraisonInfoDTO livraison = livraisonService.updateLivraison(id, updateDTO);
            return ResponseEntity.ok(livraison);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/livraisons/{id} - Supprime une livraison
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        try {
            livraisonService.deleteLivraison(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ==================== RECHERCHES SPÉCIALISÉES ====================
    
    /**
     * GET /api/livraisons/info - Récupère les informations complètes des livraisons
     * Paramètres optionnels : dateDebut, dateFin
     */
    @GetMapping("/info")
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsInfo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        // Si pas de dates fournies, prendre les 30 derniers jours
        if (dateDebut == null && dateFin == null) {
            dateFin = LocalDate.now();
            dateDebut = dateFin.minusDays(30);
        } else if (dateDebut == null) {
            dateDebut = dateFin.minusDays(30);
        } else if (dateFin == null) {
            dateFin = dateDebut.plusDays(30);
        }
        
        List<LivraisonInfoDTO> livraisons = livraisonService.getLivraisonsInfo(dateDebut, dateFin);
        return ResponseEntity.ok(livraisons);
    }
    
    /**
     * GET /api/livraisons/date/{date} - Récupère les livraisons pour une date donnée
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<LivraisonInfoDTO> livraisons = livraisonService.getLivraisonsInfo(date, date);
        return ResponseEntity.ok(livraisons);
    }
    
    /**
     * GET /api/livraisons/periode - Récupère les livraisons entre deux dates
     */
    @GetMapping("/periode")
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        List<LivraisonInfoDTO> livraisons = livraisonService.getLivraisonsInfo(dateDebut, dateFin);
        return ResponseEntity.ok(livraisons);
    }
    
    /**
     * GET /api/livraisons/statut/{statut} - Récupère les livraisons par statut
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsByStatut(
            @PathVariable StatutLivraisonEnum statut) {
        
        List<LivraisonInfoDTO> livraisons = livraisonService.getLivraisonsByStatut(statut);
        return ResponseEntity.ok(livraisons);
    }
    
    // ==================== GESTION DES STATUTS ====================
    
    /**
     * PUT /api/livraisons/{id}/statut - Met à jour le statut d'une livraison
     */
    @PutMapping("/{id}/statut")
    public ResponseEntity<Void> updateStatutLivraison(
            @PathVariable Long id,
            @RequestBody StatutUpdateDTO request) {
        
        try {
            livraisonService.updateStatutLivraison(id, request.getStatut(), request.getCommentaire());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/livraisons/statuts - Récupère tous les statuts possibles
     */
    @GetMapping("/statuts")
    public ResponseEntity<StatutLivraisonEnum[]> getStatuts() {
        return ResponseEntity.ok(StatutLivraisonEnum.values());
    }
    
    // ==================== RECHERCHES PAR LIVREUR/ZONE ====================
    
    /**
     * GET /api/livraisons/livreur/{livreurId} - Récupère les livraisons d'un livreur
     */
    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsByLivreur(@PathVariable Long livreurId) {
        // Cette méthode nécessiterait d'être ajoutée au service
        return ResponseEntity.ok().build();
    }
    
    /**
     * GET /api/livraisons/zone/{zone} - Récupère les livraisons d'une zone
     */
    @GetMapping("/zone/{zone}")
    public ResponseEntity<List<LivraisonInfoDTO>> getLivraisonsByZone(@PathVariable String zone) {
        // Cette méthode nécessiterait d'être ajoutée au service
        return ResponseEntity.ok().build();
    }
}
