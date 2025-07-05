package itu.fromagerie.fromagerie.service.production;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import itu.fromagerie.fromagerie.entities.production.PerteProduction;
import itu.fromagerie.fromagerie.entities.production.ProductionEffectuee;
import itu.fromagerie.fromagerie.repository.production.PerteProductionRepository;
import itu.fromagerie.fromagerie.repository.production.ProductionEffectueeRepository;

@Service
public class PerteProductionService {

    private final PerteProductionRepository perteRepo;
    private final ProductionEffectueeRepository productionRepo;

    @Autowired
    public PerteProductionService(PerteProductionRepository perteRepo, ProductionEffectueeRepository productionRepo) {
        this.perteRepo = perteRepo;
        this.productionRepo = productionRepo;
    }

    /**
     * Retrieves all loss records for a given production.
     * @param production The production to query loss records for.
     * @return List of loss records.
     * @throws IllegalArgumentException if production is null.
     */
    public List<PerteProduction> findPerteByProduction(ProductionEffectuee production) {
        Objects.requireNonNull(production, "Production cannot be null");
        return perteRepo.findByProduction(production);
    }

    /**
     * Retrieves loss records with a loss rate greater than the specified threshold.
     * @param seuil The threshold loss rate.
     * @return List of loss records exceeding the threshold.
     * @throws IllegalArgumentException if seuil is null.
     */
    public List<PerteProduction> findByTauxPerteGreater(BigDecimal seuil) {
        Objects.requireNonNull(seuil, "Threshold (seuil) cannot be null");
        return perteRepo.findByTauxPerteGreaterThan(seuil);
    }

    /**
     * Calculates the average loss rate across all loss records.
     * @return The average loss rate, or zero if no records exist.
     */
    public BigDecimal getAverageTauxPerte() {
        BigDecimal average = perteRepo.getAverageTauxPerte();
        return average != null ? average : BigDecimal.ZERO;
    }
}