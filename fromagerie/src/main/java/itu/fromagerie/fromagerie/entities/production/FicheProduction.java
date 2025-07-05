package itu.fromagerie.fromagerie.entities.production;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "fiche_production")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FicheProduction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id")
    private MatierePremiere matiere;
    
    @Column(name = "quantite_necessaire", precision = 10, scale = 2)
    private BigDecimal quantiteNecessaire;
}
