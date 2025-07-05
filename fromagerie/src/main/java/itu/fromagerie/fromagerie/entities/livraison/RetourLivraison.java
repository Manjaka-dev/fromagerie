package itu.fromagerie.fromagerie.entities.livraison;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "retour_livraison")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetourLivraison {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Livraison livraison;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @Column(name = "quantite_retour")
    private Integer quantiteRetour;
    
    @Column(columnDefinition = "TEXT")
    private String raison;
}
