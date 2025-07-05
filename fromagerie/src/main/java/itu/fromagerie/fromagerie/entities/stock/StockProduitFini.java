package itu.fromagerie.fromagerie.entities.stock;

import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "stock_produit_fini")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockProduitFini {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotProduit lot;
    
    private Integer quantite;
}
