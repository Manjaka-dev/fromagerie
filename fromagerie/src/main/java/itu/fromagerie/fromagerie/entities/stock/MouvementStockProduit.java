package itu.fromagerie.fromagerie.entities.stock;

import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mouvement_stock_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockProduit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotProduit lot;
    
    @Column(name = "type_mouvement", length = 20)
    private String typeMouvement; // entree, sortie, ajustement
    
    private Integer quantite;
    
    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement = LocalDateTime.now();
    
    @Column(columnDefinition = "TEXT")
    private String commentaire;
}
