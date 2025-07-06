package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.vente.Client;
import itu.fromagerie.fromagerie.projection.CommandeLivraisonProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClient(Client client);
    List<Commande> findByStatut(String statut);
    List<Commande> findByDateCommandeBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT c FROM Commande c WHERE c.dateCommande = :date")
    List<Commande> findByDateCommande(LocalDate date);
    
    @Query("SELECT COUNT(c) FROM Commande c WHERE c.statut = :statut")
    Long countByStatut(String statut);
    
    @Query("SELECT MAX(c.id) FROM Commande c")
    Long getLastInsertCommandeId();
    
    @Query("SELECT c.id as commandeId, cl.nom as clientNom, p.nom as produitNom, " +
           "lc.quantite as quantite, c.dateCommande as dateCommande, " +
           "c.dateCommande as dateLivraison " +
           "FROM Commande c " +
           "JOIN c.client cl " +
           "JOIN c.lignesCommande lc " +
           "JOIN lc.produit p " +
           "WHERE c.statut != 'Annulée'")
    List<CommandeLivraisonProjection> findCommandesLivraisons();
    
    @Query("SELECT DISTINCT c FROM Commande c " +
           "LEFT JOIN FETCH c.client " +
           "LEFT JOIN FETCH c.lignesCommande lc " +
           "LEFT JOIN FETCH lc.produit " +
           "ORDER BY c.dateCommande DESC")
    List<Commande> findAllWithRelations();
    
    @Query("SELECT DISTINCT c FROM Commande c " +
           "LEFT JOIN FETCH c.client " +
           "LEFT JOIN FETCH c.lignesCommande lc " +
           "LEFT JOIN FETCH lc.produit " +
           "WHERE c.statut != 'Annulée' " +
           "AND NOT EXISTS (SELECT 1 FROM Livraison l WHERE l.commande.id = c.id) " +
           "ORDER BY c.dateCommande DESC")
    List<Commande> findCommandesSansLivraison();
}
