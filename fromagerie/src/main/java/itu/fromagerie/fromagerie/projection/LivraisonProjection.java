package itu.fromagerie.fromagerie.projection;

import java.time.LocalDate;

public interface LivraisonProjection {
    Long getLivraisonId();
    String getStatutLivraison();
    String getZone();
    Double getMontantTotal();
    Long getLivreurId();
    String getLivreurNom();
    String getClientNom();
    String getClientTelephone();
    String getProduitsCommandes();
}