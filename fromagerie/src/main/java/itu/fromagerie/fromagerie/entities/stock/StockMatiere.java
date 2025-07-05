package itu.fromagerie.fromagerie.entities.stock;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "stock_matiere")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMatiere {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id")
    private MatierePremiere matiere;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal quantite;
}
