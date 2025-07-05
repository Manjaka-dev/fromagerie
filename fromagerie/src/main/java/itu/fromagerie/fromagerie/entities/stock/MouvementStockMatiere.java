package itu.fromagerie.fromagerie.entities.stock;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvement_stock_matiere")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockMatiere {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id")
    private MatierePremiere matiere;
    
    @Column(name = "type_mouvement", length = 20)
    private String typeMouvement; // entree, sortie, ajustement
    
    @Column(precision = 10, scale = 2)
    private BigDecimal quantite;
    
    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement = LocalDateTime.now();
    
    @Column(columnDefinition = "TEXT")
    private String commentaire;
}
