package itu.fromagerie.fromagerie.repository.statistique;

import itu.fromagerie.fromagerie.entities.statistique.StatistiqueVente;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatistiqueVenteRepository extends JpaRepository<StatistiqueVente, Long> {
    List<StatistiqueVente> findByProduit(Produit produit);
    List<StatistiqueVente> findByPeriodeBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT SUM(s.quantiteVendue) FROM StatistiqueVente s WHERE s.produit = :produit")
    Integer getTotalQuantiteVendueByProduit(Produit produit);
    
    @Query("SELECT SUM(s.revenuTotal) FROM StatistiqueVente s WHERE s.periode BETWEEN :dateDebut AND :dateFin")
    BigDecimal getTotalRevenuBetween(LocalDate dateDebut, LocalDate dateFin);
}
