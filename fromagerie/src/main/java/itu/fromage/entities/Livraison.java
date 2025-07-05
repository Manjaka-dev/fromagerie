package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "livraison", uniqueConstraints = {
    @UniqueConstraint(columnNames = "commande_id")
})
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id; // Ensure this is Integer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande; // Commande's id must also be Integer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @Column(name = "date_livraison")
    private LocalDate dateLivraison;

    @Column(name = "zone")
    private String zone;
}