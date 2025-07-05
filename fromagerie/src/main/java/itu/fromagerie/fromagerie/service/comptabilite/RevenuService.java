package itu.fromagerie.fromagerie.service.comptabilite;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import itu.fromagerie.fromagerie.entities.comptabilite.Revenu;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.repository.comptabilite.RevenuRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RevenuService {

    private final RevenuRepository revenuRepo;

    @Autowired
    public RevenuService(RevenuRepository revenuRepo) {
        this.revenuRepo = revenuRepo;
    }

    /**
     * Retrieves a revenue record for a given order.
     * @param commande The order to query revenue for.
     * @return The revenue record, or null if not found.
     * @throws IllegalArgumentException if commande is null.
     */
    public Revenu findByCommande(Commande commande) {
        Objects.requireNonNull(commande, "Commande cannot be null");
        Optional<Revenu> revenu = revenuRepo.findByCommande(commande);
        return revenu.orElse(null);
    }

    /**
     * Retrieves revenue records within a date range.
     * @param dateDebut The start date of the range.
     * @param dateFin The end date of the range.
     * @return List of revenue records within the date range.
     * @throws IllegalArgumentException if dateDebut or dateFin is null, or if dateFin is before dateDebut.
     */
    public List<Revenu> findByDateRevenuBetween(LocalDate dateDebut, LocalDate dateFin) {
        Objects.requireNonNull(dateDebut, "Start date cannot be null");
        Objects.requireNonNull(dateFin, "End date cannot be null");
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        return revenuRepo.findByDateRevenuBetween(dateDebut, dateFin);
    }

    /**
     * Calculates the total revenue for a given date range.
     * @param dateDebut The start date of the range.
     * @param dateFin The end date of the range.
     * @return The total revenue, or zero if no records exist.
     * @throws IllegalArgumentException if dateDebut or dateFin is null, or if dateFin is before dateDebut.
     */
    public BigDecimal getTotalRevenusBetween(LocalDate dateDebut, LocalDate dateFin) {
        Objects.requireNonNull(dateDebut, "Start date cannot be null");
        Objects.requireNonNull(dateFin, "End date cannot be null");
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        BigDecimal total = revenuRepo.getTotalRevenusBetween(dateDebut, dateFin);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Calculates the total revenue for a specific date.
     * @param date The date to query.
     * @return The total revenue, or zero if no records exist.
     * @throws IllegalArgumentException if date is null.
     */
    public BigDecimal getTotalRevenusByDate(LocalDate date) {
        Objects.requireNonNull(date, "Date cannot be null");
        BigDecimal total = revenuRepo.getTotalRevenusByDate(date);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Saves a new revenue record.
     * @param revenu The revenue record to save.
     * @return The saved revenue record.
     * @throws IllegalArgumentException if revenu is null.
     */
    public Revenu saveRevenu(Revenu revenu) {
        Objects.requireNonNull(revenu, "Revenu cannot be null");
        return revenuRepo.save(revenu);
    }

    /**
     * Retrieves a revenue record by its ID.
     * @param id The ID of the revenue record.
     * @return The revenue record, or null if not found.
     * @throws IllegalArgumentException if id is null.
     */
    public Revenu findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        Optional<Revenu> revenu = revenuRepo.findById(id);
        return revenu.orElse(null);
    }
}