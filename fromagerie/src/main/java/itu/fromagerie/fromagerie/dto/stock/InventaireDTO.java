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
public class InventaireDTO {
    private Long id;
    private LocalDate dateInventaire;
    private String statut; // EN_COURS, TERMINE, VALIDE
    private List<LigneInventaireDTO> lignes;
    private BigDecimal valeurTotale;
    private String commentaire;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LigneInventaireDTO {
        private Long matiereId;
        private String nomMatiere;
        private BigDecimal quantiteTheorique;
        private BigDecimal quantiteReelle;
        private BigDecimal ecart;
        private String unite;
        private String commentaire;
    }
} 