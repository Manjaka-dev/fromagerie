package itu.fromagerie.fromagerie.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValorisationStockDTO {
    private LocalDate dateValorisation;
    private BigDecimal valeurTotale;
    private Integer nombreMatieres;
    private List<ValorisationMatiereDTO> valorisations;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValorisationMatiereDTO {
        private Long matiereId;
        private String nomMatiere;
        private BigDecimal quantite;
        private BigDecimal prixUnitaire;
        private BigDecimal valeurTotale;
        private String unite;
        private String statut; // NORMAL, FAIBLE, CRITIQUE
    }
} 