package itu.fromagerie.fromagerie.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO pour créer ou mettre à jour une fiche de production.
 * Contient toutes les informations nécessaires pour créer un produit et sa fiche associée.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FicheProductionDTO {
    // Informations du produit
    private String nomProduit;
    private String categorie;
    private String poids;
    private String prixRevient;
    private String prixVente;
    private String ingredients;
    private String allergenes;
    private String dateExpiration;
    private String dateCreation;
    
    // Informations spécifiques à la fiche
    private BigDecimal quantiteNecessaire;
}
