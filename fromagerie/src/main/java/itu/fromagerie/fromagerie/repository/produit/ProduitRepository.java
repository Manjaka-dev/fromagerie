package itu.fromagerie.fromagerie.repository.produit;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.produit.CategorieProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
