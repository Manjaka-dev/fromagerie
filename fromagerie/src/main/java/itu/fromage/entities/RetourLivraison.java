package itu.fromage.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "retour_livraison")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RetourLivraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Livraison livraison;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Produit produit;

    @Column(name = "quantite_retour")
    private Integer quantiteRetour;

    @Column(name = "raison")
    private String raison;
}
