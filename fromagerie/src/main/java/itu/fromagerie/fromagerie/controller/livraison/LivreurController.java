package itu.fromagerie.fromagerie.controller.livraison;

import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import itu.fromagerie.fromagerie.service.livraison.LivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/livreurs")
public class LivreurController {
    private final LivraisonService livraisonService;

    @Autowired
    public LivreurController(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    @GetMapping
    public ResponseEntity<List<Livreur>> getAllLivreurs() {
        List<Livreur> livreurs = livraisonService.listeLivreur();
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livreur> getLivreurById(@PathVariable Long id) {
        List<Livreur> livreurs = livraisonService.listeLivreur();
        return livreurs.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createLivreur(@RequestBody Livreur livreur) {
        try {
            // Note: Ajouter une méthode save dans LivraisonService si nécessaire
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livreur créé avec succès");
            response.put("livreur", livreur);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la création du livreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateLivreur(@PathVariable Long id, @RequestBody Livreur livreur) {
        try {
            livreur.setId(id);
            // Note: Ajouter une méthode save dans LivraisonService si nécessaire
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livreur mis à jour avec succès");
            response.put("livreur", livreur);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la mise à jour du livreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteLivreur(@PathVariable Long id) {
        try {
            // Note: Ajouter une méthode deleteById dans LivraisonService si nécessaire
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livreur supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Erreur lors de la suppression du livreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 