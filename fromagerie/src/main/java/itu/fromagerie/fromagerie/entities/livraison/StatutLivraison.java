package itu.fromagerie.fromagerie.entities.livraison;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "statut_livraison")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutLivraison {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;
    
    @Column(length = 30)
    private String statut;
    
    @Column(name = "date_statut")
    private LocalDateTime dateStatut = LocalDateTime.now();
    
    @Column(columnDefinition = "TEXT")
    private String commentaire;
}
