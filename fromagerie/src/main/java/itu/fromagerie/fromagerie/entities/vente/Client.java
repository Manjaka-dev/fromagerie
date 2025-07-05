package itu.fromagerie.fromagerie.entities.vente;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 100)
    private String nom;
    
    @Column(length = 20)
    private String telephone;
    
    @Column(columnDefinition = "TEXT")
    private String adresse;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Commande> commandes;
}
