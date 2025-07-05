package itu.fromagerie.fromagerie.repository.statistique;

import itu.fromagerie.fromagerie.entities.statistique.StatistiqueProduction;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatistiqueProductionRepository extends JpaRepository<StatistiqueProduction, Long> {
    List<StatistiqueProduction> findByProduit(Produit produit);
    List<StatistiqueProduction> findByPeriodeBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT SUM(s.quantiteProduite) FROM StatistiqueProduction s WHERE s.produit = :produit")
    Integer getTotalQuantiteProduiteByProduit(Produit produit);
    
    @Query("SELECT SUM(s.quantiteProduite) FROM StatistiqueProduction s WHERE s.periode BETWEEN :dateDebut AND :dateFin")
    Integer getTotalQuantiteProduiteBetween(LocalDate dateDebut, LocalDate dateFin);
}
