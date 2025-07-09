package itu.fromagerie.fromagerie.service.commande;

import itu.fromagerie.fromagerie.entities.vente.*;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.repository.vente.CommandeRepository;
import itu.fromagerie.fromagerie.repository.vente.LigneCommandeRepository;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;
import itu.fromagerie.fromagerie.repository.client.ClientRepository;
import itu.fromagerie.fromagerie.repository.produit.LotProduitRepository;
import itu.fromagerie.fromagerie.repository.vente.PromotionCommandeRepository;
import itu.fromagerie.fromagerie.repository.vente.PromotionRepository;
import itu.fromagerie.fromagerie.service.produit.ProduitService;
import itu.fromagerie.fromagerie.service.vente.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final ProduitRepository produitRepository;
    private final ClientRepository clientRepository;
    private final ProduitService produitService;
    private final LotProduitRepository lotProduitRepository;
    private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;
    private final PromotionCommandeRepository promotionCommandeRepository;
    @Autowired
    public CommandeService(CommandeRepository commandeRepository,
                           LigneCommandeRepository ligneCommandeRepository,
                           ProduitRepository produitRepository, ClientRepository clientRepository,
                           ProduitService produitService, LotProduitRepository lotProduitRepository,
                           PromotionService promotionService,
                           PromotionRepository promotionRepository,
                           PromotionCommandeRepository promotionCommandeRepository)
    {
        this.commandeRepository = commandeRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.produitRepository = produitRepository;
        this.clientRepository = clientRepository;
        this.produitService = produitService;
        this.lotProduitRepository = lotProduitRepository;
        this.promotionService = promotionService;
        this.promotionRepository = promotionRepository;

        this.promotionCommandeRepository = promotionCommandeRepository;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    public Commande updateCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }

    public int getLastInsertCommandID(){
        return commandeRepository.getLastInsertCommandeId().intValue();
    }

    public Commande saveCommande(Long idClient){
        Commande commande = new Commande();
        commande.setDateCommande(LocalDate.from(Instant.now()));
        commande.setStatut("En attente");
        return commandeRepository.save(commande);
    }

    public void creerCommandeAvecLignes(Long clientId, Map<String, String> formData) {
        // 1. Créer la commande

        Client cl = clientRepository.findById(clientId).orElse(null);

        Commande commande = new Commande();
        LocalDate dateCommande = LocalDate.now();
        commande.setClient(cl);
        commande.setDateCommande(dateCommande);
        commande.setStatut("EN ATTENTE");
        commande = commandeRepository.save(commande);

        // 2. Parcourir les données de type produits[ID].selected
        for (String key : formData.keySet()) {
            if (key.matches("produits\\[\\d+\\]\\.selected")) {
                // extraire l'id du produit depuis "produits[42].selected"
                Long produitId = Long.valueOf(key.replaceAll("\\D+", ""));
                Produit produit = produitRepository.findById(produitId).orElse(null);
                String quantiteKey = "produits[" + produitId + "].quantite";
                int quantite = Integer.parseInt(formData.get(quantiteKey));

                // mis a jour quantite
                Integer quantiteActuel = produitService.getQuantiteProduit(produitId);
                Integer nouveauQuantity = quantiteActuel - quantite;
                produitService.updateLotProduit(produitId, nouveauQuantity);

                if (promotionService.findPromotionCommande(produitId, dateCommande)){
                    Promotion promotion = promotionRepository.findPromotionCommande(produitId, dateCommande);
                    PromotionCommande pc = new PromotionCommande();
                    pc.setCommande(commande);
                    pc.setPromotion(promotion);
                    promotionCommandeRepository.save(pc);
                }

                // facultatif : vérifier si le produit existe
                if (quantite > 0) {
                    LigneCommande ligne = new LigneCommande();
                    ligne.setCommande(commande);
                    ligne.setProduit(produit);
                    ligne.setQuantite(quantite);
                    ligneCommandeRepository.save(ligne);
                }
            }
        }
    }

    public void saveCommande(Commande commande, List<LigneCommande> lignesCommande) {
        // Sauvegarder la commande
        commande = commandeRepository.save(commande);
        
        // Sauvegarder les lignes de commande
        for (LigneCommande ligne : lignesCommande) {
            ligne.setCommande(commande);
            ligneCommandeRepository.save(ligne);
        }
    }

    public void saveCommandeWithClient(Long clientId, LocalDate dateCommande, String statut) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new IllegalArgumentException("Client non trouvé");
        }

        Commande commande = new Commande();
        commande.setClient(client);
        commande.setDateCommande(dateCommande);
        commande.setStatut(statut);
        
        commandeRepository.save(commande);
    }

    public void saveLigneCommande(Long commandeId, Long produitId, int quantite) {
        Commande commande = commandeRepository.findById(commandeId).orElse(null);
        Produit produit = produitRepository.findById(produitId).orElse(null);
        
        if (commande == null || produit == null) {
            throw new IllegalArgumentException("Commande ou produit non trouvé");
        }

        LigneCommande ligne = new LigneCommande();
        ligne.setCommande(commande);
        ligne.setProduit(produit);
        ligne.setQuantite(quantite);
        
        ligneCommandeRepository.save(ligne);
    }
}
