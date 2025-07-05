package itu.fromagerie.fromagerie.entities.production;

import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transformation_dechet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransformationDechet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id")
    private MatierePremiere matiere;
    
    @Column(name = "produit_final", length = 100)
    private String produitFinal;
    
    @Column(name = "quantite_transforme", precision = 10, scale = 2)
    private BigDecimal quantiteTransforme;
    
    @Column(name = "date_transformation")
    private LocalDate dateTransformation;
}
