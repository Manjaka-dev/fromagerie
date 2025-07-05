package itu.fromagerie.fromagerie.entities.produit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nom;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal poids;
    
    @Column(name = "prix_vente", precision = 10, scale = 2)
    private BigDecimal prixVente;
    
    @Column(name = "prix_revient", precision = 10, scale = 2)
    private BigDecimal prixRevient;
    
    @Column(columnDefinition = "TEXT")
    private String ingredients;
    
    @Column(columnDefinition = "TEXT")
    private String allergenes;
    
    @Column(name = "date_peremption")
    private LocalDate datePeremption;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private CategorieProduit categorie;
    
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Éviter les références circulaires
    private List<LotProduit> lots;
}
