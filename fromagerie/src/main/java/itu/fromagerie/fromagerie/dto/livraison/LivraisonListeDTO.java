package itu.fromagerie.fromagerie.dto.livraison;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonListeDTO {
    private Long id;
    private LocalDate dateLivraison;
    private LocalDate dateCommande;
    private String numeroCommande;
    private String nomClient;
    private String lieu;
    private String statut;
    private String nomLivreur;
    private Integer nombreProduits;
}
