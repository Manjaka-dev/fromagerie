package itu.fromagerie.fromagerie.service.produit;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.repository.produit.LotProduitRepository;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {
    private final ProduitRepository produitRepository;
    private final LotProduitRepository lotProduitRepository;
    @Autowired
    public ProduitService(ProduitRepository produitRepository, LotProduitRepository lotProduitRepository) {
        this.produitRepository = produitRepository;
        this.lotProduitRepository = lotProduitRepository;
    }

    public List<Produit> AllProduit(){
        return produitRepository.findAll();
    }

    public int getQuantiteProduit(Long id) {
        Integer quantite = lotProduitRepository.getQuantiteProduit(id.intValue());
        return quantite != null ? quantite : 0; // Return 0 if quantite is null
    }

    @Transactional
    public void updateLotProduit(Long idProduit, int quantite) {
        lotProduitRepository.updateLotProduitBy(idProduit.intValue(), quantite);
    }

    public Produit getProduitById(Long idProduit) {
        return produitRepository.findById(idProduit).orElse(null);
    }

    public Produit saveProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public Produit updateProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    /**
     * Ajuste le stock d'un produit avec une nouvelle quantité et trace l'opération
     * @param produitId ID du produit à ajuster
     * @param ajustement Informations d'ajustement (quantite, raison)
     * @return Map contenant les informations de l'opération et le résultat
     */
    @Transactional
    public java.util.Map<String, Object> ajusterStock(Long produitId, java.util.Map<String, Object> ajustement) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        
        try {
            // Vérifier que le produit existe
            Produit produit = getProduitById(produitId);
            if (produit == null) {
                response.put("success", false);
                response.put("message", "Produit non trouvé");
                return response;
            }
            
            // Récupérer la quantité actuelle
            int quantiteActuelle = getQuantiteProduit(produitId);
            
            // Récupérer les informations d'ajustement
            Integer nouvelleQuantite = null;
            if (ajustement.get("quantite") instanceof Integer) {
                nouvelleQuantite = (Integer) ajustement.get("quantite");
            } else if (ajustement.get("quantite") instanceof String) {
                try {
                    nouvelleQuantite = Integer.parseInt((String) ajustement.get("quantite"));
                } catch (NumberFormatException e) {
                    response.put("success", false);
                    response.put("message", "La quantité doit être un nombre");
                    return response;
                }
            }
            
            if (nouvelleQuantite == null) {
                response.put("success", false);
                response.put("message", "Quantité non spécifiée");
                return response;
            }
            
            String raison = ajustement.containsKey("raison") ? (String) ajustement.get("raison") : "Ajustement manuel";
            
            // Effectuer la mise à jour
            updateLotProduit(produitId, nouvelleQuantite);
            
            // Log ou enregistrement de l'opération peut être ajouté ici
            // Exemple : mouvementStockService.createMouvement(produitId, "AJUSTEMENT", nouvelleQuantite, raison);
            
            response.put("success", true);
            response.put("produitId", produitId);
            response.put("ancienneQuantite", quantiteActuelle);
            response.put("nouvelleQuantite", nouvelleQuantite);
            response.put("variation", nouvelleQuantite - quantiteActuelle);
            response.put("raison", raison);
            response.put("message", "Stock ajusté avec succès");
            
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de l'ajustement du stock: " + e.getMessage());
            return response;
        }
    }
}
