package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "lot_produit")
public class LotProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(name = "numero_lot", length = 50)
    private String numeroLot;

    @Column(name = "date_fabrication")
    private LocalDate dateFabrication;

    @Column(name = "date_peremption")
    private LocalDate datePeremption;

    @Column(name = "quantite")
    private Integer quantite;

}