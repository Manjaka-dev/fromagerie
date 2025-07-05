package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "poids", precision = 5, scale = 2)
    private BigDecimal poids;

    @Column(name = "prix_vente", precision = 10, scale = 2)
    private BigDecimal prixVente;

    @Column(name = "prix_revient", precision = 10, scale = 2)
    private BigDecimal prixRevient;

    @Column(name = "ingredients", length = Integer.MAX_VALUE)
    private String ingredients;

    @Column(name = "allergenes", length = Integer.MAX_VALUE)
    private String allergenes;

    @Column(name = "date_peremption")
    private LocalDate datePeremption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private CategorieProduit categorie;

}