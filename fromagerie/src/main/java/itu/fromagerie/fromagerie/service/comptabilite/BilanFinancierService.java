package itu.fromagerie.fromagerie.service.comptabilite;

import org.springframework.stereotype.Service;

import itu.fromagerie.fromagerie.entities.comptabilite.BilanFinancier;
import itu.fromagerie.fromagerie.repository.comptabilite.BilanFinancierRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BilanFinancierService {

    private final BilanFinancierRepository bilanFinancierRepo;

    public BilanFinancierService(BilanFinancierRepository bilanFinancierRepo) {
        this.bilanFinancierRepo = bilanFinancierRepo;
    }

    /**
     * Retrieves a financial balance record for a specific period.
     * @param periode The period to query.
     * @return The financial balance record, or null if not found.
     * @throws IllegalArgumentException if periode is null.
     */
    public BilanFinancier findByPeriode(LocalDate periode) {
        Objects.requireNonNull(periode, "Periode cannot be null");
        Optional<BilanFinancier> bilan = bilanFinancierRepo.findByPeriode(periode);
        return bilan.orElse(null);
    }

    /**
     * Retrieves financial balance records within a date range.
     * @param dateDebut The start date of the range.
     * @param dateFin The end date of the range.
     * @return List of financial balance records within the date range.
     * @throws IllegalArgumentException if dateDebut or dateFin is null, or if dateFin is before dateDebut.
     */
    public List<BilanFinancier> findByPeriodeBetween(LocalDate dateDebut, LocalDate dateFin) {
        Objects.requireNonNull(dateDebut, "Start date cannot be null");
        Objects.requireNonNull(dateFin, "End date cannot be null");
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        return bilanFinancierRepo.findByPeriodeBetween(dateDebut, dateFin);
    }

    /**
     * Retrieves financial balance records with profit greater than the specified amount.
     * @param profit The profit threshold.
     * @return List of financial balance records exceeding the profit threshold.
     * @throws IllegalArgumentException if profit is null.
     */
    public List<BilanFinancier> findByProfitGreaterThan(BigDecimal profit) {
        Objects.requireNonNull(profit, "Profit cannot be null");
        return bilanFinancierRepo.findByProfitGreaterThan(profit);
    }

    /**
     * Retrieves all financial balance records ordered by period in descending order.
     * @return List of financial balance records.
     */
    public List<BilanFinancier> findAllOrderByPeriodeDesc() {
        return bilanFinancierRepo.findAllOrderByPeriodeDesc();
    }

    /**
     * Calculates the total profit for financial balance records within a date range.
     * @param dateDebut The start date of the range.
     * @param dateFin The end date of the range.
     * @return The total profit, Communications and Electronics
or zero if no records exist.
     * @throws IllegalArgumentException if dateDebut or dateFin is null, or if dateFin is before dateDebut.
     */
    public BigDecimal getTotalProfitBetween(LocalDate dateDebut, LocalDate dateFin) {
        Objects.requireNonNull(dateDebut, "Start date cannot be null");
        Objects.requireNonNull(dateFin, "End date cannot be null");
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        BigDecimal total = bilanFinancierRepo.getTotalProfitBetween(dateDebut, dateFin);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Saves a new financial balance record.
     * @param bilanFinancier The financial balance record to save.
     * @return The saved financial balance record.
     * @throws IllegalArgumentException if bilanFinancier is null.
     */
    public BilanFinancier saveBilanFinancier(BilanFinancier bilanFinancier) {
        Objects.requireNonNull(bilanFinancier, "BilanFinancier cannot be null");
        return bilanFinancierRepo.save(bilanFinancier);
    }

    /**
     * Retrieves a financial balance record by its ID.
     * @param id The ID of the financial balance record.
     * @return The financial balance record, or null if not found.
     * @throws IllegalArgumentException if id is null.
     */
    public BilanFinancier findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        Optional<BilanFinancier> bilan = bilanFinancierRepo.findById(id);
        return bilan.orElse(null);
    }
}