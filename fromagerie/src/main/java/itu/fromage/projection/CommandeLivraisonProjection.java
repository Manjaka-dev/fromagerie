package itu.fromage.projection;

import java.time.LocalDate;

public interface CommandeLivraisonProjection {
    Integer getCommandeId();
    String getClientNom();
    String getProduitNom();
    Integer getPoids();
    Integer getQuantite();
    LocalDate getDateCommande();
    LocalDate getDateLivraison();
}