package itu.fromagerie.fromagerie.entities.livraison;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "confirmation_reception")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationReception {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Livraison livraison;
    
    @Column(columnDefinition = "TEXT")
    private String signature;
    
    @Column(name = "photo_reception", columnDefinition = "TEXT")
    private String photoReception;
}
