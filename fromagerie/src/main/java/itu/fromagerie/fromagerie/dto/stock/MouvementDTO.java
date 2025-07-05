package itu.fromagerie.fromagerie.dto.stock;

import java.time.LocalDateTime;

public class MouvementDTO {
    public Long id;
    public String typeMouvement;
    public int quantite;
    public LocalDateTime dateMouvement;
    public String commentaire;
    public String lot;
} 