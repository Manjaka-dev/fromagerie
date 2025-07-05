package itu.fromage.repositories;

import itu.fromage.entities.Commande;
import itu.fromage.projection.CommandeLivraisonProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    @Query("""
    SELECT 
        c.id AS commandeId,
        cl.nom AS clientNom,
        p.nom AS produitNom,
        p.poids AS poids, 
        lc.quantite AS quantite,
        c.dateCommande AS dateCommande,
        liv.dateLivraison AS dateLivraison
    FROM LigneCommande lc
    JOIN lc.commande c
    JOIN c.client cl
    JOIN lc.produit p
    LEFT JOIN Livraison liv ON liv.commande.id = c.id
    ORDER BY c.id, p.nom
    """)
    List<CommandeLivraisonProjection> findCommandesLivraisons();

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Integer getLastInsertCommandeId();
}