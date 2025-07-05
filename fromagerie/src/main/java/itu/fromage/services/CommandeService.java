package itu.fromage.services;

import itu.fromage.entities.*;
import itu.fromage.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Service
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final ProduitRepository produitRepository;
    private final ClientRepository clientRepository;
    private final ProduitService produitService;
    private final LotProduitRepository lotProduitRepository;
    @Autowired
    public CommandeService(CommandeRepository commandeRepository, LigneCommandeRepository ligneCommandeRepository, ProduitRepository produitRepository, ClientRepository clientRepository, ProduitService produitService, LotProduitRepository lotProduitRepository) {
        this.commandeRepository = commandeRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.produitRepository = produitRepository;
        this.clientRepository = clientRepository;
        this.produitService = produitService;
        this.lotProduitRepository = lotProduitRepository;
    }

    public int getLastInsertCommandID(){
        return commandeRepository.getLastInsertCommandeId();
    }

    public Commande saveCommande(Integer idClient){
        Commande commande = new Commande();
        commande.setDateCommande(LocalDate.from(Instant.now()));
        commande.setStatut("En attente");
        return commandeRepository.save(commande);
    }

    public void creerCommandeAvecLignes(int clientId, Map<String, String> formData) {
        // 1. Créer la commande

        Client cl = clientRepository.findById(clientId).orElse(null);

        Commande commande = new Commande();
        commande.setClient(cl);
        commande.setDateCommande(LocalDate.now());
        commande.setStatut("EN ATTENTE");
        commande = commandeRepository.save(commande);

        // 2. Parcourir les données de type produits[ID].selected
        for (String key : formData.keySet()) {
            if (key.matches("produits\\[\\d+\\]\\.selected")) {
                // extraire l'id du produit depuis "produits[42].selected"
                int produitId = Integer.parseInt(key.replaceAll("\\D+", ""));
                Produit produit = produitRepository.findById(produitId).orElse(null);
                String quantiteKey = "produits[" + produitId + "].quantite";
                int quantite = Integer.parseInt(formData.get(quantiteKey));

                // mis a jour quantite
                Integer quantiteActuel = produitService.getQuantiteProduit(produitId);
                Integer nouveauQuantity = quantiteActuel - quantite;
                produitService.updateLotProduit(produitId, nouveauQuantity);


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
}
