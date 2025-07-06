package itu.fromagerie.fromagerie.controller.production;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.fromagerie.fromagerie.entities.production.TransformationDechet;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import itu.fromagerie.fromagerie.service.production.TransformationDechetService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transformations")
public class TransformationDechetController {

    private final TransformationDechetService transformationDechetService;

    public TransformationDechetController(TransformationDechetService transformationDechetService) {
        this.transformationDechetService = transformationDechetService;
    }

    @GetMapping("/matiere/{matiereId}")
    public ResponseEntity<List<TransformationDechet>> getTransformationsByMatiere(@PathVariable Long matiereId) {
        MatierePremiere matiere = new MatierePremiere();
        matiere.setId(matiereId);
        List<TransformationDechet> transformations = transformationDechetService.findByMatiere(matiere);
        return ResponseEntity.ok(transformations);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TransformationDechet>> getTransformationsByDateRange(
            @RequestParam String dateDebut, @RequestParam String dateFin) {
        LocalDate startDate = LocalDate.parse(dateDebut);
        LocalDate endDate = LocalDate.parse(dateFin);
        List<TransformationDechet> transformations = transformationDechetService.findByDateTransformationBetween(startDate, endDate);
        return ResponseEntity.ok(transformations);
    }

    @GetMapping("/produit-final")
    public ResponseEntity<List<TransformationDechet>> getTransformationsByProduitFinal(@RequestParam String produitFinal) {
        List<TransformationDechet> transformations = transformationDechetService.findByProduitFinalContainingIgnoreCase(produitFinal);
        return ResponseEntity.ok(transformations);
    }

    @GetMapping("/total/{matiereId}")
    public ResponseEntity<Double> getTotalTransformedByMatiere(@PathVariable Long matiereId) {
        MatierePremiere matiere = new MatierePremiere();
        matiere.setId(matiereId);
        Double total = transformationDechetService.getTotalTransformedByMatiere(matiere);
        return ResponseEntity.ok(total);
    }

    @PostMapping
    public ResponseEntity<TransformationDechet> createTransformationDechet(@RequestBody TransformationDechet transformationDechet) {
        TransformationDechet savedTransformation = transformationDechetService.saveTransformationDechet(transformationDechet);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransformation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransformationDechet> getTransformationById(@PathVariable Long id) {
        TransformationDechet transformation = transformationDechetService.findById(id);
        return transformation != null ? ResponseEntity.ok(transformation) : ResponseEntity.notFound().build();
    }
}