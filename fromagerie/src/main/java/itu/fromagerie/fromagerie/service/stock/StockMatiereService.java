package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.*;
import itu.fromagerie.fromagerie.dto.stock.InventaireDTO;
import itu.fromagerie.fromagerie.dto.stock.ValorisationStockDTO;
import itu.fromagerie.fromagerie.dto.stock.AlerteStockDTO;
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
import java.util.List;
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

    /**
     * Mettre à jour une matière première
     */
    public MatierePremiereDTO updateMatierePremiere(Long id, MatierePremiereDTO updateDTO) {
        MatierePremiere matiere = matierePremiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière première non trouvée"));

        matiere.setNom(updateDTO.getNom());
        matiere.setUnite(updateDTO.getUnite());
        matiere.setDureeConservation(updateDTO.getDureeConservation());

        MatierePremiere savedMatiere = matierePremiereRepository.save(matiere);
        return convertToMatierePremiereDTO(savedMatiere);
    }

    /**
     * Supprimer une matière première
     */
    public void deleteMatierePremiere(Long id) {
        // Vérifier si la matière première a des mouvements
        List<MouvementStockMatiere> mouvements = mouvementStockMatiereRepository.findByMatiereId(id);
        if (!mouvements.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une matière première avec des mouvements de stock");
        }

        // Supprimer le stock associé
        stockMatiereRepository.deleteByMatiereId(id);

        // Supprimer la matière première
        matierePremiereRepository.deleteById(id);
    }

    // ==================== GESTION MOUVEMENTS STOCK ====================

    /**
     * Créer un mouvement de stock
     */
    public MouvementStockDTO createMouvement(Long matiereId, String typeMouvement, BigDecimal quantite,
            String commentaire) {
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
                createDTO.getCommentaire());
    }

    /**
     * Obtenir l'historique des mouvements
     */
    public List<MouvementStockDTO> getMouvements(Long matiereId, LocalDate dateDebut, LocalDate dateFin) {
        // TODO: Implémenter les méthodes manquantes dans
        // MouvementStockMatiereRepository
        // Pour l'instant, retourner une liste vide
        // return new ArrayList<>();
        // return mouvementStockMatiereRepository.findAll().stream()
        // .map(this::convertToMouvementStockDTO)
        // .collect(Collectors.toList());
        // Code original commenté :
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
        // TODO: Calculer valeur perdue basée sur prix d'achat de la matière
        dechet.setValeurPerdue(BigDecimal.ZERO);

        return dechet;
    }

    /**
     * Obtenir les déchets par période
     */
    public List<DechetDTO> getDechets(LocalDate dateDebut, LocalDate dateFin) {
        // TODO: Implémenter findByDateMouvementBetween dans
        // MouvementStockMatiereRepository
        // Pour l'instant, retourner une liste vide
        return new ArrayList<>();

        // Code original commenté :
        // List<MouvementStockMatiere> mouvements = mouvementStockMatiereRepository
        // .findByDateMouvementBetween(
        // dateDebut.atStartOfDay(),
        // dateFin.atTime(23, 59, 59)
        // ).stream()
        // .filter(m -> "DECHET".equals(m.getTypeMouvement()))
        // .collect(Collectors.toList());
        //
        // return mouvements.stream()
        // .map(this::convertToDechetDTO)
        // .collect(Collectors.toList());
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
        // TODO: Implémenter findActiveAlertes dans AlertePeremptionRepository
        // Pour l'instant, retourner une liste vide
        return new ArrayList<>();

        // Code original commenté :
        // LocalDate today = LocalDate.now();
        // List<AlertePeremption> alertes =
        // alertePeremptionRepository.findActiveAlertes(today);
        //
        // return alertes.stream()
        // .map(this::convertToAlertePeremptionDTO)
        // .collect(Collectors.toList());
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

        // TODO: Implémenter la logique de calcul basée sur les recettes
        // Pour l'instant, simulation simple
        estimation.setProductionPossible(true);
        estimation.setMatieresManquantes(List.of());

        return estimation;
    }

    // ==================== METHODES UTILITAIRES ====================

    private void updateStock(Long matiereId, String typeMouvement, BigDecimal quantite) {
        // TODO: Implémenter findByMatiereId dans StockMatiereRepository
        // Pour l'instant, utiliser findAll() et filtrer
        StockMatiere stock = stockMatiereRepository.findAll().stream()
                .filter(s -> s.getMatiere().getId().equals(matiereId))
                .findFirst()
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
        dto.setQuantiteMinimum(BigDecimal.valueOf(10)); // TODO: configurable
        dto.setStatutStock(determinerStatutStock(stock.getQuantite()));
        dto.setJoursRestantsStock(30); // TODO: calculer basé sur consommation
        dto.setValeurStock(BigDecimal.ZERO); // TODO: calculer basé sur prix
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

        dto.setValeurPerdue(BigDecimal.ZERO); // TODO: calculer
        return dto;
    }

    // ==================== GESTION INVENTAIRE ====================

    /**
     * Lancer un inventaire
     */
    public InventaireDTO lancerInventaire(String commentaire) {
        List<MatierePremiere> matieres = matierePremiereRepository.findAll();
        List<InventaireDTO.LigneInventaireDTO> lignes = new ArrayList<>();

        for (MatierePremiere matiere : matieres) {
            StockMatiere stock = stockMatiereRepository.findAll().stream()
                    .filter(s -> s.getMatiere().getId().equals(matiere.getId()))
                    .findFirst().orElse(null);
            BigDecimal quantiteTheorique = stock != null ? stock.getQuantite() : BigDecimal.ZERO;

            InventaireDTO.LigneInventaireDTO ligne = new InventaireDTO.LigneInventaireDTO();
            ligne.setMatiereId(matiere.getId());
            ligne.setNomMatiere(matiere.getNom());
            ligne.setQuantiteTheorique(quantiteTheorique);
            ligne.setQuantiteReelle(BigDecimal.ZERO); // À remplir manuellement
            ligne.setEcart(BigDecimal.ZERO);
            ligne.setUnite(matiere.getUnite());
            ligne.setCommentaire("");

            lignes.add(ligne);
        }

        InventaireDTO inventaire = new InventaireDTO();
        inventaire.setDateInventaire(LocalDate.now());
        inventaire.setStatut("EN_COURS");
        inventaire.setLignes(lignes);
        inventaire.setValeurTotale(BigDecimal.ZERO);
        inventaire.setCommentaire(commentaire);

        return inventaire;
    }

    // ==================== VALORISATION STOCK ====================

    /**
     * Calculer la valorisation du stock
     */
    public ValorisationStockDTO calculerValorisationStock() {
        List<StockMatiere> stocks = stockMatiereRepository.findAll();
        List<ValorisationStockDTO.ValorisationMatiereDTO> valorisations = new ArrayList<>();
        BigDecimal valeurTotale = BigDecimal.ZERO;

        for (StockMatiere stock : stocks) {
            // Prix unitaire fictif pour l'exemple (à remplacer par le vrai prix d'achat)
            BigDecimal prixUnitaire = BigDecimal.valueOf(5.0); // Prix par défaut

            BigDecimal valeurMatiere = stock.getQuantite().multiply(prixUnitaire);
            valeurTotale = valeurTotale.add(valeurMatiere);

            ValorisationStockDTO.ValorisationMatiereDTO valorisation = new ValorisationStockDTO.ValorisationMatiereDTO();
            valorisation.setMatiereId(stock.getMatiere().getId());
            valorisation.setNomMatiere(stock.getMatiere().getNom());
            valorisation.setQuantite(stock.getQuantite());
            valorisation.setPrixUnitaire(prixUnitaire);
            valorisation.setValeurTotale(valeurMatiere);
            valorisation.setUnite(stock.getMatiere().getUnite());
            valorisation.setStatut(determinerStatutStock(stock.getQuantite()));

            valorisations.add(valorisation);
        }

        return new ValorisationStockDTO(
                LocalDate.now(),
                valeurTotale,
                valorisations.size(),
                valorisations);
    }

    // ==================== ALERTES STOCK ====================

    /**
     * Obtenir toutes les alertes de stock
     */
    public AlerteStockDTO getAlertesStock() {
        List<AlerteStockDTO.AlerteStockFaibleDTO> alertesStockFaible = new ArrayList<>();
        List<AlerteStockDTO.AlerteRuptureDTO> alertesRupture = new ArrayList<>();

        List<StockMatiere> stocks = stockMatiereRepository.findAll();

        for (StockMatiere stock : stocks) {
            BigDecimal quantite = stock.getQuantite();
            String statut = determinerStatutStock(quantite);

            if ("FAIBLE".equals(statut)) {
                AlerteStockDTO.AlerteStockFaibleDTO alerte = new AlerteStockDTO.AlerteStockFaibleDTO();
                alerte.setMatiereId(stock.getMatiere().getId());
                alerte.setNomMatiere(stock.getMatiere().getNom());
                alerte.setQuantiteActuelle(quantite);
                alerte.setSeuilAlerte(BigDecimal.valueOf(10.0)); // Seuil par défaut
                alerte.setUnite(stock.getMatiere().getUnite());
                alerte.setNiveau("FAIBLE");
                alerte.setDateAlerte(LocalDate.now());

                alertesStockFaible.add(alerte);
            } else if ("CRITIQUE".equals(statut)) {
                AlerteStockDTO.AlerteStockFaibleDTO alerte = new AlerteStockDTO.AlerteStockFaibleDTO();
                alerte.setMatiereId(stock.getMatiere().getId());
                alerte.setNomMatiere(stock.getMatiere().getNom());
                alerte.setQuantiteActuelle(quantite);
                alerte.setSeuilAlerte(BigDecimal.valueOf(5.0)); // Seuil critique
                alerte.setUnite(stock.getMatiere().getUnite());
                alerte.setNiveau("CRITIQUE");
                alerte.setDateAlerte(LocalDate.now());

                alertesStockFaible.add(alerte);
            } else if (quantite.compareTo(BigDecimal.ZERO) == 0) {
                AlerteStockDTO.AlerteRuptureDTO alerte = new AlerteStockDTO.AlerteRuptureDTO();
                alerte.setMatiereId(stock.getMatiere().getId());
                alerte.setNomMatiere(stock.getMatiere().getNom());
                alerte.setUnite(stock.getMatiere().getUnite());
                alerte.setDateRupture(LocalDate.now());
                alerte.setImpact("PRODUCTION_BLOQUEE");

                alertesRupture.add(alerte);
            }
        }

        List<AlertePeremptionDTO> alertesPeremption = getAlertesActives();

        int nombreAlertesTotal = alertesStockFaible.size() + alertesRupture.size() + alertesPeremption.size();

        return new AlerteStockDTO(
                alertesStockFaible,
                alertesPeremption,
                alertesRupture,
                nombreAlertesTotal);
    }
}
