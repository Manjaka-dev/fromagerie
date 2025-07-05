package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.Paiement;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByCommande(Commande commande);
    List<Paiement> findByMethode(String methode);
    List<Paiement> findByDatePaiementBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.commande = :commande")
    BigDecimal getTotalMontantByCommande(Commande commande);
}
