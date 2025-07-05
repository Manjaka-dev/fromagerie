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
public class CreateLivraisonDTO {
    
    private Long commandeId;
    private Long livreurId;
    private LocalDate dateLivraison;
    private String zone;
    private StatutLivraisonEnum statutLivraison = StatutLivraisonEnum.PLANIFIEE;
    private List<ProduitLivraisonCreateDTO> produitsALivrer;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitLivraisonCreateDTO {
        private Long produitId;
        private Integer quantiteALivrer;
    }
}
