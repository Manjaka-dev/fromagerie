package itu.fromagerie.fromagerie.dto.produit;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitSearchDTO {
    private Long id;
    private String nom;
    private BigDecimal poids;
    private BigDecimal prixVente;
    private BigDecimal prixRevient;
    private String ingredients;
    private String allergenes;
    private LocalDate datePeremption;
    private String categorieNom;
    private Integer quantiteDisponible;
} 