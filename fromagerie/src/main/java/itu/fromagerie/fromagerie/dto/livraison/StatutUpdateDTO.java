package itu.fromagerie.fromagerie.dto.livraison;

import itu.fromagerie.fromagerie.entities.livraison.StatutLivraisonEnum;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutUpdateDTO {
    private StatutLivraisonEnum statut;
    private String commentaire;
}
