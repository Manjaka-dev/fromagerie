package itu.fromagerie.fromagerie.entities.statistique;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "statistique_production")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiqueProduction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    private LocalDate periode;
    
    @Column(name = "quantite_produite")
    private Integer quantiteProduite;
}
