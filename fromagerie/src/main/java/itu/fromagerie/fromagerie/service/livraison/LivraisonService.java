package itu.fromagerie.fromagerie.service.livraison;

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

        Commande commande = commandeRepository.findById(idCommande).orElse(null);
        Livreur livreur1 = livreurRepository.findById(livreur).orElse(null);

        Livraison livraison = new Livraison();
        livraison.setCommande(commande);
        livraison.setDateLivraison(dateLivraison);
        livraison.setLivreur(livreur1);
        livraison.setZone(zone);

        livraison = livraisonRepository.save(livraison);

        StatutLivraison statutLivraison = new StatutLivraison();
        statutLivraison.setStatut("Planifiée");
        statutLivraison.setLivraison(livraison);
        statutLivraisonRepository.save(statutLivraison);
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