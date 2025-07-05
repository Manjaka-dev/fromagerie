package itu.fromage.repositories;

import itu.fromage.entities.LigneCommande;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Integer> {
    List<LigneCommande> findByCommandeId(Integer commandeId);

    @Query("""
        SELECT lc.quantite AS quatite FROM LigneCommande lc WHERE lc.produit.id = :idProduit
    """)
    Integer getQuantiteCommande(@Param("idProduit") Integer idProduit);
}