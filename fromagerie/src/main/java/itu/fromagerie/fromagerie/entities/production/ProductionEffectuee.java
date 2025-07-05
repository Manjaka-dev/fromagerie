package itu.fromagerie.fromagerie.entities.production;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "production_effectuee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionEffectuee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @Column(name = "date_production")
    private LocalDate dateProduction;
    
    @Column(name = "quantite_produite")
    private Integer quantiteProduite;
    
    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PerteProduction> pertes;
}
