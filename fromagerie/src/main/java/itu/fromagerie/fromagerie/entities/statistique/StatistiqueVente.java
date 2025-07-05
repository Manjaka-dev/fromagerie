package itu.fromagerie.fromagerie.entities.statistique;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "statistique_vente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiqueVente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    private LocalDate periode;
    
    @Column(name = "quantite_vendue")
    private Integer quantiteVendue;
    
    @Column(name = "revenu_total", precision = 10, scale = 2)
    private BigDecimal revenuTotal;
}
