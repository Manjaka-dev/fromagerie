package itu.fromagerie.fromagerie.controller.stock;

import itu.fromagerie.fromagerie.dto.stock.*;
import itu.fromagerie.fromagerie.dto.stock.InventaireDTO;
import itu.fromagerie.fromagerie.dto.stock.ValorisationStockDTO;
import itu.fromagerie.fromagerie.dto.stock.AlerteStockDTO;
import itu.fromagerie.fromagerie.service.stock.StockMatiereService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "*")
@Tag(name = "Stock Matières Premières", description = "API de gestion du stock des matières premières")
public class StockMatiereController {

    @Autowired
    private StockMatiereService stockMatiereService;

    // ==================== GESTION MATIERES PREMIERES ====================

    @PostMapping("/matieres-premieres")
    @Operation(summary = "Créer une nouvelle matière première", 
               description = "Crée une nouvelle matière première avec un stock initial")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Matière première créée avec succès",
                    content = @Content(schema = @Schema(implementation = MatierePremiereDTO.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<MatierePremiereDTO> createMatierePremiere(
            @RequestBody CreateMatierePremiereDTO createDTO) {
        try {
            MatierePremiereDTO matiere = stockMatiereService.createMatierePremiere(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(matiere);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/matieres-premieres")
    @Operation(summary = "Lister toutes les matières premières", 
               description = "Récupère la liste de toutes les matières premières avec leur stock")
    public ResponseEntity<List<MatierePremiereDTO>> getAllMatieresPremiere() {
        List<MatierePremiereDTO> matieres = stockMatiereService.getAllMatieresPremieres();
        return ResponseEntity.ok(matieres);
    }

    @GetMapping("/matieres-premieres/{id}")
    @Operation(summary = "Obtenir une matière première par son ID")
    public ResponseEntity<MatierePremiereDTO> getMatierePremiere(
            @Parameter(description = "ID de la matière première") @PathVariable Long id) {
        try {
            return stockMatiereService.getMatierePremiereById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== GESTION MOUVEMENTS STOCK ====================

    @PostMapping("/mouvements")
    @Operation(summary = "Créer un mouvement de stock", 
               description = "Enregistre un mouvement de stock (entrée, sortie, ajustement)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mouvement créé avec succès",
                    content = @Content(schema = @Schema(implementation = MouvementStockDTO.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides ou stock insuffisant")
    })
    public ResponseEntity<MouvementStockDTO> createMouvement(
            @RequestBody CreateMouvementStockDTO createDTO) {
        try {
            MouvementStockDTO mouvement = stockMatiereService.createMouvement(
                createDTO.getMatiereId(),
                createDTO.getTypeMouvement(),
                createDTO.getQuantite(),
                createDTO.getCommentaire()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(mouvement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/mouvements")
    @Operation(summary = "Obtenir l'historique des mouvements de stock")
    public ResponseEntity<List<MouvementStockDTO>> getHistoriqueMouvements(
            @Parameter(description = "ID de la matière première (optionnel)") 
            @RequestParam(required = false) Long matiereId,
            @Parameter(description = "Date de début (format YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin (format YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        List<MouvementStockDTO> mouvements = stockMatiereService.getMouvements(
            matiereId, dateDebut, dateFin);
        return ResponseEntity.ok(mouvements);
    }

    // ==================== GESTION DECHETS ====================

    @PostMapping("/dechets")
    @Operation(summary = "Déclarer un déchet", 
               description = "Déclare une perte de matière première comme déchet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Déchet déclaré avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides ou stock insuffisant")
    })
    public ResponseEntity<DechetDTO> declarerDechet(
            @Parameter(description = "ID de la matière première") @RequestParam Long matiereId,
            @Parameter(description = "Quantité gaspillée") @RequestParam BigDecimal quantite,
            @Parameter(description = "Raison du déchet") @RequestParam String raison,
            @Parameter(description = "Commentaire supplémentaire") @RequestParam(required = false) String commentaire) {
        try {
            DechetDTO dechet = stockMatiereService.declarerDechet(matiereId, quantite, raison, commentaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(dechet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dechets")
    @Operation(summary = "Obtenir la liste des déchets", 
               description = "Récupère la liste des déchets sur une période donnée")
    public ResponseEntity<List<DechetDTO>> getDechets(
            @Parameter(description = "Date de début (format YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin (format YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        if (dateDebut == null) dateDebut = LocalDate.now().minusMonths(1);
        if (dateFin == null) dateFin = LocalDate.now();
        
        List<DechetDTO> dechets = stockMatiereService.getDechets(dateDebut, dateFin);
        return ResponseEntity.ok(dechets);
    }

    // ==================== ALERTES PEREMPTION ====================

    @PostMapping("/alertes-peremption")
    @Operation(summary = "Créer une alerte de péremption", 
               description = "Crée une alerte pour surveiller la péremption d'une matière première")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Alerte créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<AlertePeremptionDTO> createAlertePeremption(
            @Parameter(description = "ID de la matière première") @RequestParam Long matiereId,
            @Parameter(description = "Date de péremption") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datePeremption,
            @Parameter(description = "Seuil d'alerte en jours") @RequestParam Integer seuilAlerte) {
        try {
            AlertePeremptionDTO alerte = stockMatiereService.createAlertePeremption(
                matiereId, datePeremption, seuilAlerte);
            return ResponseEntity.status(HttpStatus.CREATED).body(alerte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/alertes-peremption")
    @Operation(summary = "Obtenir les alertes de péremption", 
               description = "Récupère les alertes de péremption actives ou à venir")
    public ResponseEntity<List<AlertePeremptionDTO>> getAlertesPeremption() {
        List<AlertePeremptionDTO> alertes = stockMatiereService.getAlertesActives();
        return ResponseEntity.ok(alertes);
    }

    // ==================== RESUMÉ ET STATISTIQUES ====================

    @GetMapping("/resume")
    @Operation(summary = "Obtenir le résumé du stock", 
               description = "Récupère un résumé complet de tous les stocks de matières premières")
    public ResponseEntity<List<StockSummaryDTO>> getResumeStock() {
        List<StockSummaryDTO> resume = stockMatiereService.getStockSummary();
        return ResponseEntity.ok(resume);
    }

    // ==================== ESTIMATION PRODUCTION ====================

    @GetMapping("/estimation-production")
    @Operation(summary = "Estimer la capacité de production", 
               description = "Calcule la capacité de production possible en fonction du stock disponible")
    public ResponseEntity<EstimationProductionDTO> estimerCapaciteProduction(
            @Parameter(description = "ID du produit") @RequestParam Long produitId,
            @Parameter(description = "Quantité souhaitée") @RequestParam Integer quantiteSouhaitee) {
        try {
            EstimationProductionDTO estimation = stockMatiereService.estimerProduction(produitId, quantiteSouhaitee);
            return ResponseEntity.ok(estimation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/simulation-production")
    @Operation(summary = "Créer une simulation de production", 
               description = "Simule la production d'un produit et son impact sur le stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Simulation créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Stock insuffisant pour la production")
    })
    public ResponseEntity<EstimationProductionDTO> simulerProduction(
            @Parameter(description = "ID du produit à produire") @RequestParam Long produitId,
            @Parameter(description = "Quantité à produire") @RequestParam Integer quantite) {
        try {
            EstimationProductionDTO simulation = stockMatiereService.estimerProduction(produitId, quantite);
            return ResponseEntity.status(HttpStatus.CREATED).body(simulation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== ENDPOINTS UTILITAIRES ====================

    @GetMapping("/stocks-faibles")
    @Operation(summary = "Obtenir les stocks faibles", 
               description = "Récupère uniquement les stocks ayant un niveau faible ou critique")
    public ResponseEntity<List<StockSummaryDTO>> getStocksFaibles() {
        List<StockSummaryDTO> stocksFaibles = stockMatiereService.getStocksFaibles();
        return ResponseEntity.ok(stocksFaibles);
    }

    @GetMapping("/statuts")
    @Operation(summary = "Obtenir les statuts de stock possibles", 
               description = "Récupère la liste des statuts de stock possibles")
    public ResponseEntity<String[]> getStatutsStock() {
        String[] statuts = {"VIDE", "CRITIQUE", "FAIBLE", "NORMAL"};
        return ResponseEntity.ok(statuts);
    }

    @GetMapping("/types-mouvement")
    @Operation(summary = "Obtenir les types de mouvement possibles", 
               description = "Récupère la liste des types de mouvement possibles")
    public ResponseEntity<String[]> getTypesMouvement() {
        String[] types = stockMatiereService.getTypesMouvement();
        return ResponseEntity.ok(types);
    }
    
    // ==================== NOUVEAUX ENDPOINTS ====================
    
    @PutMapping("/matieres-premieres/{id}")
    @Operation(summary = "Mettre à jour une matière première", 
               description = "Met à jour les informations d'une matière première existante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matière première mise à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Matière première non trouvée"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<MatierePremiereDTO> updateMatierePremiere(
            @Parameter(description = "ID de la matière première") @PathVariable Long id,
            @RequestBody MatierePremiereDTO updateDTO) {
        try {
            MatierePremiereDTO matiere = stockMatiereService.updateMatierePremiere(id, updateDTO);
            return ResponseEntity.ok(matiere);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/matieres-premieres/{id}")
    @Operation(summary = "Supprimer une matière première", 
               description = "Supprime une matière première si elle n'a pas de mouvements de stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matière première supprimée avec succès"),
        @ApiResponse(responseCode = "400", description = "Impossible de supprimer (mouvements existants)")
    })
    public ResponseEntity<Void> deleteMatierePremiere(
            @Parameter(description = "ID de la matière première") @PathVariable Long id) {
        try {
            stockMatiereService.deleteMatierePremiere(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/alerts")
    @Operation(summary = "Obtenir toutes les alertes de stock", 
               description = "Récupère toutes les alertes : stock faible, péremption, rupture")
    public ResponseEntity<AlerteStockDTO> getAlertesStock() {
        try {
            AlerteStockDTO alertes = stockMatiereService.getAlertesStock();
            return ResponseEntity.ok(alertes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/inventaire")
    @Operation(summary = "Lancer un inventaire", 
               description = "Crée un nouvel inventaire avec les quantités théoriques actuelles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Inventaire créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Erreur lors de la création")
    })
    public ResponseEntity<InventaireDTO> lancerInventaire(
            @Parameter(description = "Commentaire pour l'inventaire") 
            @RequestParam(required = false) String commentaire) {
        try {
            InventaireDTO inventaire = stockMatiereService.lancerInventaire(commentaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(inventaire);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/valorisation")
    @Operation(summary = "Calculer la valorisation du stock", 
               description = "Calcule la valeur totale du stock de matières premières")
    public ResponseEntity<ValorisationStockDTO> getValorisationStock() {
        try {
            ValorisationStockDTO valorisation = stockMatiereService.calculerValorisationStock();
            return ResponseEntity.ok(valorisation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
