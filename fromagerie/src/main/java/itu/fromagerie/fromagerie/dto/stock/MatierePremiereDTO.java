package itu.fromagerie.fromagerie.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatierePremiereDTO {
    private Long id;
    private String nom;
    private String unite;
    private Integer dureeConservation;
    private BigDecimal quantiteEnStock;
    private String statutStock; // "NORMAL", "FAIBLE", "CRITIQUE", "VIDE"
}
