package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import itu.fromagerie.fromagerie.entities.livraison.Livreur;
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
    
    @Query("SELECT l FROM Livraison l WHERE l.dateLivraison = :date")
    List<Livraison> findByDateLivraison(LocalDate date);
}
