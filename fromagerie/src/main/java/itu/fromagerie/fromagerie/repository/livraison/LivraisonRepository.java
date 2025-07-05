package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import itu.fromagerie.fromagerie.entities.livraison.StatutLivraisonEnum;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    Optional<Livraison> findByCommande(Commande commande);
    List<Livraison> findByLivreur(Livreur livreur);
    List<Livraison> findByZone(String zone);
    List<Livraison> findByDateLivraisonBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Livraison> findByStatutLivraison(StatutLivraisonEnum statut);
    
    @Query("SELECT l FROM Livraison l WHERE l.dateLivraison = :date")
    List<Livraison> findByDateLivraison(LocalDate date);
    
    @Query("SELECT l FROM Livraison l WHERE l.dateLivraison >= CURRENT_DATE ORDER BY l.dateLivraison ASC")
    List<Livraison> findProchainesLivraisons();
    
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
    
    @Query("""
        SELECT l FROM Livraison l 
        JOIN FETCH l.commande c 
        JOIN FETCH c.client cl 
        JOIN FETCH l.livreur lr 
        WHERE l.statutLivraison = :statut
        ORDER BY l.dateLivraison ASC
        """)
    List<Livraison> findByStatutWithDetails(StatutLivraisonEnum statut);
}
