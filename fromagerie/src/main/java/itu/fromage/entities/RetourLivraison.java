package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "retour_livraison")
public class RetourLivraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(name = "quantite_retour")
    private Integer quantiteRetour;

    @Column(name = "raison")
    private String raison;
}
