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
    public ResponseEntity<?> createFicheProduction(@RequestBody itu.fromagerie.fromagerie.dto.FicheProductionDTO ficheDTO) {
        try {
            // Logique pour transformer le DTO en entités et les sauvegarder
            // Cela sera implémenté dans le service
            FicheProduction savedFiche = ficheProductionService.creerFicheProductionDepuisDTO(ficheDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFiche);
        } catch (Exception e) {
            // Log l'erreur et retourne un message d'erreur approprié
            System.err.println("Erreur lors de la création de la fiche de production: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Erreur lors de la création de la fiche: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FicheProduction> getFicheById(@PathVariable Long id) {
        FicheProduction fiche = ficheProductionService.findById(id);
        return fiche != null ? ResponseEntity.ok(fiche) : ResponseEntity.notFound().build();
    }
}