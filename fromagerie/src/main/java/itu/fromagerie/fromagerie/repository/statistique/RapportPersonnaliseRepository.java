package itu.fromagerie.fromagerie.repository.statistique;

import itu.fromagerie.fromagerie.entities.statistique.RapportPersonnalise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RapportPersonnaliseRepository extends JpaRepository<RapportPersonnalise, Long> {
    List<RapportPersonnalise> findByNomContainingIgnoreCase(String nom);
    List<RapportPersonnalise> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    
    @Query("SELECT r FROM RapportPersonnalise r ORDER BY r.dateCreation DESC")
    List<RapportPersonnalise> findAllOrderByDateDesc();

}
