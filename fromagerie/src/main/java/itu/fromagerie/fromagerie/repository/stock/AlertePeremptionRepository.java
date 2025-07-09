package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.AlertePeremption;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlertePeremptionRepository extends JpaRepository<AlertePeremption, Long> {
    List<AlertePeremption> findByMatiere(MatierePremiere matiere);
    List<AlertePeremption> findByDatePeremptionBefore(LocalDate date);
    
    @Query("SELECT a FROM AlertePeremption a WHERE a.datePeremption BETWEEN :dateDebut AND :dateFin")
    List<AlertePeremption> findAlertesEntre(LocalDate dateDebut, LocalDate dateFin);
    
    /**
     * Trouve toutes les alertes actives pour une date donnée, où:
     * 1. La date de péremption est proche (moins de jours restants que le seuil d'alerte)
     * 2. La date est aujourd'hui ou dans le futur (pas déjà périmée)
     *
     * @param today Date actuelle utilisée comme référence
     * @return Liste des alertes de péremption actives
     */
    @Query(value = "SELECT * FROM alerte_peremption a WHERE a.date_peremption >= :today AND " +
           "(a.date_peremption - :today) <= a.seuil_alerte", nativeQuery = true)
    List<AlertePeremption> findActiveAlertes(@Param("today") LocalDate today);
    
    /**
     * Trouve toutes les alertes où les produits sont déjà périmés
     *
     * @param today Date actuelle utilisée comme référence
     * @return Liste des alertes de produits périmés
     */
    @Query("SELECT a FROM AlertePeremption a WHERE a.datePeremption < :today")
    List<AlertePeremption> findExpiredAlertes(@Param("today") LocalDate today);
}
