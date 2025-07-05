package itu.fromagerie.fromagerie.service.production;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import itu.fromagerie.fromagerie.entities.production.TransformationDechet;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import itu.fromagerie.fromagerie.repository.production.TransformationDechetRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransformationDechetService {

    private final TransformationDechetRepository transformationDechetRepo;

    @Autowired
    public TransformationDechetService(TransformationDechetRepository transformationDechetRepo) {
        this.transformationDechetRepo = transformationDechetRepo;
    }

    /**
     * Retrieves all waste transformation records for a given raw material.
     * @param matiere The raw material to query transformation records for.
     * @return List of transformation records.
     * @throws IllegalArgumentException if matiere is null.
     */
    public List<TransformationDechet> findByMatiere(MatierePremiere matiere) {
        Objects.requireNonNull(matiere, "MatierePremiere cannot be null");
        return transformationDechetRepo.findByMatiere(matiere);
    }

    /**
     * Retrieves all waste transformation records within a date range.
     * @param dateDebut The start date of the range.
     * @param dateFin The end date of the range.
     * @return List of transformation records within the date range.
     * @throws IllegalArgumentException if dateDebut or dateFin is null, or if dateFin is before dateDebut.
     */
    public List<TransformationDechet> findByDateTransformationBetween(LocalDate dateDebut, LocalDate dateFin) {
        Objects.requireNonNull(dateDebut, "Start date cannot be null");
        Objects.requireNonNull(dateFin, "End date cannot be null");
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        return transformationDechetRepo.findByDateTransformationBetween(dateDebut, dateFin);
    }

    /**
     * Retrieves waste transformation records by a partial match on the final product name.
     * @param produitFinal The partial name of the final product to search for.
     * @return List of transformation records matching the final product name.
     * @throws IllegalArgumentException if produitFinal is null or empty.
     */
    public List<TransformationDechet> findByProduitFinalContainingIgnoreCase(String produitFinal) {
        Objects.requireNonNull(produitFinal, "ProduitFinal cannot be null");
        if (produitFinal.trim().isEmpty()) {
            throw new IllegalArgumentException("ProduitFinal cannot be empty");
        }
        return transformationDechetRepo.findByProduitFinalContainingIgnoreCase(produitFinal);
    }

    /**
     * Calculates the total quantity of waste transformed for a given raw material.
     * @param matiere The raw material to query.
     * @return The total transformed quantity, or 0.0 if none.
     * @throws IllegalArgumentException if matiere is null.
     */
    public Double getTotalTransformedByMatiere(MatierePremiere matiere) {
        Objects.requireNonNull(matiere, "MatierePremiere cannot be null");
        Double total = transformationDechetRepo.getTotalTransformedByMatiere(matiere);
        return total != null ? total : 0.0;
    }

    /**
     * Saves a new waste transformation record.
     * @param transformationDechet The transformation record to save.
     * @return The saved transformation record.
     * @throws IllegalArgumentException if transformationDechet is null.
     */
    public TransformationDechet saveTransformationDechet(TransformationDechet transformationDechet) {
        Objects.requireNonNull(transformationDechet, "TransformationDechet cannot be null");
        return transformationDechetRepo.save(transformationDechet);
    }

    /**
     * Retrieves a waste transformation record by its ID.
     * @param id The ID of the transformation record.
     * @return The transformation record, or null if not found.
     * @throws IllegalArgumentException if id is null.
     */
    public TransformationDechet findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        Optional<TransformationDechet> transformation = transformationDechetRepo.findById(id);
        return transformation.orElse(null);
    }
}