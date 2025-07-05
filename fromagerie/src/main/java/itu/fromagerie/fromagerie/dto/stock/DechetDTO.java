package itu.fromagerie.fromagerie.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DechetDTO {
    private Long id;
    private Long matiereId;
    private String nomMatiere;
    private BigDecimal quantiteGaspillee;
    private String raisonDechet; // "PEREMPTION", "DETERIORATION", "ACCIDENT", "AUTRE"
    private LocalDate dateDechet;
    private String commentaire;
    private BigDecimal valeurPerdue;
}
