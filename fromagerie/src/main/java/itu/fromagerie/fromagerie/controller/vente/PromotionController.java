package itu.fromagerie.fromagerie.controller.vente;

import itu.fromagerie.fromagerie.entities.vente.Promotion;
import itu.fromagerie.fromagerie.service.vente.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vente")
public class PromotionController {
    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPromotion(@RequestBody Map<String, Object> request) {
        try {
            Long produitId = Long.valueOf((Integer) request.get("produit"));
            String nom = (String) request.get("nom");
            String description = (String) request.get("description");
            BigDecimal reduction = BigDecimal.valueOf((Integer) request.get("reduction"));
            String dateDebut = (String) request.get("dateDebut");
            String dateFin = (String) request.get("dateFin");

            LocalDate date_debut = LocalDate.parse(dateDebut);
            LocalDate date_fin = LocalDate.parse(dateFin);

            promotionService.save(produitId, nom, description, reduction, date_debut, date_fin);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Promotion créé avec succès");
            response.put("nom", nom);
            response.put("description", description);
            response.put("reduction", reduction);
            response.put("dateDebut", date_debut);
            response.put("dateFin", date_fin);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création de promotion : " + e.getMessage());
            return  ResponseEntity.badRequest().body(response);
        }
    }
}
