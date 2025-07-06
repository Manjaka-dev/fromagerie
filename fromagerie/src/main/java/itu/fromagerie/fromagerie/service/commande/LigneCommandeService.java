package itu.fromagerie.fromagerie.service.commande;

import itu.fromagerie.fromagerie.entities.vente.LigneCommande;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.repository.vente.LigneCommandeRepository;
import itu.fromagerie.fromagerie.repository.vente.CommandeRepository;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LigneCommandeService {
    private final LigneCommandeRepository ligneCommandeRepository;
    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;

    @Autowired
    public LigneCommandeService(LigneCommandeRepository ligneCommandeRepository, CommandeRepository commandeRepository, ProduitRepository produitRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.commandeRepository = commandeRepository;
        this.produitRepository = produitRepository;
    }

    public void saveLigneCommande(Long commandeId, Long produitId, Integer quantite) {
        Commande commande = commandeRepository.findById(commandeId).orElse(null);
        Produit produit = produitRepository.findById(produitId).orElse(null);
        
        if (commande == null || produit == null) {
            throw new IllegalArgumentException("Commande ou produit non trouvé");
        }

        LigneCommande lc = new LigneCommande();
        lc.setCommande(commande);
        lc.setProduit(produit);
        lc.setQuantite(quantite);
        
        ligneCommandeRepository.save(lc);
    }

    public List<LigneCommande> getAllLigneCommandes() {
        return ligneCommandeRepository.findAll();
    }

    public LigneCommande getLigneCommandeById(Long id) {
        return ligneCommandeRepository.findById(id).orElse(null);
    }

    public LigneCommande updateLigneCommande(LigneCommande ligneCommande) {
        return ligneCommandeRepository.save(ligneCommande);
    }

    public void deleteLigneCommande(Long id) {
        ligneCommandeRepository.deleteById(id);
    }
}
