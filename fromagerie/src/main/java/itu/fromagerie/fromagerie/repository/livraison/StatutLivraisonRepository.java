package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.StatutLivraison;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StatutLivraisonRepository extends JpaRepository<StatutLivraison, Long> {
    Optional<StatutLivraison> findTopByLivraisonIdOrderByDateStatutDesc(Integer livraisonId);

    @Query("""
        SELECT sl FROM StatutLivraison sl
        WHERE sl.livraison.commande.id = :idCommande
    """)
    StatutLivraison findStatutCommande(@Param("idCommande") Long idCommande);
}