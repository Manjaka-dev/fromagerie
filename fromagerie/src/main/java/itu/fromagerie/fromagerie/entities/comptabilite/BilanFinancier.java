package itu.fromagerie.fromagerie.entities.comptabilite;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bilan_financier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BilanFinancier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate periode;
    
    @Column(name = "total_depenses", precision = 10, scale = 2)
    private BigDecimal totalDepenses;
    
    @Column(name = "total_revenus", precision = 10, scale = 2)
    private BigDecimal totalRevenus;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal profit;
}
