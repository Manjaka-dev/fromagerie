package itu.fromagerie.fromagerie.entities.comptabilite;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "depense")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Depense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 100)
    private String libelle;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal montant;
    
    @Column(name = "date_depense")
    private LocalDate dateDepense;
    
    @Column(length = 100)
    private String categorie;
}
