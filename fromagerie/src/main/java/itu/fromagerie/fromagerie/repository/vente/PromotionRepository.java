package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByNomContainingIgnoreCase(String nom);
    
    @Query("SELECT p FROM Promotion p WHERE :date BETWEEN p.dateDebut AND p.dateFin")
    List<Promotion> findPromotionsActives(LocalDate date);
    
    @Query("SELECT p FROM Promotion p WHERE p.dateFin < :date")
    List<Promotion> findPromotionsExpirees(LocalDate date);
}
