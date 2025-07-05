package itu.fromagerie.fromagerie.repository.comptabilite;

import itu.fromagerie.fromagerie.entities.comptabilite.BilanFinancier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BilanFinancierRepository extends JpaRepository<BilanFinancier, Long> {
    Optional<BilanFinancier> findByPeriode(LocalDate periode);
    List<BilanFinancier> findByPeriodeBetween(LocalDate dateDebut, LocalDate dateFin);
    List<BilanFinancier> findByProfitGreaterThan(BigDecimal profit);
    
    @Query("SELECT b FROM BilanFinancier b ORDER BY b.periode DESC")
    List<BilanFinancier> findAllOrderByPeriodeDesc();
    
    @Query("SELECT SUM(b.profit) FROM BilanFinancier b WHERE b.periode BETWEEN :dateDebut AND :dateFin")
    BigDecimal getTotalProfitBetween(LocalDate dateDebut, LocalDate dateFin);
}
