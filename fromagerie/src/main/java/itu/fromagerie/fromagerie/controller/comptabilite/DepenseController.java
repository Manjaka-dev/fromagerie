package itu.fromagerie.fromagerie.controller.comptabilite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.fromagerie.fromagerie.entities.comptabilite.Depense;
import itu.fromagerie.fromagerie.service.comptabilite.DepenseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/depenses")
public class DepenseController {

    private final DepenseService depenseService;

    @Autowired
    public DepenseController(DepenseService depenseService) {
        this.depenseService = depenseService;
    }

    @GetMapping("/categorie")
    public ResponseEntity<List<Depense>> getDepensesByCategorie(@RequestParam String categorie) {
        List<Depense> depenses = depenseService.findByCategorie(categorie);
        return ResponseEntity.ok(depenses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Depense>> getDepensesByDateRange(
            @RequestParam String dateDebut, @RequestParam String dateFin) {
        LocalDate startDate = LocalDate.parse(dateDebut);
        LocalDate endDate = LocalDate.parse(dateFin);
        List<Depense> depenses = depenseService.findByDateDepenseBetween(startDate, endDate);
        return ResponseEntity.ok(depenses);
    }

    @GetMapping("/libelle")
    public ResponseEntity<List<Depense>> getDepensesByLibelle(@RequestParam String libelle) {
        List<Depense> depenses = depenseService.findByLibelleContainingIgnoreCase(libelle);
        return ResponseEntity.ok(depenses);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalDepensesBetween(
            @RequestParam String dateDebut, @RequestParam String dateFin) {
        LocalDate startDate = LocalDate.parse(dateDebut);
        LocalDate endDate = LocalDate.parse(dateFin);
        BigDecimal total = depenseService.getTotalDepensesBetween(startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/categorie")
    public ResponseEntity<BigDecimal> getTotalDepensesByCategorie(@RequestParam String categorie) {
        BigDecimal total = depenseService.getTotalDepensesByCategorie(categorie);
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<Depense> createDepense(@RequestBody Depense depense) {
        Depense savedDepense = depenseService.saveDepense(depense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepense);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Depense> getDepenseById(@PathVariable Long id) {
        Depense depense = depenseService.findById(id);
        return depense != null ? ResponseEntity.ok(depense) : ResponseEntity.notFound().build();
    }
}