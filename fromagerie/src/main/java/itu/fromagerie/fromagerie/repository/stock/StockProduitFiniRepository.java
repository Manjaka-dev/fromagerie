package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.StockProduitFini;
import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockProduitFiniRepository extends JpaRepository<StockProduitFini, Long> {
    Optional<StockProduitFini> findByLot(LotProduit lot);
    List<StockProduitFini> findByQuantiteGreaterThan(Integer seuil);
    
    @Query("SELECT s FROM StockProduitFini s WHERE s.quantite > 0")
    List<StockProduitFini> findStocksDisponibles();
    
    @Query("SELECT COALESCE(SUM(s.quantite), 0) FROM StockProduitFini s WHERE s.lot.dateFabrication BETWEEN :dateDebut AND :dateFin")
    Integer getQuantiteTotaleByPeriode(LocalDate dateDebut, LocalDate dateFin);
}
