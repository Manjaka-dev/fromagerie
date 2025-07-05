package itu.fromagerie.fromagerie.entities.stock;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "matiere_premiere")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatierePremiere {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 100)
    private String nom;
    
    @Column(length = 20)
    private String unite;
    
    @Column(name = "duree_conservation")
    private Integer dureeConservation; // en jours
}
