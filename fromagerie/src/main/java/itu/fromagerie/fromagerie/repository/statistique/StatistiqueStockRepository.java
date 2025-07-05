package itu.fromagerie.fromagerie.repository.statistique;

import itu.fromagerie.fromagerie.entities.statistique.StatistiqueStock;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatistiqueStockRepository extends JpaRepository<StatistiqueStock, Long> {
    List<StatistiqueStock> findByProduit(Produit produit);
    List<StatistiqueStock> findByDateEnregistrementBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT s FROM StatistiqueStock s WHERE s.dateEnregistrement = :date")
    List<StatistiqueStock> findByDateEnregistrement(LocalDate date);
    
    @Query("SELECT AVG(s.quantite) FROM StatistiqueStock s WHERE s.produit = :produit")
    Double getAverageStockByProduit(Produit produit);
}
