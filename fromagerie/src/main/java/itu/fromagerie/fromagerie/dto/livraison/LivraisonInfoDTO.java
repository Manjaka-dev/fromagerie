package itu.fromagerie.fromagerie.dto.livraison;

import itu.fromagerie.fromagerie.entities.livraison.StatutLivraisonEnum;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonInfoDTO {
    
    // Informations de base
    private Long livraisonId;
    private LocalDate dateLivraison;
    private StatutLivraisonEnum statut;
    private String zone;
    
    // Informations commande
    private Long commandeId;
    private LocalDate dateCommande;
    
    // Informations client
    private Long clientId;
    private String nomClient;
    private String telephoneClient;
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
