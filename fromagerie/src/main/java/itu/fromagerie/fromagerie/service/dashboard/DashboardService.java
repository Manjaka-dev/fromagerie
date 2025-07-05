package itu.fromagerie.fromagerie.service.dashboard;

import itu.fromagerie.fromagerie.dto.dashboard.*;
import itu.fromagerie.fromagerie.repository.comptabilite.BilanFinancierRepository;
import itu.fromagerie.fromagerie.repository.comptabilite.RevenuRepository;
import itu.fromagerie.fromagerie.repository.vente.FactureRepository;
import itu.fromagerie.fromagerie.repository.production.ProductionEffectueeRepository;
import itu.fromagerie.fromagerie.repository.production.PerteProductionRepository;
import itu.fromagerie.fromagerie.repository.livraison.LivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired BilanFinancierRepository bilanRepo;
    @Autowired RevenuRepository revenuRepo;
    @Autowired FactureRepository factureRepo;
    @Autowired ProductionEffectueeRepository prodRepo;
    @Autowired PerteProductionRepository perteRepo;
    @Autowired LivraisonRepository livraisonRepo;

    public DashboardDTO getDashboard() {
        DashboardDTO dto = new DashboardDTO();
        LocalDate today = LocalDate.now();
        LocalDate startMonth = today.withDayOfMonth(1);
        LocalDate startYear = today.withDayOfYear(1);

        // Bénéfices
        dto.beneficeJournalier = bilanRepo.getTotalProfitBetween(today, today);
        dto.beneficeMensuel = bilanRepo.getTotalProfitBetween(startMonth, today);
        dto.beneficeAnnuel = bilanRepo.getTotalProfitBetween(startYear, today);

        // Evolution CA (exemple sur 12 mois)
        List<CAEvolutionDTO> caList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LocalDate month = today.minusMonths(i).withDayOfMonth(1);
            BigDecimal montant = factureRepo.getTotalMontantBetween(month, month.plusMonths(1).minusDays(1));
            CAEvolutionDTO ca = new CAEvolutionDTO();
            ca.periode = month;
            ca.montant = montant != null ? montant : BigDecimal.ZERO;
            caList.add(ca);
        }
        dto.evolutionCA = caList;

        // Prochaines livraisons
        dto.prochainesLivraisons = new ArrayList<>();
        livraisonRepo.findProchainesLivraisons().forEach(l -> {
            LivraisonDTO liv = new LivraisonDTO();
            liv.client = l.getCommande().getClient().getNom();
            liv.dateLivraison = l.getDateLivraison().toString();
            liv.statut = l.getStatut();
            liv.quantite = l.getCommande().getLignesCommande().stream().mapToInt(lc -> lc.getQuantite()).sum();
            dto.prochainesLivraisons.add(liv);
        });

        // Production
        ProductionDTO prod = new ProductionDTO();
        LocalDate weekStart = today.minusDays(6);
        Integer prodHebdo = prodRepo.getTotalProductionBetween(weekStart, today);
        prod.prodHebdo = prodHebdo != null ? prodHebdo : 0;
        Double tauxQualite = prodRepo.getTauxQualiteMoyen(weekStart, today);
        prod.tauxQualite = tauxQualite != null ? tauxQualite : 0;
        prod.livraisonsPlanifiees = dto.prochainesLivraisons.size();
        dto.production = prod;

        // Pertes
        PertesDTO pertes = new PertesDTO();
        BigDecimal totalPertes = perteRepo.getAverageTauxPerte();
        pertes.total = totalPertes != null ? totalPertes.doubleValue() : 0;
        pertes.taux = pertes.total; // à adapter selon besoin réel
        pertes.moyenneJournaliere = pertes.total / 7.0;
        pertes.evolution = mapEvolutionPertes(perteRepo.getEvolutionPertesParJour(weekStart, today));
        pertes.repartition = mapRepartitionPertes(perteRepo.getRepartitionPertes(weekStart, today));
        dto.pertes = pertes;

        // Alertes
        dto.alertes = new ArrayList<>();
        if (pertes.taux > 20) {
            dto.alertes.add("Attention: Augmentation des pertes détectée");
        }

        return dto;
    }

    public DashboardDTO getDashboardFiltered(LocalDate dateDebut, LocalDate dateFin, Long clientId, Long produitId) {
        LocalDate fin = dateFin != null ? dateFin : LocalDate.now();
        LocalDate debut = dateDebut != null ? dateDebut : fin.minusDays(6);

        DashboardDTO dto = new DashboardDTO();
        // Bénéfices sur la période filtrée
        dto.beneficeJournalier = bilanRepo.getTotalProfitBetween(fin, fin);
        dto.beneficeMensuel = bilanRepo.getTotalProfitBetween(fin.withDayOfMonth(1), fin);
        dto.beneficeAnnuel = bilanRepo.getTotalProfitBetween(fin.withDayOfYear(1), fin);

        // Evolution CA sur la période (par jour)
        List<CAEvolutionDTO> caList = new ArrayList<>();
        LocalDate cursor = debut;
        while (!cursor.isAfter(fin)) {
            BigDecimal montant = factureRepo.getTotalMontantBetween(cursor, cursor);
            CAEvolutionDTO ca = new CAEvolutionDTO();
            ca.periode = cursor;
            ca.montant = montant != null ? montant : BigDecimal.ZERO;
            caList.add(ca);
            cursor = cursor.plusDays(1);
        }
        dto.evolutionCA = caList;

        // Prochaines livraisons (à partir de la date de fin)
        dto.prochainesLivraisons = new ArrayList<>();
        livraisonRepo.findProchainesLivraisons().forEach(l -> {
            LivraisonDTO liv = new LivraisonDTO();
            liv.client = l.getCommande().getClient().getNom();
            liv.dateLivraison = l.getDateLivraison().toString();
            liv.statut = l.getStatut();
            liv.quantite = l.getCommande().getLignesCommande().stream().mapToInt(lc -> lc.getQuantite()).sum();
            dto.prochainesLivraisons.add(liv);
        });

        // Production sur la période
        ProductionDTO prod = new ProductionDTO();
        Integer prodHebdo = prodRepo.getTotalProductionBetween(debut, fin);
        prod.prodHebdo = prodHebdo != null ? prodHebdo : 0;
        Double tauxQualite = prodRepo.getTauxQualiteMoyen(debut, fin);
        prod.tauxQualite = tauxQualite != null ? tauxQualite : 0;
        prod.livraisonsPlanifiees = dto.prochainesLivraisons.size();
        dto.production = prod;

        // Pertes sur la période
        PertesDTO pertes = new PertesDTO();
        BigDecimal totalPertes = perteRepo.getAverageTauxPerte();
        pertes.total = totalPertes != null ? totalPertes.doubleValue() : 0;
        pertes.taux = pertes.total; // à adapter
        pertes.moyenneJournaliere = pertes.total / ((double)(fin.toEpochDay() - debut.toEpochDay() + 1));
        pertes.evolution = mapEvolutionPertes(perteRepo.getEvolutionPertesParJour(debut, fin));
        pertes.repartition = mapRepartitionPertes(perteRepo.getRepartitionPertes(debut, fin));
        dto.pertes = pertes;

        // Alertes
        dto.alertes = new ArrayList<>();
        if (pertes.taux > 20) {
            dto.alertes.add("Attention: Augmentation des pertes détectée");
        }

        return dto;
    }
    
    private List<PertesEvolutionDTO> mapEvolutionPertes(List<Object[]> results) {
        List<PertesEvolutionDTO> evolution = new ArrayList<>();
        for (Object[] row : results) {
            PertesEvolutionDTO dto = new PertesEvolutionDTO();
            dto.jour = row[0] != null ? row[0].toString() : "";
            dto.valeur = row[1] instanceof BigDecimal ? ((BigDecimal) row[1]).doubleValue() : 0.0;
            evolution.add(dto);
        }
        return evolution;
    }
    
    private List<PertesRepartitionDTO> mapRepartitionPertes(List<Object[]> results) {
        List<PertesRepartitionDTO> repartition = new ArrayList<>();
        for (Object[] row : results) {
            PertesRepartitionDTO dto = new PertesRepartitionDTO();
            dto.type = row[0] != null ? row[0].toString() : "";
            dto.cas = row[1] instanceof Long ? ((Long) row[1]).intValue() : 0;
            dto.pourcentage = row[2] instanceof BigDecimal ? ((BigDecimal) row[2]).doubleValue() : 0.0;
            repartition.add(dto);
        }
        return repartition;
    }
} 