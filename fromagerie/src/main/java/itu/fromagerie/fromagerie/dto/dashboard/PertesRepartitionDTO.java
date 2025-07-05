package itu.fromagerie.fromagerie.dto.dashboard;

import java.math.BigDecimal;

public class PertesRepartitionDTO {
    public String type;
    public double pourcentage;
    public int cas;
    
    public PertesRepartitionDTO() {}
    
    public PertesRepartitionDTO(String type, Long cas, BigDecimal pourcentage) {
        this.type = type;
        this.cas = cas != null ? cas.intValue() : 0;
        this.pourcentage = pourcentage != null ? pourcentage.doubleValue() : 0.0;
    }
} 