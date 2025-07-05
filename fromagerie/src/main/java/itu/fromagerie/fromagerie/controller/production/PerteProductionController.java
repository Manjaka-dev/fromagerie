package itu.fromagerie.fromagerie.controller.production;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.fromagerie.fromagerie.entities.production.PerteProduction;
import itu.fromagerie.fromagerie.entities.production.ProductionEffectuee;
import itu.fromagerie.fromagerie.service.production.PerteProductionService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pertes")
public class PerteProductionController {

    private final PerteProductionService perteProductionService;

    @Autowired
    public PerteProductionController(PerteProductionService perteProductionService) {
        this.perteProductionService = perteProductionService;
    }

    @GetMapping("/production/{productionId}")
    public ResponseEntity<List<PerteProduction>> getPertesByProduction(@PathVariable Long productionId) {
        ProductionEffectuee production = new ProductionEffectuee();
        production.setId(productionId);
        List<PerteProduction> pertes = perteProductionService.findPerteByProduction(production);
        return ResponseEntity.ok(pertes);
    }

    @GetMapping("/taux")
    public ResponseEntity<List<PerteProduction>> getPertesByTauxGreater(@RequestParam String seuil) {
        BigDecimal seuilValue = new BigDecimal(seuil);
        List<PerteProduction> pertes = perteProductionService.findByTauxPerteGreater(seuilValue);
        return ResponseEntity.ok(pertes);
    }

    @GetMapping("/average-taux")
    public ResponseEntity<BigDecimal> getAverageTauxPerte() {
        BigDecimal average = perteProductionService.getAverageTauxPerte();
        return ResponseEntity.ok(average);
    }
}