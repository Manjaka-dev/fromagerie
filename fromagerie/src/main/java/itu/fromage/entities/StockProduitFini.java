package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stock_produit_fini")
public class StockProduitFini {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotProduit lot;

    @Column(name = "quantite")
    private Integer quantite;

}