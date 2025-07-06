package itu.fromagerie.fromagerie.service.statistique;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.statistique.StatistiqueProduction;
import itu.fromagerie.fromagerie.repository.statistique.StatistiqueProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatistiqueProductionService {

    @Autowired
    private StatistiqueProductionRepository statistiqueProductionRepository;

    /**
     * Récupère toutes les statistiques de production pour un produit
     */
    public List<StatistiqueProduction> getStatistiquesByProduit(Produit produit) {
        return statistiqueProductionRepository.findByProduit(produit);
    }

    /**
     * Récupère les statistiques de production sur une période
     */
    public List<StatistiqueProduction> getStatistiquesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return statistiqueProductionRepository.findByPeriodeBetween(dateDebut, dateFin);
    }

    /**
     * Calcule la quantité totale produite pour un produit
     */
    public Integer getTotalQuantiteProduiteByProduit(Produit produit) {
        return statistiqueProductionRepository.getTotalQuantiteProduiteByProduit(produit);
    }

    /**
     * Calcule la quantité totale produite sur une période
     */
    public Integer getTotalQuantiteProduiteBetween(LocalDate dateDebut, LocalDate dateFin) {
        return statistiqueProductionRepository.getTotalQuantiteProduiteBetween(dateDebut, dateFin);
    }

    // === Méthodes pour les 3 pourcentages généraux ===

    /**
     * Calcule le pourcentage de production journalière
     */
    public Map<String, Object> getPourcentageProductionJournaliere(LocalDate date, Integer capaciteJournaliere) {
        // TODO: Implémenter getPourcentageProductionJournaliere dans StatistiqueProductionRepository
        Map<String, Object> stats = new HashMap<>();
        stats.put("date", date);
        stats.put("quantiteProduite", 0);
        stats.put("pourcentageProduction", 0.0);
        return stats;
        
        // Code original commenté :
        // Object[] result = statistiqueProductionRepository.getPourcentageProductionJournaliere(date, capaciteJournaliere);
        // 
        // Map<String, Object> stats = new HashMap<>();
        // if (result != null && result.length >= 3) {
        //     stats.put("date", result[0]);
        //     stats.put("quantiteProduite", result[1]);
        //     stats.put("pourcentageProduction", result[2]);
        // }
        // 
        // return stats;
    }

    /**
     * Calcule le taux de qualité journalier
     */
    public Map<String, Object> getTauxQualiteJournalier(LocalDate date) {
        // TODO: Implémenter getTauxQualiteJournalier dans StatistiqueProductionRepository
        Map<String, Object> stats = new HashMap<>();
        stats.put("date", date);
        stats.put("quantiteTotale", 0);
        stats.put("quantitePerdue", 0);
        stats.put("tauxQualite", 100.0);
        return stats;
        
        // Code original commenté :
        // Object[] result = statistiqueProductionRepository.getTauxQualiteJournalier(date);
        // 
        // Map<String, Object> stats = new HashMap<>();
        // if (result != null && result.length >= 4) {
        //     stats.put("date", result[0]);
        //     stats.put("quantiteTotale", result[1]);
        //     stats.put("quantitePerdue", result[2]);
        //     stats.put("tauxQualite", result[3]);
        // }
        // 
        // return stats;
    }

    /**
     * Calcule le taux de perte journalier
     */
    public Map<String, Object> getTauxPerteJournalier(LocalDate date) {
        // TODO: Implémenter getTauxPerteJournalier dans StatistiqueProductionRepository
        Map<String, Object> stats = new HashMap<>();
        stats.put("date", date);
        stats.put("quantiteTotale", 0);
        stats.put("quantitePerdue", 0);
        stats.put("tauxPerte", 0.0);
        return stats;
        
        // Code original commenté :
        // Object[] result = statistiqueProductionRepository.getTauxPerteJournalier(date);
        // 
        // Map<String, Object> stats = new HashMap<>();
        // if (result != null && result.length >= 4) {
        //     stats.put("date", result[0]);
        //     stats.put("quantiteTotale", result[1]);
        //     stats.put("quantitePerdue", result[2]);
        //     stats.put("tauxPerte", result[3]);
        // }
        // 
        // return stats;
    }

    /**
     * Calcule les 3 pourcentages généraux pour une journée
     */
    public Map<String, Object> getStatistiquesCompletesJournalieres(LocalDate date, Integer capaciteJournaliere) {
        // TODO: Implémenter getStatistiquesCompletesJournalieres dans StatistiqueProductionRepository
        Map<String, Object> stats = new HashMap<>();
        stats.put("date", date);
        stats.put("quantiteProduite", 0);
        stats.put("pourcentageProduction", 0.0);
        stats.put("tauxQualite", 100.0);
        stats.put("tauxPerte", 0.0);
        return stats;
        
        // Code original commenté :
        // Object[] result = statistiqueProductionRepository.getStatistiquesCompletesJournalieres(date, capaciteJournaliere);
        // 
        // Map<String, Object> stats = new HashMap<>();
        // if (result != null && result.length >= 5) {
        //     stats.put("date", result[0]);
        //     stats.put("quantiteProduite", result[1]);
        //     stats.put("pourcentageProduction", result[2]);
        //     stats.put("tauxQualite", result[3]);
        //     stats.put("tauxPerte", result[4]);
        // }
        // 
        // return stats;
    }

    /**
     * Calcule les statistiques sur une période
     */
    public List<Object[]> getStatistiquesSurPeriode(LocalDate dateDebut, LocalDate dateFin, Integer capaciteJournaliere) {
        // TODO: Implémenter getStatistiquesSurPeriode dans StatistiqueProductionRepository
        return new ArrayList<>();
        
        // Code original commenté :
        // return statistiqueProductionRepository.getStatistiquesSurPeriode(dateDebut, dateFin, capaciteJournaliere);
    }

    // === Méthodes pour la capacité journalière ===

    /**
     * Calcule la moyenne des commandes par semaine
     */
    public Double getMoyenneCommandesParSemaine(LocalDate dateDebut, LocalDate dateFin) {
        // TODO: Implémenter getMoyenneCommandesParSemaine dans StatistiqueProductionRepository
        return 0.0;
        
        // Code original commenté :
        // return statistiqueProductionRepository.getMoyenneCommandesParSemaine(dateDebut, dateFin);
    }

    /**
     * Calcule la capacité journalière basée sur les commandes
     */
    public Integer getCapaciteJournaliere(LocalDate dateDebut, LocalDate dateFin) {
        // TODO: Implémenter getCapaciteJournaliere dans StatistiqueProductionRepository
        return 100;
        
        // Code original commenté :
        // return statistiqueProductionRepository.getCapaciteJournaliere(dateDebut, dateFin);
    }

    /**
     * Calcule la capacité journalière (version PostgreSQL)
     */
    public Integer getCapaciteJournaliereNative(LocalDate dateDebut, LocalDate dateFin) {
        // TODO: Implémenter getCapaciteJournaliereNative dans StatistiqueProductionRepository
        return 100;
        
        // Code original commenté :
        // return statistiqueProductionRepository.getCapaciteJournaliereNative(dateDebut, dateFin);
    }

    /**
     * Récupère les statistiques détaillées par semaine
     */
    public List<Object[]> getStatistiquesCommandesParSemaine(LocalDate dateDebut, LocalDate dateFin) {
        // TODO: Implémenter getStatistiquesCommandesParSemaine dans StatistiqueProductionRepository
        return new ArrayList<>();
        
        // Code original commenté :
        // return statistiqueProductionRepository.getStatistiquesCommandesParSemaine(dateDebut, dateFin);
    }

    // === Méthodes utilitaires ===

    /**
     * Calcule automatiquement la capacité journalière et les statistiques pour aujourd'hui
     */
    public Map<String, Object> getStatistiquesAujourdhui() {
        LocalDate aujourdhui = LocalDate.now();
        LocalDate dateDebut = aujourdhui.minusMonths(3); // 3 mois d'historique
        LocalDate dateFin = aujourdhui;
        
        // Calculer la capacité journalière
        Integer capaciteJournaliere = getCapaciteJournaliere(dateDebut, dateFin);
        if (capaciteJournaliere == null || capaciteJournaliere == 0) {
            capaciteJournaliere = 100; // Valeur par défaut
        }
        
        // Récupérer les statistiques complètes
        return getStatistiquesCompletesJournalieres(aujourdhui, capaciteJournaliere);
    }

    /**
     * Calcule les statistiques pour une période avec capacité automatique
     */
    public List<Object[]> getStatistiquesPeriodeAvecCapaciteAuto(LocalDate dateDebut, LocalDate dateFin) {
        // Calculer la capacité journalière sur une période plus large
        LocalDate dateDebutCapacite = dateDebut.minusMonths(3);
        Integer capaciteJournaliere = getCapaciteJournaliere(dateDebutCapacite, dateFin);
        if (capaciteJournaliere == null || capaciteJournaliere == 0) {
            capaciteJournaliere = 100; // Valeur par défaut
        }
        
        return getStatistiquesSurPeriode(dateDebut, dateFin, capaciteJournaliere);
    }

    /**
     * Formate les statistiques pour l'affichage
     */
    public Map<String, Object> formaterStatistiques(Map<String, Object> stats) {
        Map<String, Object> formate = new HashMap<>();
        
        if (stats.containsKey("pourcentageProduction")) {
            Double pourcentage = (Double) stats.get("pourcentageProduction");
            formate.put("pourcentageProduction", String.format("%.2f%%", pourcentage));
        }
        
        if (stats.containsKey("tauxQualite")) {
            Double taux = (Double) stats.get("tauxQualite");
            formate.put("tauxQualite", String.format("%.2f%%", taux));
        }
        
        if (stats.containsKey("tauxPerte")) {
            Double taux = (Double) stats.get("tauxPerte");
            formate.put("tauxPerte", String.format("%.2f%%", taux));
        }
        
        // Copier les autres valeurs
        stats.forEach((key, value) -> {
            if (!formate.containsKey(key)) {
                formate.put(key, value);
            }
        });
        
        return formate;
    }
} 