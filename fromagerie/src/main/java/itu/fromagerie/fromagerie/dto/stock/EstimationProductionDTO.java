package itu.fromagerie.fromagerie.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstimationProductionDTO {
    private Long produitId;
    private String nomProduit;
    private Integer quantiteSouhaitee;
    private Boolean productionPossible;
    private String raisonImpossible;
    private java.util.List<MatiereMangante> matieresManquantes;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatiereMangante {
        private Long matiereId;
        private String nomMatiere;
        private BigDecimal quantiteNecessaire;
        private BigDecimal quantiteDisponible;
        private BigDecimal quantiteManquante;
    }
}
