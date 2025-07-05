package itu.fromagerie.fromagerie.repository.production;

import itu.fromagerie.fromagerie.entities.production.TransformationDechet;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransformationDechetRepository extends JpaRepository<TransformationDechet, Long> {
    List<TransformationDechet> findByMatiere(MatierePremiere matiere);
    List<TransformationDechet> findByDateTransformationBetween(LocalDate dateDebut, LocalDate dateFin);
    List<TransformationDechet> findByProduitFinalContainingIgnoreCase(String produitFinal);
    
    @Query("SELECT SUM(t.quantiteTransforme) FROM TransformationDechet t WHERE t.matiere = :matiere")
    Double getTotalTransformedByMatiere(MatierePremiere matiere);
}
