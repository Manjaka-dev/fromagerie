package itu.fromagerie.fromagerie.dto.stock;

import java.time.LocalDate;

public class StockStatDTO {
    public LocalDate date;
    public int quantiteTotale;
    public boolean seuilCritique;
    public boolean suffisant;
    public int seuilCritiqueValeur;
    public int seuilDemandeValeur;
} 