package itu.fromagerie.fromagerie.dto.commande;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDTO {
    private Long id;
    private ClientDTO client;
    private LocalDate dateCommande;
    private String statut;
    private BigDecimal montantTotal;
    private List<LigneCommandeDTO> lignesCommande;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientDTO {
        private Long id;
        private String nom;
        private String telephone;
        private String adresse;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LigneCommandeDTO {
        private Long id;
        private ProduitDTO produit;
        private Integer quantite;
        private BigDecimal prixUnitaire;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitDTO {
        private Long id;
        private String nom;
        private BigDecimal prixVente;
        private BigDecimal poids;
        private String ingredients;
    }
} 