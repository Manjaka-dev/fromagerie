package itu.fromagerie.fromagerie.controller.livraison;

import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import itu.fromagerie.fromagerie.repository.livraison.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livreurs")
@CrossOrigin(origins = "*")
public class LivreurController {
    
    @Autowired
    private LivreurRepository livreurRepository;
    
    /**
     * GET /api/livreurs - Récupère tous les livreurs
     */
    @GetMapping
    public ResponseEntity<List<Livreur>> getAllLivreurs() {
        List<Livreur> livreurs = livreurRepository.findAll();
        return ResponseEntity.ok(livreurs);
    }
    
    /**
     * GET /api/livreurs/{id} - Récupère un livreur par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Livreur> getLivreurById(@PathVariable Long id) {
        Optional<Livreur> livreur = livreurRepository.findById(id);
        return livreur.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/livreurs - Crée un nouveau livreur
     */
    @PostMapping
    public ResponseEntity<Livreur> createLivreur(@RequestBody Livreur livreur) {
        Livreur savedLivreur = livreurRepository.save(livreur);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLivreur);
    }
    
    /**
     * PUT /api/livreurs/{id} - Met à jour un livreur
     */
    @PutMapping("/{id}")
    public ResponseEntity<Livreur> updateLivreur(@PathVariable Long id, @RequestBody Livreur livreur) {
        if (!livreurRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        livreur.setId(id);
        Livreur updatedLivreur = livreurRepository.save(livreur);
        return ResponseEntity.ok(updatedLivreur);
    }
    
    /**
     * DELETE /api/livreurs/{id} - Supprime un livreur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        if (!livreurRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        livreurRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * GET /api/livreurs/search - Recherche par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<Livreur>> searchLivreurs(@RequestParam String nom) {
        List<Livreur> livreurs = livreurRepository.findByNomContainingIgnoreCase(nom);
        return ResponseEntity.ok(livreurs);
    }
}
