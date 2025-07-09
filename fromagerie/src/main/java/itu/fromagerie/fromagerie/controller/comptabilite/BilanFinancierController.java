package itu.fromagerie.fromagerie.controller.comptabilite;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.fromagerie.fromagerie.entities.comptabilite.BilanFinancier;
import itu.fromagerie.fromagerie.service.comptabilite.BilanFinancierService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bilans")
public class BilanFinancierController {

    private final BilanFinancierService bilanFinancierService;

    public BilanFinancierController(BilanFinancierService bilanFinancierService) {
        this.bilanFinancierService = bilanFinancierService;
    }

    @GetMapping("/periode")
    public ResponseEntity<BilanFinancier> getBilanByPeriode(@RequestParam String periode) {
        LocalDate localDate = LocalDate.parse(periode);
        BilanFinancier bilan = bilanFinancierService.findByPeriode(localDate);
        return bilan != null ? ResponseEntity.ok(bilan) : ResponseEntity.notFound().build();
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<BilanFinancier>> getBilansByPeriodeBetween(
            @RequestParam String dateDebut, @RequestParam String dateFin) {
        LocalDate startDate = LocalDate.parse(dateDebut);
        LocalDate endDate = LocalDate.parse(dateFin);
        List<BilanFinancier> bilans = bilanFinancierService.findByPeriodeBetween(startDate, endDate);
        return ResponseEntity.ok(bilans);
    }

    @GetMapping("/profit")
    public ResponseEntity<List<BilanFinancier>> getBilansByProfitGreater(@RequestParam String profit) {
        BigDecimal profitValue = new BigDecimal(profit);
        List<BilanFinancier> bilans = bilanFinancierService.findByProfitGreaterThan(profitValue);
        return ResponseEntity.ok(bilans);
    }

    @GetMapping
    public ResponseEntity<List<BilanFinancier>> getAllBilansOrderByPeriodeDesc() {
        List<BilanFinancier> bilans = bilanFinancierService.findAllOrderByPeriodeDesc();
        return ResponseEntity.ok(bilans);
    }

    @GetMapping("/total-profit")
    public ResponseEntity<BigDecimal> getTotalProfitBetween(
            @RequestParam String dateDebut, @RequestParam String dateFin) {
        LocalDate startDate = LocalDate.parse(dateDebut);
        LocalDate endDate = LocalDate.parse(dateFin);
        BigDecimal total = bilanFinancierService.getTotalProfitBetween(startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<BilanFinancier> createBilanFinancier(@RequestBody BilanFinancier bilanFinancier) {
        BilanFinancier savedBilan = bilanFinancierService.saveBilanFinancier(bilanFinancier);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBilan);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BilanFinancier> getBilanById(@PathVariable Long id) {
        BilanFinancier bilan = bilanFinancierService.findById(id);
        return bilan != null ? ResponseEntity.ok(bilan) : ResponseEntity.notFound().build();
    }
}