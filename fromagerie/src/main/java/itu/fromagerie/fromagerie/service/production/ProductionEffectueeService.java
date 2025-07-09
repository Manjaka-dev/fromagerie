package itu.fromagerie.fromagerie.service.production;

import org.springframework.stereotype.Service;

import itu.fromagerie.fromagerie.entities.production.ProductionEffectuee;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.repository.production.ProductionEffectueeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ProductionEffectueeService {

    private final ProductionEffectueeRepository proEffecRepo;

    public ProductionEffectueeService(ProductionEffectueeRepository proEffecRepo) {
        this.proEffecRepo = proEffecRepo;
    }

    /**
     * Retrieves all production records for a given product.
     * @param produit The product to query production records for.
     * @return List of production records.
     * @throws IllegalArgumentException if produit is null.
     */
    public List<ProductionEffectuee> findProductionsByProduit(Produit produit) {
        Objects.requireNonNull(produit, "Produit cannot be null");
        return proEffecRepo.findByProduit(produit);
    }

    /**
     * Retrieves production records between two dates.
     * @param dateDebut Start date.
     * @param dateFin End date.
     * @return List of production records within the date range.
     * @throws IllegalArgumentException if dates are null or dateFin is before dateDebut.
     */
    public List<ProductionEffectuee> findProductionsByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        Objects.requireNonNull(dateDebut, "Start date cannot be null");
        Objects.requireNonNull(dateFin, "End date cannot be null");
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        return proEffecRepo.findByDateProductionBetween(dateDebut, dateFin);
    }

    /**
     * Retrieves production records for a specific date.
     * @param date The date to query.
     * @return List of production records for the given date.
     * @throws IllegalArgumentException if date is null.
     */
    public List<ProductionEffectuee> findProductionsByDate(LocalDate date) {
        Objects.requireNonNull(date, "Date cannot be null");
        return proEffecRepo.findByDateProduction(date);
    }

    /**
     * Calculates the total quantity produced for a given product.
     * @param produit The product to query.
     * @return Total quantity produced, or 0 if none.
     * @throws IllegalArgumentException if produit is null.
     */
    public Integer getTotalProducedByProduit(Produit produit) {
        Objects.requireNonNull(produit, "Produit cannot be null");
        Integer total = proEffecRepo.getTotalProducedByProduit(produit);
        return total != null ? total : 0;
    }
}