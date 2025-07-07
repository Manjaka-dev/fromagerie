package itu.fromagerie.fromagerie.entities.utilisateur;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "journal_connexion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalConnexion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    @Column(name = "date_connexion")
    private LocalDateTime dateConnexion = LocalDateTime.now();
    
    @Column(columnDefinition = "TEXT")
    private String action;
    
    // Getters et setters manuels si Lombok ne fonctionne pas
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    
    public LocalDateTime getDateConnexion() { return dateConnexion; }
    public void setDateConnexion(LocalDateTime dateConnexion) { this.dateConnexion = dateConnexion; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
