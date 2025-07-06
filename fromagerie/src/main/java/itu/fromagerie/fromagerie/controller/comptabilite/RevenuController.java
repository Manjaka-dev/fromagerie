package itu.fromagerie.fromagerie.controller.comptabilite;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.fromagerie.fromagerie.entities.comptabilite.Revenu;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.service.comptabilite.RevenuService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/revenus")
public class RevenuController {

    private final RevenuService revenuService;

    public RevenuController(RevenuService revenuService) {
        this.revenuService = revenuService;
    }

  @GetMapping("/commande/{commandeId}")
    public ResponseEntity<Revenu> getRevenuByCommande(@PathVariable Long commandeId) {
        Commande commande = new Commande();
        commande.setId(commandeId);
        Revenu revenu = revenuService.findByCommande(commande);
        return revenu != null ? ResponseEntity.ok(revenu) : ResponseEntity.notFound().build();
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Revenu>> getRevenusByDateRange(
            @RequestParam String dateDebut, @RequestParam String dateFin) {
        LocalDate startDate = LocalDate.parse(dateDebut);
        LocalDate endDate = LocalDate.parse(dateFin);
        List<Revenu> revenus = revenuService.findByDateRevenuBetween(startDate, endDate);
        return ResponseEntity.ok(revenus);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalRevenusBetween(
            @RequestParam String dateDebut, @RequestParam String dateFin) {
        LocalDate startDate = LocalDate.parse(dateDebut);
        LocalDate endDate = LocalDate.parse(dateFin);
        BigDecimal total = revenuService.getTotalRevenusBetween(startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/date")
    public ResponseEntity<BigDecimal> getTotalRevenusByDate(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        BigDecimal total = revenuService.getTotalRevenusByDate(localDate);
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<Revenu> createRevenu(@RequestBody Revenu revenu) {
        Revenu savedRevenu = revenuService.saveRevenu(revenu);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRevenu);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Revenu> getRevenuById(@PathVariable Long id) {
        Revenu revenu = revenuService.findById(id);
        return revenu != null ? ResponseEntity.ok(revenu) : ResponseEntity.notFound().build();
    }
}