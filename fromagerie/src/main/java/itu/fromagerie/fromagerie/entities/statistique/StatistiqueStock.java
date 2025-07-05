package itu.fromagerie.fromagerie.entities.statistique;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "statistique_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiqueStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @Column(name = "date_enregistrement")
    private LocalDate dateEnregistrement;
    
    private Integer quantite;
}
