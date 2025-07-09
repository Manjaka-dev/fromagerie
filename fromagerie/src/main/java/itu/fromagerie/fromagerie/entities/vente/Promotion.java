package itu.fromagerie.fromagerie.entities.vente;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Size(max = 100)
    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "reduction_pourcentage", precision = 5, scale = 2)
    private BigDecimal reductionPourcentage;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

}