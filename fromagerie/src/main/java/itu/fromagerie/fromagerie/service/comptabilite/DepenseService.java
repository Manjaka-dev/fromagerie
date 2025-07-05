package itu.fromagerie.fromagerie.service.comptabilite;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import itu.fromagerie.fromagerie.entities.comptabilite.Depense;
import itu.fromagerie.fromagerie.repository.comptabilite.DepenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DepenseService {

    private final DepenseRepository depenseRepo;

    @Autowired
    public DepenseService(DepenseRepository depenseRepo) {
        this.depenseRepo = depenseRepo;
    }

    /**
     * Retrieves all expense records for a given category.
     * @param categorie The category to query expenses for.
     * @return List of expense records.
     * @throws IllegalArgumentException if categorie is null or empty.
     */
    public List<Depense> findByCategorie(String categorie) {
        Objects.requireNonNull(categorie, "Categorie cannot be null");
        if (categorie.trim().isEmpty()) {
            throw new IllegalArgumentException("Categorie cannot be empty");
        }
        return depenseRepo.findByCategorie(categorie);
    }

    /**
     * Retrieves expense records within a date range.
     * @param dateDebut The start date of the range.
     * @param dateFin The end date of the range.
     * @return List of expense records within the date range.
     * @throws IllegalArgumentException if dateDebut or dateFin is null, or if dateFin is before dateDebut.
     */
    public List<Depense> findByDateDepenseBetween(LocalDate dateDebut, LocalDate dateFin) {
        Objects.requireNonNull(dateDebut, "Start date cannot be null");
        Objects.requireNonNull(dateFin, "End date cannot be null");
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        return depenseRepo.findByDateDepenseBetween(dateDebut, dateFin);
    }

    /**
     * Retrieves expense records by a partial match on the expense label.
     * @param libelle The partial label to search for.
     * @return List of expense records matching the label.
     * @throws IllegalArgumentException if libelle is null or empty.
     */
    public List<Depense> findByLibelleContainingIgnoreCase(String libelle) {
        Objects.requireNonNull(libelle, "Libelle cannot be null");
        if (libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Libelle cannot be empty");
        }
        return depenseRepo.findByLibelleContainingIgnoreCase(libelle);
    }

    /**
     * Calculates the total expenses for a given date range.
     * @param dateDebut The start date of the range.
     * @param dateFin The end date of the range.
     * @return The total expenses, or zero if no records exist.
     * @throws IllegalArgumentException if dateDebut or dateFin is null, or if dateFin is before dateDebut.
     */
    public BigDecimal getTotalDepensesBetween(LocalDate dateDebut, LocalDate dateFin) {
        Objects.requireNonNull(dateDebut, "Start date cannot be null");
        Objects.requireNonNull(dateFin, "End date cannot be null");
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        BigDecimal total = depenseRepo.getTotalDepensesBetween(dateDebut, dateFin);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Calculates the total expenses for a specific category.
     * @param categorie The category to query.
     * @return The total expenses, or zero if no records exist.
     * @throws IllegalArgumentException if categorie is null or empty.
     */
    public BigDecimal getTotalDepensesByCategorie(String categorie) {
        Objects.requireNonNull(categorie, "Categorie cannot be null");
        if (categorie.trim().isEmpty()) {
            throw new IllegalArgumentException("Categorie cannot be empty");
        }
        BigDecimal total = depenseRepo.getTotalDepensesByCategorie(categorie);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Saves a new expense record.
     * @param depense The expense record to save.
     * @return The saved expense record.
     * @throws IllegalArgumentException if depense is null.
     */
    public Depense saveDepense(Depense depense) {
        Objects.requireNonNull(depense, "Depense cannot be null");
        return depenseRepo.save(depense);
    }

    /**
     * Retrieves an expense record by its ID.
     * @param id The ID of the expense record.
     * @return The expense record, or null if not found.
     * @throws IllegalArgumentException if id is null.
     */
    public Depense findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        Optional<Depense> depense = depenseRepo.findById(id);
        return depense.orElse(null);
    }
}