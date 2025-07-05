package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.MouvementStockMatiere;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MouvementStockMatiereRepository extends JpaRepository<MouvementStockMatiere, Long> {
    List<MouvementStockMatiere> findByMatiere(MatierePremiere matiere);
    List<MouvementStockMatiere> findByTypeMouvement(String typeMouvement);
    List<MouvementStockMatiere> findByDateMouvementBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    
    @Query("SELECT m FROM MouvementStockMatiere m WHERE m.matiere = :matiere ORDER BY m.dateMouvement DESC")
    List<MouvementStockMatiere> findByMatiereOrderByDateDesc(MatierePremiere matiere);
    
    // Méthodes pour la gestion des déchets
    List<MouvementStockMatiere> findByTypeMouvementAndCommentaireContaining(String typeMouvement, String commentaire);
    
    @Query("SELECT m FROM MouvementStockMatiere m WHERE m.typeMouvement = 'dechet' ORDER BY m.dateMouvement DESC")
    List<MouvementStockMatiere> findAllDechets();
    
    // Méthodes par matière ID
    @Query("SELECT m FROM MouvementStockMatiere m WHERE m.matiere.id = :matiereId AND m.dateMouvement BETWEEN :dateDebut AND :dateFin")
    List<MouvementStockMatiere> findByMatiereIdAndDateMouvementBetween(Long matiereId, LocalDateTime dateDebut, LocalDateTime dateFin);
    
    @Query("SELECT m FROM MouvementStockMatiere m WHERE m.matiere.id = :matiereId ORDER BY m.dateMouvement DESC")
    List<MouvementStockMatiere> findByMatiereIdOrderByDateMouvementDesc(Long matiereId);
    
    @Query("SELECT m FROM MouvementStockMatiere m ORDER BY m.dateMouvement DESC")
    List<MouvementStockMatiere> findAllByOrderByDateMouvementDesc();
}
