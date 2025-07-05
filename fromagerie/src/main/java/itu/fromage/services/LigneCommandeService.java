package itu.fromage.services;

import itu.fromage.entities.Commande;
import itu.fromage.entities.LigneCommande;
import itu.fromage.entities.Produit;
import itu.fromage.repositories.LigneCommandeRepository;
import org.springframework.stereotype.Service;

@Service
public class LigneCommandeService {
    private final LigneCommandeRepository ligneCommandeRepository;

    public LigneCommandeService(LigneCommandeRepository ligneCommandeRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    public void saveLigneCommande (Commande commandeID, Produit produitID, Integer quantiteProduit) {
        LigneCommande lc = new LigneCommande();
        lc.setCommande(commandeID);
        lc.setProduit(produitID);
        lc.setQuantite(quantiteProduit);
        ligneCommandeRepository.save(lc);
    }
}
