package itu.fromagerie.fromagerie.entities.stock;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "alerte_peremption")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertePeremption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id")
    private MatierePremiere matiere;
    
    @Column(name = "date_peremption")
    private LocalDate datePeremption;
    
    @Column(name = "seuil_alerte")
    private Integer seuilAlerte;
}
