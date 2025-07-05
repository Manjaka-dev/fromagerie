package itu.fromagerie.fromagerie.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {
    public BigDecimal beneficeJournalier;
    public BigDecimal beneficeMensuel;
    public BigDecimal beneficeAnnuel;
    public List<CAEvolutionDTO> evolutionCA;
    public List<LivraisonDTO> prochainesLivraisons;
    public ProductionDTO production;
    public PertesDTO pertes;
    public List<String> alertes;
} 