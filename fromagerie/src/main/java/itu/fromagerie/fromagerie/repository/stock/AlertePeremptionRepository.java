package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.AlertePeremption;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlertePeremptionRepository extends JpaRepository<AlertePeremption, Long> {
    List<AlertePeremption> findByMatiere(MatierePremiere matiere);
    List<AlertePeremption> findByDatePeremptionBefore(LocalDate date);
    
    @Query("SELECT a FROM AlertePeremption a WHERE a.datePeremption BETWEEN :dateDebut AND :dateFin")
    List<AlertePeremption> findAlertesEntre(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT a FROM AlertePeremption a WHERE a.datePeremption <= :date ORDER BY a.datePeremption ASC")
    List<AlertePeremption> findActiveAlertes(LocalDate date);
}
