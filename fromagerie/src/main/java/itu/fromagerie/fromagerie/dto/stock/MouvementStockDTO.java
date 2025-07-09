package itu.fromagerie.fromagerie.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockDTO {
    private Long id;
    private Long matiereId;
    private String nomMatiere;
    private String typeMouvement; // "ENTREE", "SORTIE", "AJUSTEMENT"
    private BigDecimal quantite;
    private String dateMouvement;
    private String commentaire;
    private String lot = null;

}
