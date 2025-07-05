package itu.fromagerie.fromagerie.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertePeremptionDTO {
    private Long id;
    private Long matiereId;
    private String nomMatiere;
    private LocalDate datePeremption;
    private Integer seuilAlerte; // nombre de jours avant péremption pour déclencher l'alerte
    private Integer joursRestants;
    private String niveauAlerte; // "CRITIQUE", "ATTENTION", "NORMAL"
}
