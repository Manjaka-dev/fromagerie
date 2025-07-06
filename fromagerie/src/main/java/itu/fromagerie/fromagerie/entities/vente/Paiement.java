package itu.fromagerie.fromagerie.entities.vente;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Commande commande;
    
    @Column(name = "date_paiement")
    private LocalDate datePaiement;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal montant;
    
    @Column(length = 50)
    private String methode;
}
