package itu.fromagerie.fromagerie.repository.production;

import itu.fromagerie.fromagerie.entities.production.PerteProduction;
import itu.fromagerie.fromagerie.entities.production.ProductionEffectuee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PerteProductionRepository extends JpaRepository<PerteProduction, Long> {
    List<PerteProduction> findByProduction(ProductionEffectuee production);
    List<PerteProduction> findByTauxPerteGreaterThan(BigDecimal seuil);
    
    @Query("SELECT AVG(p.tauxPerte) FROM PerteProduction p")
    BigDecimal getAverageTauxPerte();

    @Query("SELECT p.production.dateProduction, SUM(p.tauxPerte) " +
           "FROM PerteProduction p WHERE p.production.dateProduction BETWEEN :dateDebut AND :dateFin GROUP BY p.production.dateProduction")
    List<Object[]> getEvolutionPertesParJour(LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT p.raison, COUNT(p), AVG(p.tauxPerte) " +
           "FROM PerteProduction p WHERE p.production.dateProduction BETWEEN :dateDebut AND :dateFin GROUP BY p.raison")
    List<Object[]> getRepartitionPertes(LocalDate dateDebut, LocalDate dateFin);
}
