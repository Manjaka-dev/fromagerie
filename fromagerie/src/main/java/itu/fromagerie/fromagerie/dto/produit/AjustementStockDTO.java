package itu.fromagerie.fromagerie.dto.produit;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjustementStockDTO {
    private Integer quantite;
    private String raison;
    private String type; // "ajout" ou "retrait"
} 