package itu.fromagerie.fromagerie.repository.production;

import itu.fromagerie.fromagerie.entities.production.PerteProduction;
import itu.fromagerie.fromagerie.entities.production.ProductionEffectuee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    /**
     * Récupère l'évolution des pertes par jour sur une période donnée
     * 
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @return Liste des paires [date, valeur] représentant les pertes par jour
     */
    @Query("SELECT p.production.dateProduction, SUM(p.tauxPerte * p.production.quantiteProduite / 100.0) " +
           "FROM PerteProduction p " +
           "WHERE p.production.dateProduction BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY p.production.dateProduction " +
           "ORDER BY p.production.dateProduction")
    List<Object[]> getEvolutionPertesParJour(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
    
    /**
     * Récupère la répartition des pertes par type sur une période donnée
     * 
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @return Liste des paires [type, nombre, pourcentage] représentant la répartition des pertes
     */
    @Query("SELECT COALESCE(p.typePerte, 'Non catégorisé') AS type, COUNT(p), " +
           "SUM(p.tauxPerte) / (SELECT COALESCE(SUM(pp.tauxPerte), 1.0) FROM PerteProduction pp WHERE pp.production.dateProduction BETWEEN :dateDebut AND :dateFin) * 100 " +
           "FROM PerteProduction p " +
           "WHERE p.production.dateProduction BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY COALESCE(p.typePerte, 'Non catégorisé') " +
           "ORDER BY COUNT(p) DESC")
    List<Object[]> getRepartitionPertes(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}
