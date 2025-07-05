package itu.fromagerie.fromagerie.entities.produit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categorie_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieProduit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nom;
    
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produit> produits;
}
