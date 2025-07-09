package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.Promotion;
import itu.fromagerie.fromagerie.entities.vente.PromotionCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromotionCommandeRepository extends JpaRepository<PromotionCommande, Integer> {
    @Query("""
        SELECT pc FROM PromotionCommande pc WHERE pc.commande.id = :idCommande
    """)
    PromotionCommande findStatutPromotionCommande(@Param("idCommande") Long idCommande);

    @Query("""
        SELECT lc.promotion FROM PromotionCommande lc WHERE lc.commande.id = :idCommande
    """)
    Promotion getPromotionByCommandeId(@Param("idCommande") Long idCommande);
}