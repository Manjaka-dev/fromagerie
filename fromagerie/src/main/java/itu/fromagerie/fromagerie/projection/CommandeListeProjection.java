package itu.fromagerie.fromagerie.projection;

import java.time.LocalDate;

public interface CommandeListeProjection {
    Integer getId();
    String getNom();
    LocalDate getDateCommande();
    String getTelephone();
}
