package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.Facture;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    Optional<Facture> findByCommande(Commande commande);
    List<Facture> findByDateFactureBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Facture> findByMontantTotalGreaterThan(BigDecimal montant);
    
    @Query("SELECT SUM(f.montantTotal) FROM Facture f WHERE f.dateFacture BETWEEN :dateDebut AND :dateFin")
    BigDecimal getTotalMontantBetween(LocalDate dateDebut, LocalDate dateFin);
}
