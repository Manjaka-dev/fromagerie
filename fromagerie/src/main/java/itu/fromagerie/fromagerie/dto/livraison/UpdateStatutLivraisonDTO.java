package itu.fromagerie.fromagerie.dto.livraison;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatutLivraisonDTO {
    private String nouveauStatut;
    private String commentaire;
}
