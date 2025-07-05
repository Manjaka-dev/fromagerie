package itu.fromagerie.fromagerie.repository.comptabilite;

import itu.fromagerie.fromagerie.entities.comptabilite.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {
    List<Depense> findByCategorie(String categorie);
    List<Depense> findByDateDepenseBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Depense> findByLibelleContainingIgnoreCase(String libelle);
    
    @Query("SELECT SUM(d.montant) FROM Depense d WHERE d.dateDepense BETWEEN :dateDebut AND :dateFin")
    BigDecimal getTotalDepensesBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT SUM(d.montant) FROM Depense d WHERE d.categorie = :categorie")
    BigDecimal getTotalDepensesByCategorie(String categorie);
}
