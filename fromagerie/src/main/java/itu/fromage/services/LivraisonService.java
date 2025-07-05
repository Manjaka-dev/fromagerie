package itu.fromage.services;

import itu.fromage.entities.Commande;
import itu.fromage.entities.Livraison;
import itu.fromage.entities.Livreur;
import itu.fromage.entities.StatutLivraison;
import itu.fromage.entities.RetourLivraison;
import itu.fromage.entities.Paiement;
import itu.fromage.entities.ConfirmationReception;
import itu.fromage.repositories.CommandeRepository;
import itu.fromage.repositories.LivreurRepository;
import itu.fromage.repositories.LivraisonRepository;
import itu.fromage.repositories.StatutLivraisonRepository;
import itu.fromage.repositories.RetourLivraisonRepository;
import itu.fromage.repositories.LigneCommandeRepository;
import itu.fromage.repositories.PaiementRepository;
import itu.fromage.repositories.ConfirmationReceptionRepository;
import itu.fromage.projection.CommandeLivraisonGroup;
import itu.fromage.projection.CommandeLivraisonProjection;
import itu.fromage.projection.LivraisonProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        Map<Integer, CommandeLivraisonGroup> grouped = new LinkedHashMap<>();

        for (CommandeLivraisonProjection proj : projections) {
            // Filtrer ici : ne pas ajouter si la commande est annulée ou déjà planifiée
            Commande commande = commandeRepository.findById(proj.getCommandeId()).orElse(null);
            if (commande != null && "Annulée".equalsIgnoreCase(commande.getStatut())) {
                continue; // On saute cette commande annulée
            }
            // Vérifier si une livraison existe déjà pour cette commande
            boolean livraisonExiste = livraisonRepository.findByCommandeId(proj.getCommandeId()).isPresent();
            if (livraisonExiste) {
                continue; // On saute cette commande déjà planifiée
            }
            grouped.computeIfAbsent(proj.getCommandeId(), k -> new CommandeLivraisonGroup(
                proj.getCommandeId(),
                proj.getClientNom(),
                new ArrayList<>(),
                proj.getDateCommande(),
                proj.getDateLivraison()
            )).getProduits().add(new CommandeLivraisonGroup.ProductInfo(proj.getProduitNom(),proj.getPoids(), proj.getQuantite()));
        }

        grouped.values().forEach(group -> System.out.println("CommandeId: " + group.getCommandeId() + ", Produits: " + group.getProduits()));
        return new ArrayList<>(grouped.values());
    }

    public void saveLivraison(Integer idCommande, LocalDate dateLivraison, String zone, Integer livreur) {

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

    public void assignLivreur(Integer commandeId, Integer livreurId) {
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

    public Optional<Livraison> findLivraisonById(Integer livraisonId) {
        return livraisonRepository.findById(livraisonId);
    }

    public StatutLivraison saveStatutLivraison(StatutLivraison statutLivraison) {
        return statutLivraisonRepository.save(statutLivraison);
    }

    public void enregistrerPaiement(Integer commandeId, Double montant, String methode, LocalDate datePaiement) {
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

    public void annulerCommandeEtRetourLivraison(Integer commandeId) {
        // 1. Annuler la commande
        Commande commande = commandeRepository.findById(commandeId)
            .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée"));
        commande.setStatut("Annulée");
        commandeRepository.save(commande);

        // 2. Récupérer toutes les lignes de commande pour cette commande spécifique
        var lignes = ligneCommandeRepository.findByCommandeId(commandeId);
        
        // 3. Pour chaque ligne de commande, restaurer la quantité du produit
        for (var ligne : lignes) {
            Integer produitId = ligne.getProduit().getId();
            Integer quantiteActuelle = produitService.getQuantiteProduit(produitId);
            Integer quantiteCommande = ligne.getQuantite(); // Quantité spécifique à cette commande
            Integer nouveauQuantite = quantiteActuelle + quantiteCommande;

            System.out.println("Produit ID: " + produitId);
            System.out.println("Quantité actuelle: " + quantiteActuelle);
            System.out.println("Quantité commandée (à restaurer): " + quantiteCommande);
            System.out.println("Nouvelle quantité: " + nouveauQuantite);

            produitService.updateLotProduit(produitId, nouveauQuantite);
        }

        // 4. Pour chaque produit de la commande, insérer dans retour_livraison (livraison peut être null)
        for (var ligne : lignes) {
            RetourLivraison retour = new RetourLivraison();
            retour.setLivraison(null); // Pas de livraison associée
            retour.setProduit(ligne.getProduit());
            retour.setQuantiteRetour(ligne.getQuantite());
            retour.setRaison("Commande annulée par retour livraison");
            retourLivraisonRepository.save(retour);
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