package itu.fromage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "confirmation_reception")
public class ConfirmationReception {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;

    @Column(name = "signature", length = Integer.MAX_VALUE)
    private String signature;

    @Column(name = "photo_reception", length = Integer.MAX_VALUE)
    private String photoReception;

}