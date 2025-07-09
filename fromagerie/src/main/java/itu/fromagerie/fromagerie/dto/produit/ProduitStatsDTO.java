package itu.fromagerie.fromagerie.dto.produit;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitStatsDTO {
    private List<ProduitPlusVenduDTO> produitsPlusVendus;
    private List<ProduitStockFaibleDTO> produitsStockFaible;
    private BigDecimal chiffreAffairesTotal;
    private Integer nombreTotalProduits;
    private Integer nombreProduitsEnRupture;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitPlusVenduDTO {
        private Long id;
        private String nom;
        private Integer quantiteVendue;
        private BigDecimal chiffreAffaires;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitStockFaibleDTO {
        private Long id;
        private String nom;
        private Integer quantiteDisponible;
        private Integer seuilAlerte;
    }
} 