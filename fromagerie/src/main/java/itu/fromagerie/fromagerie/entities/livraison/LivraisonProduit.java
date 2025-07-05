package itu.fromagerie.fromagerie.entities.livraison;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "livraison_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonProduit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @Column(name = "quantite_a_livrer")
    private Integer quantiteALivrer;
    
    @Column(name = "quantite_livree")
    private Integer quantiteLivree = 0;
}
