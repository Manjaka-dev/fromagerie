package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    List<Promotion> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT p FROM Promotion p WHERE :date BETWEEN p.dateDebut AND p.dateFin")
    List<Promotion> findPromotionsActives(LocalDate date);

    @Query("SELECT p FROM Promotion p WHERE p.dateFin < :date")
    List<Promotion> findPromotionsExpirees(LocalDate date);

    @Query("SELECT p FROM Promotion p WHERE p.produit.id = :idProduit AND ( :dateCommande BETWEEN p.dateDebut AND p.dateFin)")
    Promotion findPromotionCommande(@Param("idProduit") Long idProduit, @Param("dateCommande") LocalDate dateCommande);


}