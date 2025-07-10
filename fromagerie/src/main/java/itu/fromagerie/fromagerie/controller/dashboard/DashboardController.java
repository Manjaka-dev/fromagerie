package itu.fromagerie.fromagerie.controller.dashboard;

import itu.fromagerie.fromagerie.dto.dashboard.DashboardDTO;
import itu.fromagerie.fromagerie.dto.dashboard.LivraisonDTO;
import itu.fromagerie.fromagerie.dto.dashboard.ProductionDTO;
import itu.fromagerie.fromagerie.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "API pour le tableau de bord principal de la fromagerie")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    @Operation(
        summary = "Tableau de bord principal",
        description = "Récupère toutes les données du tableau de bord avec les indicateurs clés de performance (KPIs)",
        tags = {"Dashboard"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Données du tableau de bord récupérées avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DashboardDTO.class))
        )
    })
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/filtre")
    @Operation(
        summary = "Tableau de bord filtré",
        description = "Récupère les données du tableau de bord filtrées par date, client et/ou produit",
        tags = {"Dashboard", "Filtres"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Données filtrées récupérées avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DashboardDTO.class))
        )
    })
    public DashboardDTO getDashboardFiltered(
        @Parameter(description = "Date de début pour le filtrage (optionnel)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        
        @Parameter(description = "Date de fin pour le filtrage (optionnel)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
        
        @Parameter(description = "ID du client pour filtrer les données (optionnel)")
        @RequestParam(required = false) Long clientId,
        
        @Parameter(description = "ID du produit pour filtrer les données (optionnel)")
        @RequestParam(required = false) Long produitId
    ) {
        return dashboardService.getDashboardFiltered(dateDebut, dateFin, clientId, produitId);
    }
    
    @GetMapping("/stats-globales")
    @Operation(
        summary = "Statistiques globales",
        description = "Récupère les indicateurs de performance globaux de l'entreprise",
        tags = {"Dashboard", "KPIs"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Statistiques globales récupérées avec succès",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<Map<String, Object>> getStatsGlobales() {
        return ResponseEntity.ok(dashboardService.getStatsGlobales());
    }
    
    @GetMapping("/productions-recent")
    @Operation(
        summary = "Productions récentes",
        description = "Récupère les données de production des derniers jours",
        tags = {"Dashboard", "Production"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Données de production récentes récupérées avec succès",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<List<Map<String, Object>>> getProductionsRecent(
        @Parameter(description = "Nombre de jours à inclure (7 par défaut)")
        @RequestParam(defaultValue = "7") Integer jours
    ) {
        return ResponseEntity.ok(dashboardService.getProductionsRecent(jours));
    }
    
    @GetMapping("/livraisons-planifiees")
    public ResponseEntity<List<LivraisonDTO>> getLivraisonsPlanifiees(
        @RequestParam(defaultValue = "7") Integer jours
    ) {
        return ResponseEntity.ok(dashboardService.getLivraisonsPlanifiees(jours));
    }
}