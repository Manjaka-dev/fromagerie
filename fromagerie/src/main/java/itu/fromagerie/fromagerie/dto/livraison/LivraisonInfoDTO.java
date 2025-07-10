package itu.fromagerie.fromagerie.dto.livraison;

import itu.fromagerie.fromagerie.entities.livraison.StatutLivraisonEnum;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class LivraisonInfoDTO {
    
    // Informations de base
    private Long id;
    private LocalDate dateLivraison;
    private String statut;
    private String zoneLivraison;
    private String adresseLivraison;
    
    // Informations commande
    private Long commandeId;
    private LocalDate dateCommande;
    
    // Informations client
    private Long clientId;
    private String nomClient;
    private String telephone;
    private String adresseClient;
    
    // Informations livreur
    private Long livreurId;
    private String nomLivreur;
    private String telephoneLivreur;
    
    // Produits à livrer
    private List<ProduitLivraisonDTO> produitsALivrer;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitLivraisonDTO {
        private Long produitId;
        private String nomProduit;
        private Integer quantiteALivrer;
        private Integer quantiteLivree;
        private String categorieProduit;
    }
}
