package itu.fromagerie.fromagerie.projection;

import java.time.LocalDate;

public interface CommandeLivraisonProjection {
    Long getCommandeId();
    String getClientNom();
    String getProduitNom();
    Integer getPoids();
    Integer getQuantite();
    LocalDate getDateCommande();
    LocalDate getDateLivraison();
}