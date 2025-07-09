package itu.fromagerie.fromagerie.controller.comptabilite;

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

    public DepenseController(DepenseService depenseService) {
        this.depenseService = depenseService;
    }

    @GetMapping("/categorie")
    public ResponseEntity<List<Depense>> getDepensesByCategorie(@RequestParam String categorie) {
        System.out.println("Recherche des dépenses pour la catégorie: '" + categorie + "'");
        try {
            List<Depense> depenses = depenseService.findByCategorie(categorie);
            System.out.println("Nombre de dépenses trouvées: " + (depenses != null ? depenses.size() : 0));
            return ResponseEntity.ok(depenses);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des dépenses par catégorie: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
        System.out.println("Endpoint appelé: /api/depenses/total/categorie avec paramètre categorie='" + categorie + "'");
        try {
            BigDecimal total = depenseService.getTotalDepensesByCategorie(categorie);
            System.out.println("Total des dépenses pour la catégorie '" + categorie + "': " + total);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du total des dépenses par catégorie: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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