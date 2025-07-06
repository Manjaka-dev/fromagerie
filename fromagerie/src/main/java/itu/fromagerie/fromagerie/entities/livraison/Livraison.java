package itu.fromagerie.fromagerie.entities.livraison;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "livraison")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livraison {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Commande commande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Livreur livreur;
    
    @Column(name = "date_livraison")
    private LocalDate dateLivraison;
    
    @Column(length = 100)
    private String zone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_livraison")
    private StatutLivraisonEnum statutLivraison = StatutLivraisonEnum.PLANIFIEE;
    
    @OneToMany(mappedBy = "livraison", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Éviter les références circulaires
    private List<LivraisonProduit> produitsALivrer;
    
    @OneToMany(mappedBy = "livraison", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Éviter les références circulaires
    private List<StatutLivraison> historiquesStatuts;
    
    @OneToOne(mappedBy = "livraison", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Éviter les références circulaires
    private ConfirmationReception confirmationReception;
    
    @OneToMany(mappedBy = "livraison", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Éviter les références circulaires
    private List<RetourLivraison> retours;
}
