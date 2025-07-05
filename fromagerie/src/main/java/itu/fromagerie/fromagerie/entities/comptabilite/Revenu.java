package itu.fromagerie.fromagerie.entities.comptabilite;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "revenu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Revenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal montant;
    
    @Column(name = "date_revenu")
    private LocalDate dateRevenu;
}
