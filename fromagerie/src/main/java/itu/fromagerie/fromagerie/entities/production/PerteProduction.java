package itu.fromagerie.fromagerie.entities.production;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "perte_production")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerteProduction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_id")
    private ProductionEffectuee production;
    
    @Column(name = "taux_perte", precision = 5, scale = 2)
    private BigDecimal tauxPerte;
    
    @Column(columnDefinition = "TEXT")
    private String raison;
}
