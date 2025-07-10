package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import itu.fromagerie.fromagerie.entities.livraison.StatutLivraisonEnum;
import itu.fromagerie.fromagerie.projection.LivraisonProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    Optional<Livraison> findByCommandeId(Long commandeId);

    Optional<Livraison> findByCommande(Commande commande);
    List<Livraison> findByLivreur(Livreur livreur);
    List<Livraison> findByZone(String zone);
    List<Livraison> findByDateLivraisonBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Livraison> findByStatutLivraison(StatutLivraisonEnum statut);
    
    // Nouvelles méthodes ajoutées
    List<Livraison> findByLivreurId(Long livreurId);
    
    @Query("SELECT DISTINCT l.zone FROM Livraison l WHERE l.zone IS NOT NULL ORDER BY l.zone")
    List<String> findDistinctZones();
    
    @Query("SELECT l FROM Livraison l WHERE l.dateLivraison = :date")
    List<Livraison> findByDateLivraison(LocalDate date);
    // Requête complète pour les informations de livraison
    @Query("""
        SELECT l FROM Livraison l 
        JOIN FETCH l.commande c 
        JOIN FETCH c.client cl 
        JOIN FETCH l.livreur lr 
        LEFT JOIN FETCH l.produitsALivrer pa 
        LEFT JOIN FETCH pa.produit p 
        WHERE l.dateLivraison BETWEEN :dateDebut AND :dateFin
        ORDER BY l.dateLivraison DESC
        """)
    List<Livraison> findLivraisonsCompletes(LocalDate dateDebut, LocalDate dateFin);
    
    @Query(value = """
        SELECT
            l.id AS livraisonId,
            COALESCE(sl.statut, 'Planifiée') AS statutLivraison,
            l.zone AS zone,
            SUM(lc.quantite * p.prix_vente) AS montantTotal,
            liv.id AS livreurId,
            liv.nom AS livreurNom,
            cl.nom AS clientNom,
            cl.telephone AS clientTelephone,
            STRING_AGG(p.nom || ' (x' || lc.quantite || ')', ', ') AS produitsCommandes
        FROM livraison l
        LEFT JOIN commande c ON l.commande_id = c.id
        LEFT JOIN client cl ON c.client_id = cl.id
        LEFT JOIN ligne_commande lc ON lc.commande_id = c.id
        LEFT JOIN produit p ON p.id = lc.produit_id
        LEFT JOIN livreur liv ON liv.id = l.livreur_id
        LEFT JOIN (
            SELECT 
                sl1.livraison_id,
                sl1.statut
            FROM statut_livraison sl1
            INNER JOIN (
                SELECT 
                    livraison_id,
                    MAX(date_statut) as max_date
                FROM statut_livraison
                GROUP BY livraison_id
            ) sl2 ON sl1.livraison_id = sl2.livraison_id 
                   AND sl1.date_statut = sl2.max_date
        ) sl ON sl.livraison_id = l.id
        GROUP BY l.id, sl.statut, l.zone, liv.id, liv.nom, cl.nom, cl.telephone
        ORDER BY l.id DESC
    """, nativeQuery = true)
    List<LivraisonProjection> getLivraisonDetails();

    @Query("""
        SELECT lc.produit.id AS idProduit FROM Produit p 
        JOIN LigneCommande lc ON lc.produit.id = p.id
        JOIN Commande c ON c.id = lc.commande.id
        WHERE c.id = :id
    """)
    List<Long> listIDProduit(@Param("id") Long id);

    /**
     * Trouver toutes les livraisons par zone
     */
    @Query("SELECT l FROM Livraison l WHERE l.zone = :zoneLivraison")
    List<Livraison> findByZoneLivraison(@Param("zoneLivraison") String zoneLivraison);
}