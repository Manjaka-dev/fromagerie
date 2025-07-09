package itu.fromagerie.fromagerie.controller.dashboard;

import itu.fromagerie.fromagerie.dto.dashboard.DashboardDTO;
import itu.fromagerie.fromagerie.dto.dashboard.LivraisonDTO;
import itu.fromagerie.fromagerie.dto.dashboard.ProductionDTO;
import itu.fromagerie.fromagerie.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/filtre")
    public DashboardDTO getDashboardFiltered(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
        @RequestParam(required = false) Long clientId,
        @RequestParam(required = false) Long produitId
    ) {
        return dashboardService.getDashboardFiltered(dateDebut, dateFin, clientId, produitId);
    }
    
    @GetMapping("/stats-globales")
    public ResponseEntity<Map<String, Object>> getStatsGlobales() {
        return ResponseEntity.ok(dashboardService.getStatsGlobales());
    }
    
    @GetMapping("/productions-recent")
    public ResponseEntity<List<Map<String, Object>>> getProductionsRecent(
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