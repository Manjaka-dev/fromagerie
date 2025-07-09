package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.MouvementStockProduit;
import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import itu.fromagerie.fromagerie.entities.produit.Produit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MouvementStockProduitRepository extends JpaRepository<MouvementStockProduit, Long> {

    // ✅ Recherche par lot
    List<MouvementStockProduit> findByLot(LotProduit lot);

    // ✅ Recherche par type de mouvement
    List<MouvementStockProduit> findByTypeMouvement(String typeMouvement);

    // ✅ Recherche par date
    List<MouvementStockProduit> findByDateMouvementBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // ✅ Produit via lot
    @Query("SELECT m FROM MouvementStockProduit m WHERE m.lot.produit = :produit ORDER BY m.dateMouvement DESC")
    List<MouvementStockProduit> findByLot_ProduitOrderByDateMouvementDesc(@Param("produit") Produit produit);

    // ✅ Utilisation de l'ID produit via lot.produit.id
    @Query("SELECT m FROM MouvementStockProduit m WHERE m.lot.produit.id = :produitId ORDER BY m.dateMouvement DESC")
    List<MouvementStockProduit> findByProduitIdOrderByDateMouvementDesc(@Param("produitId") Long produitId);

    @Query("SELECT m FROM MouvementStockProduit m WHERE m.lot.produit.id = :produitId AND m.dateMouvement BETWEEN :dateDebut AND :dateFin")
    List<MouvementStockProduit> findByProduitIdAndDateMouvementBetween(
            @Param("produitId") Long produitId,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    @Query("SELECT m FROM MouvementStockProduit m ORDER BY m.dateMouvement DESC")
    List<MouvementStockProduit> findAllByOrderByDateMouvementDesc();

    @Query("""
                SELECT p.id, p.nom, COALESCE(SUM(
                    CASE
                        WHEN m.typeMouvement = 'ENTREE' THEN m.quantite
                        WHEN m.typeMouvement = 'SORTIE' THEN -m.quantite
                        ELSE 0
                    END
                ), 0)
                FROM Produit p
                LEFT JOIN LotProduit l ON l.produit = p
                LEFT JOIN MouvementStockProduit m ON m.lot = l
                GROUP BY p.id, p.nom
            """)
    List<Object[]> findStockCourantParProduitAvecZeroSiAucunMouvement();

}
