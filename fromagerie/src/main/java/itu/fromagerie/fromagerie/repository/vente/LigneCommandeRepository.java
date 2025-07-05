package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.LigneCommande;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    List<LigneCommande> findByCommande(Commande commande);
    List<LigneCommande> findByProduit(Produit produit);
    
    @Query("SELECT SUM(l.quantite) FROM LigneCommande l WHERE l.produit = :produit")
    Integer getTotalQuantiteByProduit(Produit produit);
}
