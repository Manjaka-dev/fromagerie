package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ligne_commande")
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(name = "quantite")
    private Integer quantite;

}