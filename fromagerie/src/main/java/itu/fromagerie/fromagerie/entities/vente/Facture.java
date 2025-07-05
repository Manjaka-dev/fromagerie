package itu.fromagerie.fromagerie.entities.vente;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "facture")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facture {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande;
    
    @Column(name = "montant_total", precision = 10, scale = 2)
    private BigDecimal montantTotal;
    
    @Column(name = "date_facture")
    private LocalDate dateFacture;
}
