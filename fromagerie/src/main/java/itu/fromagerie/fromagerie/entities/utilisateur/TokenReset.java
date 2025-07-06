package itu.fromagerie.fromagerie.entities.utilisateur;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "token_reset")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenReset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    @Column(unique = true, length = 255)
    private String token;
    
    @Column(name = "date_expiration")
    private LocalDateTime dateExpiration;
    
    @Column(name = "utilise")
    private boolean utilise = false;
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();
} 