package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.LigneCommande;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    List<LigneCommande> findByCommande(Commande commande);
    List<LigneCommande> findByProduit(Produit produit);
    
    @Query("SELECT SUM(l.quantite) FROM LigneCommande l WHERE l.produit = :produit")
    Integer getTotalQuantiteByProduit(Produit produit);
    
    @Query("SELECT l FROM LigneCommande l WHERE l.commande.id = :commandeId")
    List<LigneCommande> findByCommandeId(@Param("commandeId") Long commandeId);
    
    // Produits les plus vendus avec quantités
    @Query("SELECT l.produit.id as produitId, l.produit.nom as nom, " +
           "SUM(l.quantite) as quantiteVendue, " +
           "SUM(l.quantite * l.prixUnitaire) as chiffreAffaires " +
           "FROM LigneCommande l " +
           "GROUP BY l.produit.id, l.produit.nom " +
           "ORDER BY quantiteVendue DESC")
    List<Object[]> getProduitsPlusVendus();
    
    // Chiffre d'affaires total
    @Query("SELECT SUM(l.quantite * l.prixUnitaire) FROM LigneCommande l")
    BigDecimal getChiffreAffairesTotal();
}
