package itu.fromagerie.fromagerie.entities.vente;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "commande")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    
    @Column(name = "date_commande")
    private LocalDate dateCommande;
    
    @Column(length = 30)
    private String statut; // en_attente, confirmée, annulée, livrée
    
    @Column(name = "montant_total", precision = 12, scale = 2)
    private BigDecimal montantTotal;
    
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Éviter les références circulaires
    private List<LigneCommande> lignesCommande;
    
    @OneToOne(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Éviter les références circulaires
    private Facture facture;
    
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Éviter les références circulaires
    private List<Paiement> paiements;
}
