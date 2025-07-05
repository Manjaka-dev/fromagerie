package itu.fromagerie.fromagerie.controller.livraison;

import itu.fromagerie.fromagerie.entities.livraison.LivraisonProduit;
import itu.fromagerie.fromagerie.repository.livraison.LivraisonProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livraisons-produits")
@CrossOrigin(origins = "*")
public class LivraisonProduitController {
    
    @Autowired
    private LivraisonProduitRepository livraisonProduitRepository;
    
    /**
     * GET /api/livraisons-produits/livraison/{livraisonId} - Récupère les produits d'une livraison
     */
    @GetMapping("/livraison/{livraisonId}")
    public ResponseEntity<List<LivraisonProduit>> getProduitsDelivraison(@PathVariable Long livraisonId) {
        // Cette méthode nécessiterait une requête personnalisée
        return ResponseEntity.ok().build();
    }
    
    /**
     * PUT /api/livraisons-produits/{id}/quantite-livree - Met à jour la quantité livrée
     */
    @PutMapping("/{id}/quantite-livree")
    public ResponseEntity<LivraisonProduit> updateQuantiteLivree(
            @PathVariable Long id, 
            @RequestBody QuantiteLivreeRequest request) {
        
        LivraisonProduit livraisonProduit = livraisonProduitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit de livraison non trouvé"));
        
        livraisonProduit.setQuantiteLivree(request.getQuantiteLivree());
        LivraisonProduit updated = livraisonProduitRepository.save(livraisonProduit);
        
        return ResponseEntity.ok(updated);
    }
    
    // Classe interne pour la mise à jour de quantité
    public static class QuantiteLivreeRequest {
        private Integer quantiteLivree;
        
        public Integer getQuantiteLivree() {
            return quantiteLivree;
        }
        
        public void setQuantiteLivree(Integer quantiteLivree) {
            this.quantiteLivree = quantiteLivree;
        }
    }
}
