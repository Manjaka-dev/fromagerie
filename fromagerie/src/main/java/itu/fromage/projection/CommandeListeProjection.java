package itu.fromage.projection;

import java.time.LocalDate;

public interface CommandeListeProjection {
    Integer getId();
    String getNom();
    LocalDate getDateCommande();
    String getTelephone();
}
