package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.*;
import itu.fromagerie.fromagerie.entities.stock.*;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.repository.stock.*;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockMatiereService {

    @Autowired
    private MatierePremiereRepository matierePremiereRepository;
    
    @Autowired
    private StockMatiereRepository stockMatiereRepository;
    
    @Autowired
    private MouvementStockMatiereRepository mouvementStockMatiereRepository;
    
    @Autowired
    private AlertePeremptionRepository alertePeremptionRepository;
    
    @Autowired
    private ProduitRepository produitRepository;

    // ==================== GESTION MATIERES PREMIERES ====================
    
    /**
     * Créer une nouvelle matière première avec stock initial
     */
    public MatierePremiereDTO createMatierePremiere(CreateMatierePremiereDTO createDTO) {
        // Créer la matière première
        MatierePremiere matiere = new MatierePremiere();
        matiere.setNom(createDTO.getNom());
        matiere.setUnite(createDTO.getUnite());
        matiere.setDureeConservation(createDTO.getDureeConservation());
        
        MatierePremiere savedMatiere = matierePremiereRepository.save(matiere);
        
        // Créer le stock initial si quantité fournie
        if (createDTO.getQuantiteInitiale() != null && createDTO.getQuantiteInitiale().compareTo(BigDecimal.ZERO) > 0) {
            StockMatiere stock = new StockMatiere();
            stock.setMatiere(savedMatiere);
            stock.setQuantite(createDTO.getQuantiteInitiale());
            stockMatiereRepository.save(stock);
            
            // Créer un mouvement d'entrée
            createMouvement(savedMatiere.getId(), "ENTREE", createDTO.getQuantiteInitiale(), "Stock initial");
        }
        
        return convertToMatierePremiereDTO(savedMatiere);
    }
    
    /**
     * Obtenir toutes les matières premières avec leur stock
     */
    public List<MatierePremiereDTO> getAllMatieresPremieres() {
        return matierePremiereRepository.findAll().stream()
                .map(this::convertToMatierePremiereDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtenir une matière première par ID
     */
    public Optional<MatierePremiereDTO> getMatierePremiereById(Long id) {
        return matierePremiereRepository.findById(id)
                .map(this::convertToMatierePremiereDTO);
    }

    // ==================== GESTION MOUVEMENTS STOCK ====================
    
    /**
     * Créer un mouvement de stock
     */
    public MouvementStockDTO createMouvement(Long matiereId, String typeMouvement, BigDecimal quantite, String commentaire) {
        MatierePremiere matiere = matierePremiereRepository.findById(matiereId)
                .orElseThrow(() -> new RuntimeException("Matière première non trouvée"));
        
        // Créer le mouvement
        MouvementStockMatiere mouvement = new MouvementStockMatiere();
        mouvement.setMatiere(matiere);
        mouvement.setTypeMouvement(typeMouvement.toUpperCase());
        mouvement.setQuantite(quantite);
        mouvement.setCommentaire(commentaire);
        mouvement.setDateMouvement(LocalDateTime.now());
        
        MouvementStockMatiere savedMouvement = mouvementStockMatiereRepository.save(mouvement);
        
        // Mettre à jour le stock
        updateStock(matiereId, typeMouvement, quantite);
        
        return convertToMouvementStockDTO(savedMouvement);
    }
    
    /**
     * Créer un mouvement via DTO
     */
    public MouvementStockDTO createMouvement(CreateMouvementStockDTO createDTO) {
        return createMouvement(
            createDTO.getMatiereId(),
            createDTO.getTypeMouvement(),
            createDTO.getQuantite(),
            createDTO.getCommentaire()
        );
    }
    
    /**
     * Obtenir l'historique des mouvements
     */
    public List<MouvementStockDTO> getMouvements(Long matiereId, LocalDate dateDebut, LocalDate dateFin) {
        List<MouvementStockMatiere> mouvements;
        
        if (matiereId != null && dateDebut != null && dateFin != null) {
            mouvements = mouvementStockMatiereRepository.findByMatiereIdAndDateMouvementBetween(
                matiereId, dateDebut.atStartOfDay(), dateFin.atTime(23, 59, 59));
        } else if (matiereId != null) {
            mouvements = mouvementStockMatiereRepository.findByMatiereIdOrderByDateMouvementDesc(matiereId);
        } else if (dateDebut != null && dateFin != null) {
            mouvements = mouvementStockMatiereRepository.findByDateMouvementBetween(
                dateDebut.atStartOfDay(), dateFin.atTime(23, 59, 59));
        } else {
            mouvements = mouvementStockMatiereRepository.findAllByOrderByDateMouvementDesc();
        }
        
        return mouvements.stream()
                .map(this::convertToMouvementStockDTO)
                .collect(Collectors.toList());
    }

    // ==================== GESTION DECHETS ====================
    
    /**
     * Déclarer un déchet
     */
    public DechetDTO declarerDechet(Long matiereId, BigDecimal quantite, String raison, String commentaire) {
        // Créer un mouvement de type "DECHET"
        MouvementStockDTO mouvement = createMouvement(matiereId, "DECHET", quantite, 
            "DÉCHET - " + raison + (commentaire != null ? " : " + commentaire : ""));
        
        // Convertir en DTO déchet
        DechetDTO dechet = new DechetDTO();
        dechet.setId(mouvement.getId());
        dechet.setMatiereId(matiereId);
        dechet.setNomMatiere(mouvement.getNomMatiere());
        dechet.setQuantiteGaspillee(quantite);
        dechet.setRaisonDechet(raison);
        dechet.setDateDechet(LocalDate.now());
        dechet.setCommentaire(commentaire);
        
        // Calculer une estimation de la valeur perdue basée sur une moyenne standard
        // Pour obtenir une valeur plus précise, implémenter la récupération du prix réel
        // depuis la base de données ou depuis une entité prix à créer
        BigDecimal prixMoyen = new BigDecimal("1000"); // Prix moyen estimé par unité
        dechet.setValeurPerdue(prixMoyen.multiply(quantite));
        
        return dechet;
    }
    
    /**
     * Obtenir les déchets par période
     */
    public List<DechetDTO> getDechets(LocalDate dateDebut, LocalDate dateFin) {
        List<MouvementStockMatiere> mouvements = mouvementStockMatiereRepository
            .findByDateMouvementBetween(
                dateDebut.atStartOfDay(), 
                dateFin.atTime(23, 59, 59)
            ).stream()
            .filter(m -> "DECHET".equals(m.getTypeMouvement()))
            .collect(Collectors.toList());
        
        return mouvements.stream()
                .map(this::convertToDechetDTO)
                .collect(Collectors.toList());
    }

    // ==================== ALERTES PEREMPTION ====================
    
    /**
     * Créer une alerte de péremption
     */
    public AlertePeremptionDTO createAlertePeremption(Long matiereId, LocalDate datePeremption, Integer seuilAlerte) {
        MatierePremiere matiere = matierePremiereRepository.findById(matiereId)
                .orElseThrow(() -> new RuntimeException("Matière première non trouvée"));
        
        AlertePeremption alerte = new AlertePeremption();
        alerte.setMatiere(matiere);
        alerte.setDatePeremption(datePeremption);
        alerte.setSeuilAlerte(seuilAlerte);
        
        AlertePeremption savedAlerte = alertePeremptionRepository.save(alerte);
        return convertToAlertePeremptionDTO(savedAlerte);
    }
    
    /**
     * Obtenir les alertes actives
     */
    public List<AlertePeremptionDTO> getAlertesActives() {
        LocalDate today = LocalDate.now();
        List<AlertePeremption> alertes = alertePeremptionRepository.findActiveAlertes(today);
        
        return alertes.stream()
                .map(this::convertToAlertePeremptionDTO)
                .collect(Collectors.toList());
    }

    // ==================== RESUME STOCK ====================
    
    /**
     * Obtenir le résumé de tous les stocks
     */
    public List<StockSummaryDTO> getStockSummary() {
        List<StockMatiere> stocks = stockMatiereRepository.findAll();
        
        return stocks.stream()
                .map(this::convertToStockSummaryDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtenir les stocks faibles (en dessous du seuil)
     */
    public List<StockSummaryDTO> getStocksFaibles() {
        return getStockSummary().stream()
                .filter(stock -> "FAIBLE".equals(stock.getStatutStock()) || "CRITIQUE".equals(stock.getStatutStock()))
                .collect(Collectors.toList());
    }

    // ==================== ESTIMATION PRODUCTION ====================
    
    /**
     * Estimer la capacité de production pour un produit
     */
    public EstimationProductionDTO estimerProduction(Long produitId, Integer quantiteSouhaitee) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        
        EstimationProductionDTO estimation = new EstimationProductionDTO();
        estimation.setProduitId(produitId);
        estimation.setNomProduit(produit.getNom());
        estimation.setQuantiteSouhaitee(quantiteSouhaitee);
        
        // Vérifier les stocks des matières premières principales 
        // Récupérer toutes les matières premières disponibles
        List<StockMatiere> stocksDisponibles = stockMatiereRepository.findAll();
        List<MatierePremiere> matieres = matierePremiereRepository.findAll();
        
        // Pour un fromage, on estime qu'il faut en moyenne:
        // - 8 litres de lait par kg de fromage
        // - 0.02 kg de ferment lactique par kg de fromage
        // - 0.001 kg de présure par kg de fromage
        // - 0.2 kg de sel par kg de fromage
        
        BigDecimal poidsProduit = produit.getPoids() != null ? produit.getPoids() : new BigDecimal("1.0");
        BigDecimal quantiteTotale = poidsProduit.multiply(new BigDecimal(quantiteSouhaitee));
        
        // Quantités nécessaires
        Map<String, BigDecimal> quantitesNecessaires = new HashMap<>();
        quantitesNecessaires.put("LAIT", new BigDecimal("8.0").multiply(quantiteTotale));
        quantitesNecessaires.put("FERMENT", new BigDecimal("0.02").multiply(quantiteTotale));
        quantitesNecessaires.put("PRÉSURE", new BigDecimal("0.001").multiply(quantiteTotale));
        quantitesNecessaires.put("SEL", new BigDecimal("0.2").multiply(quantiteTotale));
        
        // Vérifier la disponibilité
        List<EstimationProductionDTO.MatiereMangante> matieresManquantes = new ArrayList<>();
        boolean estPossible = true;
        
        for (Map.Entry<String, BigDecimal> entry : quantitesNecessaires.entrySet()) {
            String nomMatiere = entry.getKey();
            BigDecimal quantiteNecessaire = entry.getValue();
            
            // Chercher la matière première correspondante
            Optional<MatierePremiere> matiereOpt = matieres.stream()
                .filter(m -> m.getNom().toUpperCase().contains(nomMatiere))
                .findFirst();
                
            if (matiereOpt.isPresent()) {
                MatierePremiere matiere = matiereOpt.get();
                
                // Chercher son stock
                Optional<StockMatiere> stockOpt = stocksDisponibles.stream()
                    .filter(s -> s.getMatiere().getId().equals(matiere.getId()))
                    .findFirst();
                    
                BigDecimal stockDisponible = stockOpt.isPresent() ? 
                    stockOpt.get().getQuantite() : BigDecimal.ZERO;
                    
                if (stockDisponible.compareTo(quantiteNecessaire) < 0) {
                    // Stock insuffisant
                    EstimationProductionDTO.MatiereMangante manquante = new EstimationProductionDTO.MatiereMangante();
                    manquante.setMatiereId(matiere.getId());
                    manquante.setNomMatiere(matiere.getNom());
                    manquante.setQuantiteNecessaire(quantiteNecessaire);
                    manquante.setQuantiteDisponible(stockDisponible);
                    manquante.setQuantiteManquante(quantiteNecessaire.subtract(stockDisponible));
                    
                    matieresManquantes.add(manquante);
                    estPossible = false;
                }
            } else {
                // Matière non trouvée
                EstimationProductionDTO.MatiereMangante manquante = new EstimationProductionDTO.MatiereMangante();
                manquante.setMatiereId(null);
                manquante.setNomMatiere(nomMatiere);
                manquante.setQuantiteNecessaire(quantiteNecessaire);
                manquante.setQuantiteDisponible(BigDecimal.ZERO);
                manquante.setQuantiteManquante(quantiteNecessaire);
                
                matieresManquantes.add(manquante);
                estPossible = false;
            }
        }
        
        estimation.setProductionPossible(estPossible);
        estimation.setMatieresManquantes(matieresManquantes);
        if (!estPossible) {
            estimation.setRaisonImpossible("Stock insuffisant pour certaines matières premières");
        }
        
        return estimation;
    }

    // ==================== METHODES UTILITAIRES ====================
    
    private void updateStock(Long matiereId, String typeMouvement, BigDecimal quantite) {
        // Utiliser la méthode findByMatiereId pour récupérer le stock
        StockMatiere stock = stockMatiereRepository.findByMatiereId(matiereId)
                .orElseGet(() -> {
                    StockMatiere newStock = new StockMatiere();
                    MatierePremiere matiere = matierePremiereRepository.findById(matiereId).orElse(null);
                    newStock.setMatiere(matiere);
                    newStock.setQuantite(BigDecimal.ZERO);
                    return newStock;
                });
        
        switch (typeMouvement.toUpperCase()) {
            case "ENTREE":
                stock.setQuantite(stock.getQuantite().add(quantite));
                break;
            case "SORTIE":
            case "DECHET":
                stock.setQuantite(stock.getQuantite().subtract(quantite));
                break;
            case "AJUSTEMENT":
                stock.setQuantite(quantite);
                break;
        }
        
        // Empêcher les stocks négatifs
        if (stock.getQuantite().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Stock insuffisant pour cette opération");
        }
        
        stockMatiereRepository.save(stock);
    }
    
    /**
     * Détermine le statut d'un stock en fonction de sa quantité et d'une quantité minimale
     * 
     * @param quantite Quantité actuelle en stock
     * @param seuilMinimum Quantité minimum requise (seuil critique)
     * @return Statut du stock (VIDE, CRITIQUE, FAIBLE, NORMAL)
     */
    private String determinerStatutStock(BigDecimal quantite, BigDecimal seuilMinimum) {
        if (quantite.compareTo(BigDecimal.ZERO) == 0) {
            return "VIDE";
        } else if (quantite.compareTo(seuilMinimum) < 0) {
            return "CRITIQUE";
        } else if (quantite.compareTo(seuilMinimum.multiply(new BigDecimal("2"))) < 0) {
            return "FAIBLE";
        } else {
            return "NORMAL";
        }
    }
    
    /**
     * Détermine le statut d'un stock en fonction de sa quantité avec seuils fixes
     * @deprecated Utiliser determinerStatutStock(BigDecimal, BigDecimal) à la place
     */
    private String determinerStatutStock(BigDecimal quantite) {
        if (quantite.compareTo(BigDecimal.ZERO) == 0) {
            return "VIDE";
        } else if (quantite.compareTo(BigDecimal.valueOf(10)) < 0) {
            return "CRITIQUE";
        } else if (quantite.compareTo(BigDecimal.valueOf(50)) < 0) {
            return "FAIBLE";
        } else {
            return "NORMAL";
        }
    }

    // ==================== CONVERSIONS DTO ====================
    
    private MatierePremiereDTO convertToMatierePremiereDTO(MatierePremiere matiere) {
        MatierePremiereDTO dto = new MatierePremiereDTO();
        dto.setId(matiere.getId());
        dto.setNom(matiere.getNom());
        dto.setUnite(matiere.getUnite());
        dto.setDureeConservation(matiere.getDureeConservation());
        
        // Récupérer la quantité en stock
        Optional<StockMatiere> stock = stockMatiereRepository.findAll().stream()
                .filter(s -> s.getMatiere().getId().equals(matiere.getId()))
                .findFirst();
        BigDecimal quantite = stock.map(StockMatiere::getQuantite).orElse(BigDecimal.ZERO);
        dto.setQuantiteEnStock(quantite);
        dto.setStatutStock(determinerStatutStock(quantite));
        
        return dto;
    }
    
    private MouvementStockDTO convertToMouvementStockDTO(MouvementStockMatiere mouvement) {
        MouvementStockDTO dto = new MouvementStockDTO();
        dto.setId(mouvement.getId());
        dto.setMatiereId(mouvement.getMatiere().getId());
        dto.setNomMatiere(mouvement.getMatiere().getNom());
        dto.setTypeMouvement(mouvement.getTypeMouvement());
        dto.setQuantite(mouvement.getQuantite());
        dto.setDateMouvement(mouvement.getDateMouvement().toString());
        dto.setCommentaire(mouvement.getCommentaire());
        return dto;
    }
    
    private AlertePeremptionDTO convertToAlertePeremptionDTO(AlertePeremption alerte) {
        AlertePeremptionDTO dto = new AlertePeremptionDTO();
        dto.setId(alerte.getId());
        dto.setMatiereId(alerte.getMatiere().getId());
        dto.setNomMatiere(alerte.getMatiere().getNom());
        dto.setDatePeremption(alerte.getDatePeremption());
        dto.setSeuilAlerte(alerte.getSeuilAlerte());
        
        // Calculer jours restants
        long joursRestants = ChronoUnit.DAYS.between(LocalDate.now(), alerte.getDatePeremption());
        dto.setJoursRestants((int) joursRestants);
        
        // Déterminer niveau alerte
        if (joursRestants <= 0) {
            dto.setNiveauAlerte("CRITIQUE");
        } else if (joursRestants <= alerte.getSeuilAlerte()) {
            dto.setNiveauAlerte("ATTENTION");
        } else {
            dto.setNiveauAlerte("NORMAL");
        }
        
        return dto;
    }
    
    private StockSummaryDTO convertToStockSummaryDTO(StockMatiere stock) {
        StockSummaryDTO dto = new StockSummaryDTO();
        dto.setMatiereId(stock.getMatiere().getId());
        dto.setNomMatiere(stock.getMatiere().getNom());
        dto.setUnite(stock.getMatiere().getUnite());
        dto.setQuantiteActuelle(stock.getQuantite());
        
        // Déterminer la quantité minimum basée sur le type de matière
        // On utilise des seuils différents selon le type de matière
        String nomMatiere = stock.getMatiere().getNom().toUpperCase();
        BigDecimal quantiteMinimum;
        
        if (nomMatiere.contains("LAIT")) {
            // Pour le lait, maintenir un stock minimum plus élevé
            quantiteMinimum = BigDecimal.valueOf(100);
        } else if (nomMatiere.contains("SEL")) {
            quantiteMinimum = BigDecimal.valueOf(20);
        } else if (nomMatiere.contains("FERMENT") || nomMatiere.contains("PRÉSURE")) {
            // Pour les ferments et présures, petites quantités mais essentiels
            quantiteMinimum = BigDecimal.valueOf(5);
        } else {
            // Par défaut
            quantiteMinimum = BigDecimal.valueOf(10);
        }
        
        dto.setQuantiteMinimum(quantiteMinimum);
        dto.setStatutStock(determinerStatutStock(stock.getQuantite(), quantiteMinimum));
        
        // Calculer les jours restants basés sur la consommation historique
        // Pour l'instant, utiliser une estimation basée sur le ratio stock/minimum
        int joursEstimes = 0;
        if (stock.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
            // Formule: (quantité / minimum) * 30 jours (stock minimum = 1 mois)
            joursEstimes = (int) Math.round(stock.getQuantite().divide(
                quantiteMinimum, 2, java.math.RoundingMode.HALF_UP).doubleValue() * 30);
            // Max 90 jours
            joursEstimes = Math.min(joursEstimes, 90);
        }
        dto.setJoursRestantsStock(joursEstimes);
        
        // Estimer la valeur du stock basée sur des prix moyens du marché
        BigDecimal prixUnitaire;
        if (nomMatiere.contains("LAIT")) {
            prixUnitaire = new BigDecimal("500"); // 500 Ar par litre
        } else if (nomMatiere.contains("SEL")) {
            prixUnitaire = new BigDecimal("1500"); // 1500 Ar par kg
        } else if (nomMatiere.contains("FERMENT")) {
            prixUnitaire = new BigDecimal("15000"); // 15000 Ar par kg
        } else if (nomMatiere.contains("PRÉSURE")) {
            prixUnitaire = new BigDecimal("20000"); // 20000 Ar par kg
        } else {
            prixUnitaire = new BigDecimal("5000"); // Prix par défaut
        }
        
        dto.setValeurStock(stock.getQuantite().multiply(prixUnitaire));
        return dto;
    }
    
    private DechetDTO convertToDechetDTO(MouvementStockMatiere mouvement) {
        DechetDTO dto = new DechetDTO();
        dto.setId(mouvement.getId());
        dto.setMatiereId(mouvement.getMatiere().getId());
        dto.setNomMatiere(mouvement.getMatiere().getNom());
        dto.setQuantiteGaspillee(mouvement.getQuantite());
        dto.setDateDechet(mouvement.getDateMouvement().toLocalDate());
        dto.setCommentaire(mouvement.getCommentaire());
        
        // Extraire la raison du commentaire si possible
        String commentaire = mouvement.getCommentaire();
        if (commentaire != null && commentaire.startsWith("DÉCHET - ")) {
            String[] parts = commentaire.split(" : ");
            if (parts.length > 0) {
                dto.setRaisonDechet(parts[0].replace("DÉCHET - ", ""));
            }
        }
        
        // Calculer une estimation de la valeur perdue
        BigDecimal prixMoyen = new BigDecimal("1000"); // Prix moyen estimé par unité
        dto.setValeurPerdue(prixMoyen.multiply(dto.getQuantiteGaspillee()));
        return dto;
    }
}
