package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.MouvementStockProduit;
import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MouvementStockProduitRepository extends JpaRepository<MouvementStockProduit, Long> {
    List<MouvementStockProduit> findByLot(LotProduit lot);
    List<MouvementStockProduit> findByTypeMouvement(String typeMouvement);
    List<MouvementStockProduit> findByDateMouvementBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    
    @Query("SELECT m FROM MouvementStockProduit m WHERE m.lot = :lot ORDER BY m.dateMouvement DESC")
    List<MouvementStockProduit> findByLotOrderByDateDesc(LotProduit lot);
    
    @Query("SELECT m FROM MouvementStockProduit m WHERE m.dateMouvement BETWEEN :dateDebut AND :dateFin ORDER BY m.dateMouvement DESC")
    List<MouvementStockProduit> findMouvementsByDate(@Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);
    
    @Query("SELECT m FROM MouvementStockProduit m WHERE m.typeMouvement = :typeMouvement AND m.dateMouvement BETWEEN :dateDebut AND :dateFin ORDER BY m.dateMouvement DESC")
    List<MouvementStockProduit> findMouvementsByTypeAndDate(@Param("typeMouvement") String typeMouvement, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);
}
