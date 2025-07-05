package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "livreur")
public class Livreur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "telephone", length = 20)
    private String telephone;

}