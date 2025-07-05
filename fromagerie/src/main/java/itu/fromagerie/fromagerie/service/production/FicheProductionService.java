package itu.fromagerie.fromagerie.service.production;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import itu.fromagerie.fromagerie.entities.production.FicheProduction;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import itu.fromagerie.fromagerie.repository.production.FicheProductionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FicheProductionService {

    private final FicheProductionRepository ficheProductionRepo;

    @Autowired
    public FicheProductionService(FicheProductionRepository ficheProductionRepo) {
        this.ficheProductionRepo = ficheProductionRepo;
    }

    /**
     * Retrieves all production sheet records for a given product.
     * @param produit The product to query production sheets for.
     * @return List of production sheet records.
     * @throws IllegalArgumentException if produit is null.
     */
    public List<FicheProduction> findByProduit(Produit produit) {
        Objects.requireNonNull(produit, "Produit cannot be null");
        return ficheProductionRepo.findByProduit(produit);
    }

    /**
     * Retrieves all production sheet records for a given raw material.
     * @param matiere The raw material to query production sheets for.
     * @return List of production sheet records.
     * @throws IllegalArgumentException if matiere is null.
     */
    public List<FicheProduction> findByMatiere(MatierePremiere matiere) {
        Objects.requireNonNull(matiere, "MatierePremiere cannot be null");
        return ficheProductionRepo.findByMatiere(matiere);
    }

    /**
     * Saves a new production sheet record.
     * @param ficheProduction The production sheet to save.
     * @return The saved production sheet.
     * @throws IllegalArgumentException if ficheProduction is null.
     */
    public FicheProduction saveFicheProduction(FicheProduction ficheProduction) {
        Objects.requireNonNull(ficheProduction, "FicheProduction cannot be null");
        return ficheProductionRepo.save(ficheProduction);
    }

    /**
     * Retrieves a production sheet by its ID.
     * @param id The ID of the production sheet.
     * @return The production sheet, or null if not found.
     * @throws IllegalArgumentException if id is null.
     */
    public FicheProduction findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        Optional<FicheProduction> fiche = ficheProductionRepo.findById(id);
        return fiche.orElse(null);
    }
}