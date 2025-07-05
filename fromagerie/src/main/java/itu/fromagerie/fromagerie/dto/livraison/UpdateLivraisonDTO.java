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
public class UpdateLivraisonDTO {
    
    private Long livreurId;
    private LocalDate dateLivraison;
    private String zone;
    private StatutLivraisonEnum statutLivraison;
    private List<ProduitLivraisonUpdateDTO> produitsALivrer;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitLivraisonUpdateDTO {
        private Long id; // ID de LivraisonProduit
        private Long produitId;
        private Integer quantiteALivrer;
        private Integer quantiteLivree;
    }
}
