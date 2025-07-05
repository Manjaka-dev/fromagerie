package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "mouvement_stock_produit")
public class MouvementStockProduit {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotProduit lot;

    @Column(name = "type_mouvement", length = 20)
    private String typeMouvement;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "date_mouvement")
    private Instant dateMouvement;

    @Column(name = "commentaire", length = Integer.MAX_VALUE)
    private String commentaire;

}