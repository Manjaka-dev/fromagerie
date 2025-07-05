package itu.fromagerie.fromagerie.repository.production;

import itu.fromagerie.fromagerie.entities.production.ProductionEffectuee;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductionEffectueeRepository extends JpaRepository<ProductionEffectuee, Long> {
    List<ProductionEffectuee> findByProduit(Produit produit);
    List<ProductionEffectuee> findByDateProductionBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT p FROM ProductionEffectuee p WHERE p.dateProduction = :date")
    List<ProductionEffectuee> findByDateProduction(LocalDate date);
    
    @Query("SELECT SUM(p.quantiteProduite) FROM ProductionEffectuee p WHERE p.produit = :produit")
    Integer getTotalProducedByProduit(Produit produit);

    @Query("SELECT SUM(p.quantiteProduite) FROM ProductionEffectuee p WHERE p.dateProduction BETWEEN :dateDebut AND :dateFin")
    Integer getTotalProductionBetween(LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT AVG(p.quantiteProduite) FROM ProductionEffectuee p WHERE p.dateProduction BETWEEN :dateDebut AND :dateFin")
    Double getTauxQualiteMoyen(LocalDate dateDebut, LocalDate dateFin);
}
