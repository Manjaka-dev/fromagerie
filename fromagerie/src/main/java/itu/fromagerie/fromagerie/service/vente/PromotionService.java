package itu.fromagerie.fromagerie.service.vente;

import itu.fromagerie.fromagerie.entities.livraison.StatutLivraison;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.vente.Promotion;
import itu.fromagerie.fromagerie.entities.vente.PromotionCommande;
import itu.fromagerie.fromagerie.repository.livraison.StatutLivraisonRepository;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;
import itu.fromagerie.fromagerie.repository.vente.PromotionCommandeRepository;
import itu.fromagerie.fromagerie.repository.vente.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final ProduitRepository produitRepository;
    private final StatutLivraisonRepository stLR;
    private final PromotionCommandeRepository promotionCommandeRepository;
    @Autowired
    public PromotionService(PromotionRepository promotionRepository,
                            ProduitRepository produitRepository,
                            StatutLivraisonRepository stLR,
                            PromotionCommandeRepository promotionCommandeRepository)
    {
        this.promotionRepository = promotionRepository;
        this.produitRepository = produitRepository;
        this.stLR = stLR;
        this.promotionCommandeRepository = promotionCommandeRepository;
    }

    public void save (Long produitID, String nom, String description,
                      BigDecimal reduction, LocalDate date_debut, LocalDate date_fin)
    {
        Produit produit = produitRepository.findById(produitID).orElse(null);
        Promotion promotion = new Promotion();
        promotion.setNom(nom);
        promotion.setProduit(produit);
        promotion.setDescription(description);
        promotion.setDateFin(date_fin);
        promotion.setDateDebut(date_debut);
        promotion.setReductionPourcentage(reduction);
        promotionRepository.save(promotion);
    }

    public boolean findPromotionCommande (Long idProduit, LocalDate dateCommande) {
        Promotion promotion = promotionRepository.findPromotionCommande(idProduit, dateCommande);
        return promotion != null;
    }

    public boolean findStatutLivraisonCommande(Long idCommande) {
        StatutLivraison sl = stLR.findStatutCommande(idCommande);
        return !sl.getStatut().equalsIgnoreCase("Annulée");
    }

    public boolean findStatutPromotionCommande(Long idCommande) {
        PromotionCommande pc = promotionCommandeRepository.findStatutPromotionCommande(idCommande);
        return  pc != null;
    }
}
