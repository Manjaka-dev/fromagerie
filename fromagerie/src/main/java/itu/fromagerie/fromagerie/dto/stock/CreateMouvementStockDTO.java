package itu.fromagerie.fromagerie.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMouvementStockDTO {
    private Long matiereId;
    private String typeMouvement; // "ENTREE", "SORTIE", "AJUSTEMENT", "DECHET"
    private BigDecimal quantite;
    private String commentaire;
}
