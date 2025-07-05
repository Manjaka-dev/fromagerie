package itu.fromagerie.fromagerie.repository.comptabilite;

import itu.fromagerie.fromagerie.entities.comptabilite.Revenu;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevenuRepository extends JpaRepository<Revenu, Long> {
    Optional<Revenu> findByCommande(Commande commande);
    List<Revenu> findByDateRevenuBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT SUM(r.montant) FROM Revenu r WHERE r.dateRevenu BETWEEN :dateDebut AND :dateFin")
    BigDecimal getTotalRevenusBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT SUM(r.montant) FROM Revenu r WHERE r.dateRevenu = :date")
    BigDecimal getTotalRevenusByDate(LocalDate date);
}
