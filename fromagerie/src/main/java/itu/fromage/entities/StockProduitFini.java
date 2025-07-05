package itu.fromage.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stock_produit_fini")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StockProduitFini {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private LotProduit lot;

    @Column(name = "quantite")
    private Integer quantite;

}