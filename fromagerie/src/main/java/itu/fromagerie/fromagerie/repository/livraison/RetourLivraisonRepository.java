package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.RetourLivraison;
import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetourLivraisonRepository extends JpaRepository<RetourLivraison, Long> {
    List<RetourLivraison> findByLivraison(Livraison livraison);
    List<RetourLivraison> findByProduit(Produit produit);
    
    @Query("SELECT SUM(r.quantiteRetour) FROM RetourLivraison r WHERE r.produit = :produit")
    Integer getTotalRetourByProduit(Produit produit);
}
