package itu.fromagerie.fromagerie.controller.production;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.fromagerie.fromagerie.entities.production.FicheProduction;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import itu.fromagerie.fromagerie.service.production.FicheProductionService;

import java.util.List;

@RestController
@RequestMapping("/api/fiches")
public class FicheProductionController {

    private final FicheProductionService ficheProductionService;

    public FicheProductionController(FicheProductionService ficheProductionService) {
        this.ficheProductionService = ficheProductionService;
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<FicheProduction>> getFichesByProduit(@PathVariable Long produitId) {
        Produit produit = new Produit();
        produit.setId(produitId);
        List<FicheProduction> fiches = ficheProductionService.findByProduit(produit);
        return ResponseEntity.ok(fiches);
    }

    @GetMapping("/matiere/{matiereId}")
    public ResponseEntity<List<FicheProduction>> getFichesByMatiere(@PathVariable Long matiereId) {
        MatierePremiere matiere = new MatierePremiere();
        matiere.setId(matiereId);
        List<FicheProduction> fiches = ficheProductionService.findByMatiere(matiere);
        return ResponseEntity.ok(fiches);
    }

    @PostMapping
    public ResponseEntity<FicheProduction> createFicheProduction(@RequestBody FicheProduction ficheProduction) {
        FicheProduction savedFiche = ficheProductionService.saveFicheProduction(ficheProduction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFiche);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FicheProduction> getFicheById(@PathVariable Long id) {
        FicheProduction fiche = ficheProductionService.findById(id);
        return fiche != null ? ResponseEntity.ok(fiche) : ResponseEntity.notFound().build();
    }
}