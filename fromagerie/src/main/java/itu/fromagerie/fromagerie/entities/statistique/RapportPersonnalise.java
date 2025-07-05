package itu.fromagerie.fromagerie.entities.statistique;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rapport_personnalise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportPersonnalise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 100)
    private String nom;
    
    @Column(columnDefinition = "TEXT")
    private String contenu;
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();
}
