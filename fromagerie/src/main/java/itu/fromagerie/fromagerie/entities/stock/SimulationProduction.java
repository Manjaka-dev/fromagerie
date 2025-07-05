package itu.fromagerie.fromagerie.entities.stock;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "simulation_production")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationProduction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @Column(name = "quantite_suggeree")
    private Integer quantiteSuggeree;
    
    @Column(name = "date_simulation")
    private LocalDateTime dateSimulation = LocalDateTime.now();
}
