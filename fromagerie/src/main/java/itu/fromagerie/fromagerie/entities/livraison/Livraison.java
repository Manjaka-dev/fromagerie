package itu.fromagerie.fromagerie.entities.livraison;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "livraison")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livraison {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;
    
    @Column(name = "date_livraison")
    private LocalDate dateLivraison;
    
    @Column(length = 100)
    private String zone;
    
    @OneToMany(mappedBy = "livraison", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatutLivraison> statuts;
    
    @OneToOne(mappedBy = "livraison", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ConfirmationReception confirmationReception;
    
    @OneToMany(mappedBy = "livraison", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RetourLivraison> retours;
    
    public String getStatut() {
        if (statuts != null && !statuts.isEmpty()) {
            // Return the most recent status
            return statuts.stream()
                    .sorted((s1, s2) -> s2.getDateStatut().compareTo(s1.getDateStatut()))
                    .findFirst()
                    .map(StatutLivraison::getStatut)
                    .orElse("En attente");
        }
        return "En attente";
}
