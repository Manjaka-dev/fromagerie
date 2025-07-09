package itu.fromagerie.fromagerie.service.livraison;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import itu.fromagerie.fromagerie.entities.livraison.StatutLivraison;
import itu.fromagerie.fromagerie.entities.livraison.RetourLivraison;
import itu.fromagerie.fromagerie.entities.vente.Paiement;
import itu.fromagerie.fromagerie.entities.livraison.ConfirmationReception;
import itu.fromagerie.fromagerie.entities.vente.LigneCommande;
import itu.fromagerie.fromagerie.entities.vente.Promotion;
import itu.fromagerie.fromagerie.projection.CommandeLivraisonGroup;
import itu.fromagerie.fromagerie.projection.CommandeLivraisonProjection;
import itu.fromagerie.fromagerie.projection.LivraisonProjection;
import itu.fromagerie.fromagerie.repository.vente.PromotionCommandeRepository;
import itu.fromagerie.fromagerie.service.produit.ProduitService;
import itu.fromagerie.fromagerie.repository.livraison.LivraisonRepository;
import itu.fromagerie.fromagerie.repository.livraison.LivreurRepository;
import itu.fromagerie.fromagerie.repository.livraison.StatutLivraisonRepository;
import itu.fromagerie.fromagerie.repository.livraison.ConfirmationReceptionRepository;
import itu.fromagerie.fromagerie.repository.livraison.RetourLivraisonRepository;
import itu.fromagerie.fromagerie.repository.vente.CommandeRepository;
import itu.fromagerie.fromagerie.repository.vente.PaiementRepository;
import itu.fromagerie.fromagerie.repository.vente.LigneCommandeRepository;
import itu.fromagerie.fromagerie.service.vente.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;

@Service
public class LivraisonService {
    private final CommandeRepository commandeRepository;
    private final LivreurRepository livreurRepository;
    private final LivraisonRepository livraisonRepository;
    private final StatutLivraisonRepository statutLivraisonRepository;
    private final RetourLivraisonRepository retourLivraisonRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final PaiementRepository paiementRepository;
    private final ConfirmationReceptionRepository confirmationReceptionRepository;
    private final PromotionCommandeRepository promotionCommandeRepository;
    private final PromotionService promotionService;

    @Autowired
    public LivraisonService(CommandeRepository commandeRepository, LivreurRepository livreurRepository,
                            LivraisonRepository livraisonRepository, StatutLivraisonRepository statutLivraisonRepository,
                            RetourLivraisonRepository retourLivraisonRepository, LigneCommandeRepository ligneCommandeRepository,
                            PaiementRepository paiementRepository, ConfirmationReceptionRepository confirmationReceptionRepository,
                            PromotionCommandeRepository promotionCommandeRepository,
                            PromotionService promotionService)
    {
        this.commandeRepository = commandeRepository;
        this.livreurRepository = livreurRepository;
        this.livraisonRepository = livraisonRepository;
        this.statutLivraisonRepository = statutLivraisonRepository;
        this.retourLivraisonRepository = retourLivraisonRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.paiementRepository = paiementRepository;
        this.confirmationReceptionRepository = confirmationReceptionRepository;
        this.promotionCommandeRepository = promotionCommandeRepository;
        this.promotionService = promotionService;
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

    public void enregistrerPaiement(Long commandeId, double montant, String methode, LocalDate datePaiement) {
        // 0. verification promotion
        BigDecimal montantInsere = BigDecimal.valueOf(montant); // montant est un paramètre

        if (promotionService.findStatutPromotionCommande(commandeId) &&
                promotionService.findStatutLivraisonCommande(commandeId)) {

            Promotion pr = promotionCommandeRepository.getPromotionByCommandeId(commandeId);
            BigDecimal reduction = pr.getReductionPourcentage(); // Par exemple 20 pour 20%

            // Calcul du montant de la réduction
            BigDecimal reductionMontant = montantInsere.multiply(reduction).divide(BigDecimal.valueOf(100));

            // Appliquer la réduction au montant initial
            montantInsere = montantInsere.subtract(reductionMontant);
        }


        // 1. Créer un nouveau paiement
        Paiement paiement = new Paiement();
        paiement.setCommande(commandeRepository.findById(commandeId).orElse(null));
        paiement.setMontant(montantInsere);
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
        System.out.println("=== DÉBOGAGE STATUTS EN BASE ===");
        
        // Vérifier tous les statuts pour chaque livraison
        List<Livraison> livraisons = livraisonRepository.findAll();
        for (Livraison livraison : livraisons) {
            System.out.println("Livraison ID: " + livraison.getId());
            
            // Récupérer tous les statuts pour cette livraison
            List<StatutLivraison> statuts = statutLivraisonRepository.findAll().stream()
                .filter(s -> s.getLivraison().getId().equals(livraison.getId()))
                .sorted((s1, s2) -> s2.getDateStatut().compareTo(s1.getDateStatut()))
                .toList();
            
            for (StatutLivraison statut : statuts) {
                System.out.println("  - Statut ID: " + statut.getId() + 
                                  " | Statut: '" + statut.getStatut() + "'" +
                                  " | Date: " + statut.getDateStatut());
            }
            System.out.println();
        }
        System.out.println("=== FIN DÉBOGAGE STATUTS ===");
    }
}