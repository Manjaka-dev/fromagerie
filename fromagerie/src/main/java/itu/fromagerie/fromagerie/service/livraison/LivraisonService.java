package itu.fromagerie.fromagerie.service.livraison;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import itu.fromagerie.fromagerie.entities.livraison.StatutLivraison;
import itu.fromagerie.fromagerie.entities.livraison.RetourLivraison;
import itu.fromagerie.fromagerie.entities.vente.Paiement;
import itu.fromagerie.fromagerie.entities.livraison.ConfirmationReception;
import itu.fromagerie.fromagerie.entities.vente.LigneCommande;
import itu.fromagerie.fromagerie.projection.CommandeLivraisonGroup;
import itu.fromagerie.fromagerie.projection.CommandeLivraisonProjection;
import itu.fromagerie.fromagerie.projection.LivraisonProjection;
import itu.fromagerie.fromagerie.service.produit.ProduitService;
import itu.fromagerie.fromagerie.repository.livraison.LivraisonRepository;
import itu.fromagerie.fromagerie.repository.livraison.LivreurRepository;
import itu.fromagerie.fromagerie.repository.livraison.StatutLivraisonRepository;
import itu.fromagerie.fromagerie.repository.livraison.ConfirmationReceptionRepository;
import itu.fromagerie.fromagerie.repository.livraison.RetourLivraisonRepository;
import itu.fromagerie.fromagerie.repository.vente.CommandeRepository;
import itu.fromagerie.fromagerie.repository.vente.PaiementRepository;
import itu.fromagerie.fromagerie.repository.vente.LigneCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LivraisonService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final CommandeRepository commandeRepository;
    private final LivreurRepository livreurRepository;
    private final LivraisonRepository livraisonRepository;
    private final StatutLivraisonRepository statutLivraisonRepository;
    private final RetourLivraisonRepository retourLivraisonRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final PaiementRepository paiementRepository;
    private final ConfirmationReceptionRepository confirmationReceptionRepository;
    private final ProduitService produitService;

    @Autowired
    public LivraisonService(CommandeRepository commandeRepository, LivreurRepository livreurRepository, LivraisonRepository livraisonRepository, StatutLivraisonRepository statutLivraisonRepository, RetourLivraisonRepository retourLivraisonRepository, LigneCommandeRepository ligneCommandeRepository, PaiementRepository paiementRepository, ConfirmationReceptionRepository confirmationReceptionRepository, ProduitService produitService) {
        this.commandeRepository = commandeRepository;
        this.livreurRepository = livreurRepository;
        this.livraisonRepository = livraisonRepository;
        this.statutLivraisonRepository = statutLivraisonRepository;
        this.retourLivraisonRepository = retourLivraisonRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.paiementRepository = paiementRepository;
        this.confirmationReceptionRepository = confirmationReceptionRepository;
        this.produitService = produitService;
    }

    public List<CommandeLivraisonGroup> getCommandeLivraison() {
        List<CommandeLivraisonProjection> projections = commandeRepository.findCommandesLivraisons();
        
        Map<Long, CommandeLivraisonGroup> groups = new HashMap<>();
        
        for (CommandeLivraisonProjection projection : projections) {
            Long commandeId = projection.getCommandeId();
            
            if (!groups.containsKey(commandeId)) {
                groups.put(commandeId, new CommandeLivraisonGroup(
                    commandeId,
                    projection.getClientNom(),
                    new ArrayList<>(),
                    projection.getDateCommande(),
                    projection.getDateLivraison()
                ));
            }
            
            CommandeLivraisonGroup.ProductInfo productInfo = new CommandeLivraisonGroup.ProductInfo(
                projection.getProduitNom(),
                projection.getPoids(),
                projection.getQuantite()
            );
            
            groups.get(commandeId).getProduits().add(productInfo);
        }
        
        return new ArrayList<>(groups.values());
    }

    public void saveLivraison(Long idCommande, LocalDate dateLivraison, String zone, Long livreur) {
        // Vérifier si une livraison existe déjà pour cette commande
        Optional<Livraison> existingLivraison = livraisonRepository.findByCommandeId(idCommande);
        
        Livraison livraison;
        if (existingLivraison.isPresent()) {
            // Mettre à jour la livraison existante
            livraison = existingLivraison.get();
            System.out.println("Mise à jour de la livraison existante ID: " + livraison.getId());
        } else {
            // Créer une nouvelle livraison
            Commande commande = commandeRepository.findById(idCommande).orElse(null);
            Livreur livreur1 = livreurRepository.findById(livreur).orElse(null);

            livraison = new Livraison();
            livraison.setCommande(commande);
            livraison.setLivreur(livreur1);
            System.out.println("Création d'une nouvelle livraison pour la commande ID: " + idCommande);
        }
        
        // Mettre à jour les informations de la livraison
        livraison.setDateLivraison(dateLivraison);
        livraison.setZone(zone);

        livraison = livraisonRepository.save(livraison);

        // Créer ou mettre à jour le statut
        StatutLivraison statutLivraison = new StatutLivraison();
        statutLivraison.setStatut("Planifiée");
        statutLivraison.setLivraison(livraison);
        statutLivraison.setDateStatut(Instant.now());
        statutLivraisonRepository.save(statutLivraison);
        
        System.out.println("Livraison sauvegardée avec succès - ID: " + livraison.getId());
    }

    public List<Livreur> listeLivreur() {
        return livreurRepository.findAll();
    }

    public void assignLivreur(Long commandeId, Long livreurId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée"));
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new IllegalArgumentException("Livreur non trouvé"));

        Livraison livraison = livraisonRepository.findByCommandeId(commandeId)
                .orElse(new Livraison());

        livraison.setCommande(commande);
        livraison.setLivreur(livreur);
        livraison.setDateLivraison(LocalDate.now());
        livraison.setZone("IVATO");

        livraisonRepository.save(livraison);

        // Ajout du statut après assignation
        StatutLivraison statut = new StatutLivraison();
        statut.setLivraison(livraison);
        statut.setStatut("Planifiée");
        statut.setDateStatut(Instant.now());
        statutLivraisonRepository.save(statut);
    }

    public List<LivraisonProjection> getLivraisonDetails() {
        // Forcer le rafraîchissement en vidant le cache Hibernate si nécessaire
        return livraisonRepository.getLivraisonDetails();
    }

    public Optional<Livraison> findLivraisonById(Long livraisonId) {
        return livraisonRepository.findById(livraisonId);
    }

    public Long getCommandeIdByLivraisonId(Long livraisonId) {
        Optional<Livraison> livraison = livraisonRepository.findById(livraisonId);
        return livraison.map(l -> l.getCommande().getId()).orElse(null);
    }

    public StatutLivraison saveStatutLivraison(StatutLivraison statutLivraison) {
        return statutLivraisonRepository.save(statutLivraison);
    }

    public void enregistrerPaiement(Long commandeId, Double montant, String methode, LocalDate datePaiement) {
        // 1. Créer un nouveau paiement
        Paiement paiement = new Paiement();
        paiement.setCommande(commandeRepository.findById(commandeId).orElse(null));
        paiement.setMontant(BigDecimal.valueOf(montant));
        paiement.setMethode(methode);
        paiement.setDatePaiement(datePaiement);
        
        // 2. Sauvegarder le paiement
        paiementRepository.save(paiement);
        
        // 3. Créer automatiquement une confirmation de réception
        Livraison livraison = livraisonRepository.findByCommandeId(commandeId).orElse(null);
        if (livraison != null) {
            ConfirmationReception confirmation = new ConfirmationReception();
            confirmation.setLivraison(livraison);
            confirmation.setSignature("Signature automatique - Paiement confirmé");
            confirmation.setPhotoReception("Photo automatique - Réception confirmée");
            
            confirmationReceptionRepository.save(confirmation);
            
            System.out.println("Confirmation de réception créée automatiquement pour la livraison ID: " + livraison.getId());
        }
        
        // 4. Logger les informations
        System.out.println("Paiement enregistré - Commande: " + commandeId + 
                          ", Montant: " + montant + 
                          ", Méthode: " + methode + 
                          ", Date: " + datePaiement);
    }

    public void afficherEtMettreAJourStatut() {
        List<LivraisonProjection> livraisons = getLivraisonDetails();
    
        for (LivraisonProjection livraison : livraisons) {
            // Mise à jour du statut
            Optional<Livraison> livraisonEntity = livraisonRepository.findById(livraison.getLivraisonId());
            if (livraisonEntity.isPresent()) {
                StatutLivraison statut = new StatutLivraison();
                statut.setLivraison(livraisonEntity.get());
    
                if ("En cours".equalsIgnoreCase(livraison.getStatutLivraison())) {
                    statut.setStatut("Livree");
                } else {
                    statut.setStatut("En cours");
                }
    
                statut.setDateStatut(Instant.now());
                statutLivraisonRepository.save(statut);
            }
        }
    }

    public void annulerCommandeEtRetourLivraison(Long commandeId) {
        // 1. Mettre à jour le statut de la commande
        Commande commande = commandeRepository.findById(commandeId).orElse(null);
        if (commande != null) {
            commande.setStatut("Annulée");
            commandeRepository.save(commande);
        }

        // 2. Récupérer les lignes de commande pour créer les retours
        List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(commandeId);
        
        // 3. Créer un retour pour chaque produit
        Livraison livraison = livraisonRepository.findByCommandeId(commandeId).orElse(null);
        if (livraison != null) {
            for (LigneCommande ligne : lignes) {
                RetourLivraison retour = new RetourLivraison();
                retour.setLivraison(livraison);
                retour.setProduit(ligne.getProduit());
                retour.setQuantiteRetour(ligne.getQuantite());
                retour.setRaison("Commande annulée par le client");
                
                retourLivraisonRepository.save(retour);
            }
        }
    }

    public void debugStatutsLivraison() {
        List<StatutLivraison> statuts = statutLivraisonRepository.findAll();
        System.out.println("=== DÉBOGAGE STATUTS ===");
        System.out.println("Nombre total de statuts: " + statuts.size());
        for (StatutLivraison statut : statuts) {
            System.out.println("Statut ID: " + statut.getId() + 
                              " | Livraison ID: " + statut.getLivraison().getId() + 
                              " | Statut: '" + statut.getStatut() + "'" +
                              " | Date: " + statut.getDateStatut());
            System.out.println();
        }
        System.out.println("=== FIN DÉBOGAGE STATUTS ===");
    }

    // ==================== NOUVELLES MÉTHODES POUR LES ENDPOINTS ====================

    /**
     * Met à jour une livraison existante
     */
    public Livraison updateLivraison(Long livraisonId, itu.fromagerie.fromagerie.dto.livraison.UpdateLivraisonDTO updateDTO) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);
        if (livraisonOpt.isEmpty()) {
            throw new RuntimeException("Livraison non trouvée avec l'ID: " + livraisonId);
        }

        Livraison livraison = livraisonOpt.get();
        
        // Mettre à jour les champs de base
        if (updateDTO.getDateLivraison() != null) {
            livraison.setDateLivraison(updateDTO.getDateLivraison());
        }
        if (updateDTO.getZone() != null) {
            livraison.setZone(updateDTO.getZone());
        }
        if (updateDTO.getLivreurId() != null) {
            Optional<Livreur> livreur = livreurRepository.findById(updateDTO.getLivreurId());
            if (livreur.isPresent()) {
                livraison.setLivreur(livreur.get());
            }
        }

        // Sauvegarder la livraison mise à jour
        Livraison livraisonMiseAJour = livraisonRepository.save(livraison);

        // Mettre à jour le statut si fourni
        if (updateDTO.getStatutLivraison() != null) {
            StatutLivraison nouveauStatut = new StatutLivraison();
            nouveauStatut.setLivraison(livraisonMiseAJour);
            nouveauStatut.setStatut(updateDTO.getStatutLivraison().getLibelle());
            nouveauStatut.setDateStatut(Instant.now());
            statutLivraisonRepository.save(nouveauStatut);
        }

        return livraisonMiseAJour;
    }

    /**
     * Annule une livraison en mettant son statut à "Annulée"
     */
    public void cancelLivraison(Long livraisonId) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);
        if (livraisonOpt.isEmpty()) {
            throw new RuntimeException("Livraison non trouvée avec l'ID: " + livraisonId);
        }

        Livraison livraison = livraisonOpt.get();
        
        // Créer un nouveau statut "Annulée"
        StatutLivraison statutAnnule = new StatutLivraison();
        statutAnnule.setLivraison(livraison);
        statutAnnule.setStatut("Annulée");
        statutAnnule.setDateStatut(Instant.now());
        
        statutLivraisonRepository.save(statutAnnule);
    }

    /**
     * Récupère la liste des zones de livraison disponibles
     */
    public List<String> getZonesLivraisonDisponibles() {
        List<String> zones = livraisonRepository.findDistinctZones();
        
        // Si aucune zone n'est trouvée, retourner des zones par défaut
        if (zones.isEmpty()) {
            zones = List.of(
                "Antananarivo Centre",
                "Antananarivo Nord", 
                "Antananarivo Sud",
                "Antananarivo Est",
                "Antananarivo Ouest",
                "Périphérie Nord",
                "Périphérie Sud"
            );
        }
        
        return zones;
    }

    /**
     * Récupère les livraisons d'un livreur spécifique
     */
    public List<itu.fromagerie.fromagerie.dto.livraison.LivraisonInfoDTO> getLivraisonsByLivreur(Long livreurId) {
        // Vérifier que le livreur existe
        Optional<Livreur> livreur = livreurRepository.findById(livreurId);
        if (livreur.isEmpty()) {
            throw new RuntimeException("Livreur non trouvé avec l'ID: " + livreurId);
        }

        List<Livraison> livraisons = livraisonRepository.findByLivreurId(livreurId);
        return convertToLivraisonInfoDTO(livraisons);
    }

    /**
     * Récupère les livraisons d'une zone spécifique
     */
    public List<itu.fromagerie.fromagerie.dto.livraison.LivraisonInfoDTO> getLivraisonsByZone(String zone) {
        List<Livraison> livraisons = livraisonRepository.findByZone(zone);
        return convertToLivraisonInfoDTO(livraisons);
    }

    /**
     * Convertit une liste de Livraison en LivraisonInfoDTO
     */
    private List<itu.fromagerie.fromagerie.dto.livraison.LivraisonInfoDTO> convertToLivraisonInfoDTO(List<Livraison> livraisons) {
        List<itu.fromagerie.fromagerie.dto.livraison.LivraisonInfoDTO> livraisonDTOs = new ArrayList<>();
        
        for (Livraison livraison : livraisons) {
            itu.fromagerie.fromagerie.dto.livraison.LivraisonInfoDTO dto = new itu.fromagerie.fromagerie.dto.livraison.LivraisonInfoDTO();
            
            // Informations de base
            dto.setLivraisonId(livraison.getId());
            dto.setDateLivraison(livraison.getDateLivraison());
            dto.setZone(livraison.getZone());
            
            // Informations commande
            if (livraison.getCommande() != null) {
                dto.setCommandeId(livraison.getCommande().getId());
                dto.setDateCommande(livraison.getCommande().getDateCommande());
                
                // Informations client
                if (livraison.getCommande().getClient() != null) {
                    dto.setClientId(livraison.getCommande().getClient().getId());
                    dto.setNomClient(livraison.getCommande().getClient().getNom());
                    dto.setTelephoneClient(livraison.getCommande().getClient().getTelephone());
                    dto.setAdresseClient(livraison.getCommande().getClient().getAdresse());
                }
            }
            
            // Informations livreur
            if (livraison.getLivreur() != null) {
                dto.setLivreurId(livraison.getLivreur().getId());
                dto.setNomLivreur(livraison.getLivreur().getNom());
                dto.setTelephoneLivreur(livraison.getLivreur().getTelephone());
            }
            
            // Récupérer le dernier statut
            Optional<StatutLivraison> dernierStatut = statutLivraisonRepository
                .findTopByLivraisonOrderByDateStatutDesc(livraison);
            if (dernierStatut.isPresent()) {
                String statutStr = dernierStatut.get().getStatut();
                try {
                    dto.setStatut(itu.fromagerie.fromagerie.entities.livraison.StatutLivraisonEnum.valueOf(statutStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    // Si le statut ne correspond à aucun enum, utiliser PLANIFIEE par défaut
                    dto.setStatut(itu.fromagerie.fromagerie.entities.livraison.StatutLivraisonEnum.PLANIFIEE);
                }
            }
            
            livraisonDTOs.add(dto);
        }
        
        return livraisonDTOs;
    }

    /**
     * Corrige toutes les séquences PostgreSQL problématiques
     */
    @Transactional
    public void fixStatutLivraisonSequence() {
        try {
            // Corriger la séquence statut_livraison
            Long maxStatutId = statutLivraisonRepository.findAll()
                    .stream()
                    .mapToLong(StatutLivraison::getId)
                    .max()
                    .orElse(0L);
            
            String sql1 = "SELECT setval('statut_livraison_id_seq', " + (maxStatutId + 1) + ", false)";
            entityManager.createNativeQuery(sql1).getSingleResult();
            System.out.println("Séquence statut_livraison corrigée - prochain ID: " + (maxStatutId + 1));
            
            // Corriger la séquence paiement  
            Long maxPaiementId = paiementRepository.findAll()
                    .stream()
                    .mapToLong(paiement -> paiement.getId())
                    .max()
                    .orElse(0L);
            
            String sql2 = "SELECT setval('paiement_id_seq', " + (maxPaiementId + 1) + ", false)";
            entityManager.createNativeQuery(sql2).getSingleResult();
            System.out.println("Séquence paiement corrigée - prochain ID: " + (maxPaiementId + 1));
            
            // Corriger la séquence confirmation_reception
            Long maxConfirmationId = confirmationReceptionRepository.findAll()
                    .stream()
                    .mapToLong(conf -> conf.getId())
                    .max()
                    .orElse(0L);
            
            String sql3 = "SELECT setval('confirmation_reception_id_seq', " + (maxConfirmationId + 1) + ", false)";
            entityManager.createNativeQuery(sql3).getSingleResult();
            System.out.println("Séquence confirmation_reception corrigée - prochain ID: " + (maxConfirmationId + 1));
            
        } catch (Exception e) {
            System.out.println("Erreur lors de la correction des séquences: " + e.getMessage());
            throw e;
        }
    }

    // ==================== MÉTHODES UTILITAIRES ====================
}