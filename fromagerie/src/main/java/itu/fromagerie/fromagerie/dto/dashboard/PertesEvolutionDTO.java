package itu.fromagerie.fromagerie.dto.dashboard;

import java.math.BigDecimal;

public class PertesEvolutionDTO {
    public String jour;
    public double valeur;
    
    public PertesEvolutionDTO() {}
    
    public PertesEvolutionDTO(String jour, BigDecimal valeur) {
        this.jour = jour;
        this.valeur = valeur != null ? valeur.doubleValue() : 0.0;
    }
} 