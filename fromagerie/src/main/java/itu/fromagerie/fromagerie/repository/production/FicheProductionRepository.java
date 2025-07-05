package itu.fromagerie.fromagerie.repository.production;

import itu.fromagerie.fromagerie.entities.production.FicheProduction;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FicheProductionRepository extends JpaRepository<FicheProduction, Long> {
    List<FicheProduction> findByProduit(Produit produit);
    List<FicheProduction> findByMatiere(MatierePremiere matiere);
}
