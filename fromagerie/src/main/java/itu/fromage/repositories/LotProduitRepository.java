package itu.fromage.repositories;

import itu.fromage.entities.LotProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LotProduitRepository extends JpaRepository<LotProduit, Integer> {
    @Query("""
        SELECT quantite FROM LotProduit WHERE produit.id = :id AND datePeremption > current date 
    """)
    Integer getQuantiteProduit(@Param("id") int id);

   @Modifying
    @Query("""
        UPDATE LotProduit SET quantite = :qt WHERE produit.id = :idProduit
    """)
    void updateLotProduitBy(@Param("idProduit") int idProduit, @Param("qt") int quantite);
}