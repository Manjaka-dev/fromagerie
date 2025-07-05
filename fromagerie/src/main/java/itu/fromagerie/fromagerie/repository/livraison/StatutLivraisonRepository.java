package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.StatutLivraison;
import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatutLivraisonRepository extends JpaRepository<StatutLivraison, Long> {
    List<StatutLivraison> findByLivraison(Livraison livraison);
    List<StatutLivraison> findByStatut(String statut);
    List<StatutLivraison> findByDateStatutBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    
    @Query("SELECT s FROM StatutLivraison s WHERE s.livraison = :livraison ORDER BY s.dateStatut DESC")
    List<StatutLivraison> findByLivraisonOrderByDateDesc(Livraison livraison);
}
