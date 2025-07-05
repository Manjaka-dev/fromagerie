package itu.fromage.projection;

import java.time.LocalDate;

public interface LivraisonProjection {
    Integer getLivraisonId();
    String getStatutLivraison();
    String getZone();
    Double getMontantTotal();
    Integer getLivreurId();
    String getLivreurNom();
    String getClientNom();
    String getClientTelephone();
    String getProduitsCommandes();
}