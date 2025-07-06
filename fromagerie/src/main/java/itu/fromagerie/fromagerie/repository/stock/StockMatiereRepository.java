package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.StockMatiere;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockMatiereRepository extends JpaRepository<StockMatiere, Long> {
    Optional<StockMatiere> findByMatiere(MatierePremiere matiere);
    List<StockMatiere> findByQuantiteLessThan(BigDecimal seuil);
    
    @Query("SELECT s FROM StockMatiere s WHERE s.quantite > 0")
    List<StockMatiere> findStocksDisponibles();
}
