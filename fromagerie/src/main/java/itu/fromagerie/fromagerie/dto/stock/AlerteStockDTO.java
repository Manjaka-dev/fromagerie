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
public class AlerteStockDTO {
    private List<AlerteStockFaibleDTO> alertesStockFaible;
    private List<AlertePeremptionDTO> alertesPeremption;
    private List<AlerteRuptureDTO> alertesRupture;
    private Integer nombreAlertesTotal;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlerteStockFaibleDTO {
        private Long matiereId;
        private String nomMatiere;
        private BigDecimal quantiteActuelle;
        private BigDecimal seuilAlerte;
        private String unite;
        private String niveau; // FAIBLE, CRITIQUE
        private LocalDate dateAlerte;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlerteRuptureDTO {
        private Long matiereId;
        private String nomMatiere;
        private String unite;
        private LocalDate dateRupture;
        private String impact; // PRODUCTION_BLOQUEE, PRODUCTION_REDUITE
    }
} 