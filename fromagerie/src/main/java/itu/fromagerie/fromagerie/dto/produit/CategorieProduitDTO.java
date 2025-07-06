package itu.fromagerie.fromagerie.dto.produit;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieProduitDTO {
    private Long id;
    private String nom;
    private Integer nombreProduits;
} 