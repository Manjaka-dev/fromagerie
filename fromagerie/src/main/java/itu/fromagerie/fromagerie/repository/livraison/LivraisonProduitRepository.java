package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.LivraisonProduit;
import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivraisonProduitRepository extends JpaRepository<LivraisonProduit, Long> {
    List<LivraisonProduit> findByLivraison(Livraison livraison);
    List<LivraisonProduit> findByProduit(Produit produit);
    
    // Méthode pour récupérer par ID de livraison
    @Query("SELECT lp FROM LivraisonProduit lp WHERE lp.livraison.id = :livraisonId")
    List<LivraisonProduit> findByLivraisonId(Long livraisonId);
    
    @Query("SELECT lp FROM LivraisonProduit lp WHERE lp.livraison = :livraison")
    List<LivraisonProduit> findProduitsALivrerByLivraison(Livraison livraison);
    
    @Query("SELECT SUM(lp.quantiteALivrer) FROM LivraisonProduit lp WHERE lp.produit = :produit")
    Integer getTotalQuantiteALivrerByProduit(Produit produit);
}
