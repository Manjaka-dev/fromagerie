package itu.fromagerie.fromagerie.dto.livraison;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitLivraisonDTO {
    private Long produitId;
    private String nomProduit;
    private Integer quantite;
    private String description;
}
