package itu.fromagerie.fromagerie.controller.production;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.fromagerie.fromagerie.entities.production.ProductionEffectuee;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.service.production.ProductionEffectueeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/productions")
public class ProductionEffectueeController {

    private final ProductionEffectueeService productionEffectueeService;

    @Autowired
    public ProductionEffectueeController(ProductionEffectueeService productionEffectueeService) {
        this.productionEffectueeService = productionEffectueeService;
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<ProductionEffectuee>> getProductionsByProduit(@PathVariable Long produitId) {
        Produit produit = new Produit();
        produit.setId(produitId);
        List<ProductionEffectuee> productions = productionEffectueeService.findProductionsByProduit(produit);
        return ResponseEntity.ok(productions);
    }

    @GetMapping("/date")
    public ResponseEntity<List<ProductionEffectuee>> getProductionsByDate(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<ProductionEffectuee> productions = productionEffectueeService.findProductionsByDate(localDate);
        return ResponseEntity.ok(productions);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ProductionEffectuee>> getProductionsByDateRange(
            @RequestParam String dateDebut, @RequestParam String dateFin) {
        LocalDate startDate = LocalDate.parse(dateDebut);
        LocalDate endDate = LocalDate.parse(dateFin);
        List<ProductionEffectuee> productions = productionEffectueeService.findProductionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(productions);
    }

    @GetMapping("/total/{produitId}")
    public ResponseEntity<Integer> getTotalProducedByProduit(@PathVariable Long produitId) {
        Produit produit = new Produit();
        produit.setId(produitId);
        Integer total = productionEffectueeService.getTotalProducedByProduit(produit);
        return ResponseEntity.ok(total);
    }
}