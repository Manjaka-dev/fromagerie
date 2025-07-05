package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "statut_livraison")
public class StatutLivraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;

    @Column(name = "statut", length = 30)
    private String statut;

    @Column(name = "date_statut")
    private Instant dateStatut;

}