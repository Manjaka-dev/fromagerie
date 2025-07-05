package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "adresse", length = Integer.MAX_VALUE)
    private String adresse;

}