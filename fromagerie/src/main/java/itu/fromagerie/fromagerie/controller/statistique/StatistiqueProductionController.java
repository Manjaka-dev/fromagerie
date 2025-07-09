package itu.fromagerie.fromagerie.controller.statistique;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.statistique.StatistiqueProduction;
import itu.fromagerie.fromagerie.service.statistique.StatistiqueProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistiques/production")
@CrossOrigin(origins = "*")
@Tag(name = "Statistiques de Production", description = "Endpoints pour analyser et suivre les statistiques de production")
public class StatistiqueProductionController {

    @Autowired
    private StatistiqueProductionService statistiqueProductionService;

    // === Endpoints de base ===

    /**
     * Récupère toutes les statistiques de production pour un produit
     */
    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<StatistiqueProduction>> getStatistiquesByProduit(@PathVariable Long produitId) {
        // TODO: Récupérer le produit par ID depuis le service produit
        // Produit produit = produitService.getProduitById(produitId);
        // List<StatistiqueProduction> stats = statistiqueProductionService.getStatistiquesByProduit(produit);
        return ResponseEntity.ok().build();
    }

    /**
     * Récupère les statistiques de production sur une période
     */
    @GetMapping("/periode")
    public ResponseEntity<List<StatistiqueProduction>> getStatistiquesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        List<StatistiqueProduction> stats = statistiqueProductionService.getStatistiquesByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(stats);
    }

    /**
     * Calcule la quantité totale produite pour un produit
     */
    @GetMapping("/produit/{produitId}/total")
    public ResponseEntity<Map<String, Object>> getTotalQuantiteProduiteByProduit(@PathVariable Long produitId) {
        // TODO: Récupérer le produit par ID depuis le service produit
        // Produit produit = produitService.getProduitById(produitId);
        // Integer total = statistiqueProductionService.getTotalQuantiteProduiteByProduit(produit);
        
        Map<String, Object> response = new HashMap<>();
        response.put("produitId", produitId);
        response.put("totalProduit", 0); // À remplacer par la vraie valeur
        return ResponseEntity.ok(response);
    }

    /**
     * Calcule la quantité totale produite sur une période
     */
    @GetMapping("/periode/total")
    public ResponseEntity<Map<String, Object>> getTotalQuantiteProduiteBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        Integer total = statistiqueProductionService.getTotalQuantiteProduiteBetween(dateDebut, dateFin);
        
        Map<String, Object> response = new HashMap<>();
        response.put("dateDebut", dateDebut);
        response.put("dateFin", dateFin);
        response.put("totalProduit", total);
        return ResponseEntity.ok(response);
    }

    // === Endpoints pour les 3 pourcentages généraux ===

    /**
     * Calcule le pourcentage de production journalière
     */
    @GetMapping({"/journaliere/pourcentage", "/pourcentage-journalier"})
    @Operation(
        summary = "Calcul du pourcentage de production journalière",
        description = "Calcule le pourcentage de production par rapport à la capacité journalière pour une date donnée",
        tags = {"Statistiques", "Production"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pourcentage de production calculé avec succès",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Paramètres invalides")
    })
    public ResponseEntity<Map<String, Object>> getPourcentageProductionJournaliere(
            @Parameter(description = "Date pour laquelle calculer le pourcentage", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            
            @Parameter(description = "Capacité journalière de production en unités", required = true)
            @RequestParam Integer capaciteJournaliere) {
        
        Map<String, Object> stats = statistiqueProductionService.getPourcentageProductionJournaliere(date, capaciteJournaliere);
        Map<String, Object> statsFormatees = statistiqueProductionService.formaterStatistiques(stats);
        
        return ResponseEntity.ok(statsFormatees);
    }

    /**
     * Calcule le taux de qualité journalier
     */
    @GetMapping({"/journaliere/qualite", "/qualite-journalier"})
    @Operation(
        summary = "Calcul du taux de qualité journalier",
        description = "Calcule le taux de qualité des produits fabriqués pour une journée spécifique",
        tags = {"Statistiques", "Qualité"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Taux de qualité calculé avec succès",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Date invalide")
    })
    public ResponseEntity<Map<String, Object>> getTauxQualiteJournalier(
            @Parameter(description = "Date pour laquelle calculer le taux de qualité", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        Map<String, Object> stats = statistiqueProductionService.getTauxQualiteJournalier(date);
        Map<String, Object> statsFormatees = statistiqueProductionService.formaterStatistiques(stats);
        
        return ResponseEntity.ok(statsFormatees);
    }

    /**
     * Calcule le taux de perte journalier
     */
    @GetMapping({"/journaliere/perte", "/perte-journalier"})
    @Operation(
        summary = "Calcul du taux de perte journalier",
        description = "Calcule le pourcentage de pertes (déchets, produits défectueux) par rapport à la production journalière",
        tags = {"Statistiques", "Pertes"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Taux de perte calculé avec succès",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Date invalide")
    })
    public ResponseEntity<Map<String, Object>> getTauxPerteJournalier(
            @Parameter(description = "Date pour laquelle calculer le taux de perte", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        Map<String, Object> stats = statistiqueProductionService.getTauxPerteJournalier(date);
        Map<String, Object> statsFormatees = statistiqueProductionService.formaterStatistiques(stats);
        
        return ResponseEntity.ok(statsFormatees);
    }

    /**
     * Calcule les 3 pourcentages généraux pour une journée
     */
    @GetMapping("/journaliere/completes")
    public ResponseEntity<Map<String, Object>> getStatistiquesCompletesJournalieres(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Integer capaciteJournaliere) {
        
        Map<String, Object> stats = statistiqueProductionService.getStatistiquesCompletesJournalieres(date, capaciteJournaliere);
        Map<String, Object> statsFormatees = statistiqueProductionService.formaterStatistiques(stats);
        
        return ResponseEntity.ok(statsFormatees);
    }

    /**
     * Calcule les statistiques sur une période
     */
    @GetMapping("/periode/completes")
    public ResponseEntity<List<Object[]>> getStatistiquesSurPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam Integer capaciteJournaliere) {
        
        List<Object[]> stats = statistiqueProductionService.getStatistiquesSurPeriode(dateDebut, dateFin, capaciteJournaliere);
        return ResponseEntity.ok(stats);
    }

    // === Endpoints pour la capacité journalière ===

    /**
     * Calcule la moyenne des commandes par semaine
     */
    @GetMapping({"/capacite/moyenne-semaine", "/commandes-semaine"})
    @Operation(
        summary = "Moyenne des commandes par semaine",
        description = "Calcule le nombre moyen de commandes reçues par semaine sur une période donnée",
        tags = {"Statistiques", "Commandes"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Moyenne calculée avec succès",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Paramètres de date invalides")
    })
    public ResponseEntity<Map<String, Object>> getMoyenneCommandesParSemaine(
            @Parameter(description = "Date de début de la période d'analyse", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            
            @Parameter(description = "Date de fin de la période d'analyse", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        Double moyenne = statistiqueProductionService.getMoyenneCommandesParSemaine(dateDebut, dateFin);
        
        Map<String, Object> response = new HashMap<>();
        response.put("dateDebut", dateDebut);
        response.put("dateFin", dateFin);
        response.put("moyenneCommandesParSemaine", moyenne);
        return ResponseEntity.ok(response);
    }

    /**
     * Calcule la capacité journalière basée sur les commandes
     */
    @GetMapping({"/capacite/journaliere", "/capacite-journaliere"})
    @Operation(
        summary = "Capacité journalière de production",
        description = "Calcule la capacité journalière optimale de production basée sur l'historique des commandes",
        tags = {"Statistiques", "Capacité"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Capacité journalière calculée avec succès",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Paramètres de date invalides")
    })
    public ResponseEntity<Map<String, Object>> getCapaciteJournaliere(
            @Parameter(description = "Date de début pour l'analyse historique", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            
            @Parameter(description = "Date de fin pour l'analyse historique", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        Integer capacite = statistiqueProductionService.getCapaciteJournaliere(dateDebut, dateFin);
        
        Map<String, Object> response = new HashMap<>();
        response.put("dateDebut", dateDebut);
        response.put("dateFin", dateFin);
        response.put("capaciteJournaliere", capacite);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère les statistiques détaillées par semaine
     */
    @GetMapping("/capacite/statistiques-semaine")
    public ResponseEntity<List<Object[]>> getStatistiquesCommandesParSemaine(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        List<Object[]> stats = statistiqueProductionService.getStatistiquesCommandesParSemaine(dateDebut, dateFin);
        return ResponseEntity.ok(stats);
    }

    // === Endpoints utilitaires ===

    /**
     * Calcule automatiquement la capacité journalière et les statistiques pour aujourd'hui
     */
    @GetMapping("/aujourdhui")
    public ResponseEntity<Map<String, Object>> getStatistiquesAujourdhui() {
        Map<String, Object> stats = statistiqueProductionService.getStatistiquesAujourdhui();
        Map<String, Object> statsFormatees = statistiqueProductionService.formaterStatistiques(stats);
        
        return ResponseEntity.ok(statsFormatees);
    }

    /**
     * Calcule les statistiques pour une période avec capacité automatique
     */
    @GetMapping("/periode/auto-capacite")
    public ResponseEntity<List<Object[]>> getStatistiquesPeriodeAvecCapaciteAuto(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        List<Object[]> stats = statistiqueProductionService.getStatistiquesPeriodeAvecCapaciteAuto(dateDebut, dateFin);
        return ResponseEntity.ok(stats);
    }

    /**
     * Dashboard complet avec toutes les statistiques importantes
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        LocalDate aujourdhui = LocalDate.now();
        LocalDate dateDebut = aujourdhui.minusMonths(3);
        
        Map<String, Object> dashboard = new HashMap<>();
        
        // Statistiques d'aujourd'hui
        Map<String, Object> statsAujourdhui = statistiqueProductionService.getStatistiquesAujourdhui();
        dashboard.put("aujourdhui", statistiqueProductionService.formaterStatistiques(statsAujourdhui));
        
        // Capacité journalière
        Integer capacite = statistiqueProductionService.getCapaciteJournaliere(dateDebut, aujourdhui);
        dashboard.put("capaciteJournaliere", capacite);
        
        // Total produit sur 3 mois
        Integer total3Mois = statistiqueProductionService.getTotalQuantiteProduiteBetween(dateDebut, aujourdhui);
        dashboard.put("totalProduit3Mois", total3Mois);
        
        // Statistiques par semaine
        List<Object[]> statsSemaine = statistiqueProductionService.getStatistiquesCommandesParSemaine(dateDebut, aujourdhui);
        dashboard.put("statistiquesParSemaine", statsSemaine);
        
        return ResponseEntity.ok(dashboard);
    }

    // === Gestion des erreurs ===

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erreur lors du calcul des statistiques");
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
} 