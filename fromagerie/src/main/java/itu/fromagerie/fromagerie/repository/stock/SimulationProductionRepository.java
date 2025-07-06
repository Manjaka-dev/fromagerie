package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.SimulationProduction;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SimulationProductionRepository extends JpaRepository<SimulationProduction, Long> {
    List<SimulationProduction> findByProduit(Produit produit);
    List<SimulationProduction> findByDateSimulationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    
    @Query("SELECT s FROM SimulationProduction s ORDER BY s.dateSimulation DESC")
    List<SimulationProduction> findAllOrderByDateDesc();
    
    @Query("SELECT s FROM SimulationProduction s WHERE DATE(s.dateSimulation) BETWEEN :dateDebut AND :dateFin")
    List<SimulationProduction> findSimulationsByPeriode(LocalDate dateDebut, LocalDate dateFin);
}
