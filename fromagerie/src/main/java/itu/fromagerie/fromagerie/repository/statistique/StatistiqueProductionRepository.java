package itu.fromagerie.fromagerie.repository.statistique;

import itu.fromagerie.fromagerie.entities.statistique.StatistiqueProduction;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatistiqueProductionRepository extends JpaRepository<StatistiqueProduction, Long> {
    List<StatistiqueProduction> findByProduit(Produit produit);
    List<StatistiqueProduction> findByPeriodeBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT SUM(s.quantiteProduite) FROM StatistiqueProduction s WHERE s.produit = :produit")
    Integer getTotalQuantiteProduiteByProduit(Produit produit);
    
    @Query("SELECT SUM(s.quantiteProduite) FROM StatistiqueProduction s WHERE s.periode BETWEEN :dateDebut AND :dateFin")
    Integer getTotalQuantiteProduiteBetween(LocalDate dateDebut, LocalDate dateFin);
        
    // 1. Pourcentage de production journalière (par rapport à une capacité cible)
    @Query("SELECT pe.dateProduction as date, " +
           "SUM(pe.quantiteProduite) as quantiteProduite, " +
           "(:capaciteJournaliere * 100.0 / SUM(pe.quantiteProduite)) as pourcentageProduction " +
           "FROM ProductionEffectuee pe " +
           "WHERE pe.dateProduction = :date " +
           "GROUP BY pe.dateProduction")
    Object[] getPourcentageProductionJournaliere(@Param("date") LocalDate date, 
                                                @Param("capaciteJournaliere") Integer capaciteJournaliere);
    
    // 2. Taux de qualité total en une journée
    @Query("SELECT pe.dateProduction as date, " +
           "SUM(pe.quantiteProduite) as quantiteTotale, " +
           "COALESCE(SUM(pp.tauxPerte * pe.quantiteProduite / 100), 0) as quantitePerdue, " +
           "CASE WHEN SUM(pe.quantiteProduite) > 0 THEN " +
           "   (SUM(pe.quantiteProduite) - COALESCE(SUM(pp.tauxPerte * pe.quantiteProduite / 100), 0)) * 100.0 / SUM(pe.quantiteProduite) " +
           "ELSE 0 END as tauxQualite " +
           "FROM ProductionEffectuee pe " +
           "LEFT JOIN pe.pertes pp " +
           "WHERE pe.dateProduction = :date " +
           "GROUP BY pe.dateProduction")
    Object[] getTauxQualiteJournalier(@Param("date") LocalDate date);
    
    // 3. Taux de perte total en une journée
    @Query("SELECT pe.dateProduction as date, " +
           "SUM(pe.quantiteProduite) as quantiteTotale, " +
           "COALESCE(SUM(pp.tauxPerte * pe.quantiteProduite / 100), 0) as quantitePerdue, " +
           "CASE WHEN SUM(pe.quantiteProduite) > 0 THEN " +
           "   COALESCE(SUM(pp.tauxPerte * pe.quantiteProduite / 100), 0) * 100.0 / SUM(pe.quantiteProduite) " +
           "ELSE 0 END as tauxPerte " +
           "FROM ProductionEffectuee pe " +
           "LEFT JOIN pe.pertes pp " +
           "WHERE pe.dateProduction = :date " +
           "GROUP BY pe.dateProduction")
    Object[] getTauxPerteJournalier(@Param("date") LocalDate date);
    
    // Statistiques complètes pour une journée (les 3 pourcentages ensemble)
    @Query("SELECT pe.dateProduction as date, " +
           "SUM(pe.quantiteProduite) as quantiteProduite, " +
           "(:capaciteJournaliere * 100.0 / SUM(pe.quantiteProduite)) as pourcentageProduction, " +
           "CASE WHEN SUM(pe.quantiteProduite) > 0 THEN " +
           "   (SUM(pe.quantiteProduite) - COALESCE(SUM(pp.tauxPerte * pe.quantiteProduite / 100), 0)) * 100.0 / SUM(pe.quantiteProduite) " +
           "ELSE 0 END as tauxQualite, " +
           "CASE WHEN SUM(pe.quantiteProduite) > 0 THEN " +
           "   COALESCE(SUM(pp.tauxPerte * pe.quantiteProduite / 100), 0) * 100.0 / SUM(pe.quantiteProduite) " +
           "ELSE 0 END as tauxPerte " +
           "FROM ProductionEffectuee pe " +
           "LEFT JOIN pe.pertes pp " +
           "WHERE pe.dateProduction = :date " +
           "GROUP BY pe.dateProduction")
    Object[] getStatistiquesCompletesJournalieres(@Param("date") LocalDate date, 
                                                 @Param("capaciteJournaliere") Integer capaciteJournaliere);
    
    // Statistiques sur une période (moyennes des 3 pourcentages)
    @Query("SELECT pe.dateProduction as date, " +
           "SUM(pe.quantiteProduite) as quantiteProduite, " +
           "(:capaciteJournaliere * 100.0 / SUM(pe.quantiteProduite)) as pourcentageProduction, " +
           "CASE WHEN SUM(pe.quantiteProduite) > 0 THEN " +
           "   (SUM(pe.quantiteProduite) - COALESCE(SUM(pp.tauxPerte * pe.quantiteProduite / 100), 0)) * 100.0 / SUM(pe.quantiteProduite) " +
           "ELSE 0 END as tauxQualite, " +
           "CASE WHEN SUM(pe.quantiteProduite) > 0 THEN " +
           "   COALESCE(SUM(pp.tauxPerte * pe.quantiteProduite / 100), 0) * 100.0 / SUM(pe.quantiteProduite) " +
           "ELSE 0 END as tauxPerte " +
           "FROM ProductionEffectuee pe " +
           "LEFT JOIN pe.pertes pp " +
           "WHERE pe.dateProduction BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY pe.dateProduction " +
           "ORDER BY pe.dateProduction DESC")
    List<Object[]> getStatistiquesSurPeriode(@Param("dateDebut") LocalDate dateDebut, 
                                            @Param("dateFin") LocalDate dateFin, 
                                            @Param("capaciteJournaliere") Integer capaciteJournaliere);

    // Calcul de la moyenne des commandes par semaine (capacité journalière)
    @Query("SELECT AVG(quantiteTotaleParSemaine) as moyenneCommandesParSemaine " +
           "FROM (SELECT YEARWEEK(c.dateCommande) as semaine, " +
           "             SUM(lc.quantite) as quantiteTotaleParSemaine " +
           "      FROM Commande c " +
           "      JOIN c.lignesCommande lc " +
           "      WHERE c.dateCommande BETWEEN :dateDebut AND :dateFin " +
           "        AND c.statut IN ('confirmée', 'livrée') " +
           "      GROUP BY YEARWEEK(c.dateCommande)) as sous_requete")
    Double getMoyenneCommandesParSemaine(@Param("dateDebut") LocalDate dateDebut, 
                                         @Param("dateFin") LocalDate dateFin);
    
    // Calcul de la capacité journalière basée sur la moyenne hebdomadaire
    @Query("SELECT ROUND(AVG(quantiteTotaleParSemaine) / 7, 0) as capaciteJournaliere " +
           "FROM (SELECT YEARWEEK(c.dateCommande) as semaine, " +
           "             SUM(lc.quantite) as quantiteTotaleParSemaine " +
           "      FROM Commande c " +
           "      JOIN c.lignesCommande lc " +
           "      WHERE c.dateCommande BETWEEN :dateDebut AND :dateFin " +
           "        AND c.statut IN ('confirmée', 'livrée') " +
           "      GROUP BY YEARWEEK(c.dateCommande)) as sous_requete")
    Integer getCapaciteJournaliere(@Param("dateDebut") LocalDate dateDebut, 
                                   @Param("dateFin") LocalDate dateFin);
    
    // Version alternative avec EXTRACT pour PostgreSQL
    @Query(value = "SELECT ROUND(AVG(quantite_totale_semaine) / 7, 0) as capacite_journaliere " +
           "FROM (SELECT EXTRACT(YEAR FROM c.date_commande) * 100 + EXTRACT(WEEK FROM c.date_commande) as semaine, " +
           "             SUM(lc.quantite) as quantite_totale_semaine " +
           "      FROM commande c " +
           "      JOIN ligne_commande lc ON c.id = lc.commande_id " +
           "      WHERE c.date_commande BETWEEN :dateDebut AND :dateFin " +
           "        AND c.statut IN ('confirmée', 'livrée') " +
           "      GROUP BY EXTRACT(YEAR FROM c.date_commande) * 100 + EXTRACT(WEEK FROM c.date_commande)) as sous_requete", 
           nativeQuery = true)
    Integer getCapaciteJournaliereNative(@Param("dateDebut") LocalDate dateDebut, 
                                         @Param("dateFin") LocalDate dateFin);
    
    // Statistiques détaillées par semaine
    @Query("SELECT YEARWEEK(c.dateCommande) as semaine, " +
           "       COUNT(DISTINCT c.id) as nombreCommandes, " +
           "       SUM(lc.quantite) as quantiteTotale, " +
           "       ROUND(SUM(lc.quantite) / 7, 0) as moyenneJournaliere " +
           "FROM Commande c " +
           "JOIN c.lignesCommande lc " +
           "WHERE c.dateCommande BETWEEN :dateDebut AND :dateFin " +
           "  AND c.statut IN ('confirmée', 'livrée') " +
           "GROUP BY YEARWEEK(c.dateCommande) " +
           "ORDER BY semaine DESC")
    List<Object[]> getStatistiquesCommandesParSemaine(@Param("dateDebut") LocalDate dateDebut, 
                                                     @Param("dateFin") LocalDate dateFin);
}
