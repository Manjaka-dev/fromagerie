package itu.fromagerie.fromagerie.repository.produit;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.produit.CategorieProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByCategorie(CategorieProduit categorie);
    List<Produit> findByNomContainingIgnoreCase(String nom);
    List<Produit> findByPrixVenteBetween(BigDecimal prixMin, BigDecimal prixMax);
    List<Produit> findByDatePeremptionBefore(LocalDate date);
    
    @Query("SELECT p FROM Produit p WHERE p.datePeremption BETWEEN :dateDebut AND :dateFin")
    List<Produit> findProduitsExpirantEntre(LocalDate dateDebut, LocalDate dateFin);
    
    // Recherche de produits par nom avec pagination
    @Query("SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Produit> searchProduitsByNom(@Param("nom") String nom);
    
    // Produits avec stock faible (moins de 10 unités)
    @Query("SELECT p FROM Produit p WHERE p.id IN (SELECT lp.produit.id FROM LotProduit lp GROUP BY lp.produit.id HAVING SUM(lp.quantite) < 10)")
    List<Produit> findProduitsStockFaible();
    
    // Produits en rupture de stock
    @Query("SELECT p FROM Produit p WHERE p.id IN (SELECT lp.produit.id FROM LotProduit lp GROUP BY lp.produit.id HAVING SUM(lp.quantite) = 0)")
    List<Produit> findProduitsEnRupture();
}
