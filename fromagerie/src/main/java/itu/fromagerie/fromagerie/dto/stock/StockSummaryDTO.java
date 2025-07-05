package itu.fromagerie.fromagerie.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockSummaryDTO {
    private Long matiereId;
    private String nomMatiere;
    private String unite;
    private BigDecimal quantiteActuelle;
    private BigDecimal quantiteMinimum;
    private String statutStock; // "NORMAL", "FAIBLE", "CRITIQUE", "VIDE"
    private Integer joursRestantsStock; // estimation basée sur consommation moyenne
    private BigDecimal valeurStock;
}
