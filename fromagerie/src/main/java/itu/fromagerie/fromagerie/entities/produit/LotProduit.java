package itu.fromagerie.fromagerie.entities.produit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "lot_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotProduit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;
    
    @Column(name = "numero_lot", length = 50)
    private String numeroLot;
    
    @Column(name = "date_fabrication")
    private LocalDate dateFabrication;
    
    @Column(name = "date_peremption")
    private LocalDate datePeremption;
    
    private Integer quantite;
}
