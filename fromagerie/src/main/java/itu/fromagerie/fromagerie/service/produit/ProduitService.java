package itu.fromagerie.fromagerie.service.produit;

import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.produit.CategorieProduit;
import itu.fromagerie.fromagerie.dto.produit.ProduitSearchDTO;
import itu.fromagerie.fromagerie.dto.produit.CategorieProduitDTO;
import itu.fromagerie.fromagerie.dto.produit.ProduitStatsDTO;
import itu.fromagerie.fromagerie.repository.produit.LotProduitRepository;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;
import itu.fromagerie.fromagerie.repository.produit.CategorieProduitRepository;
import itu.fromagerie.fromagerie.repository.vente.LigneCommandeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProduitService {
    private final ProduitRepository produitRepository;
    private final LotProduitRepository lotProduitRepository;
    private final CategorieProduitRepository categorieProduitRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    
    @Autowired
    public ProduitService(ProduitRepository produitRepository, LotProduitRepository lotProduitRepository, 
                         CategorieProduitRepository categorieProduitRepository, LigneCommandeRepository ligneCommandeRepository) {
        this.produitRepository = produitRepository;
        this.lotProduitRepository = lotProduitRepository;
        this.categorieProduitRepository = categorieProduitRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
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
    
    // Recherche de produits par nom
    public List<ProduitSearchDTO> searchProduitsByNom(String nom) {
        List<Produit> produits = produitRepository.searchProduitsByNom(nom);
        return produits.stream()
                .map(this::convertToProduitSearchDTO)
                .collect(Collectors.toList());
    }
    
    // Liste des catégories avec nombre de produits
    public List<CategorieProduitDTO> getAllCategories() {
        List<CategorieProduit> categories = categorieProduitRepository.findAll();
        return categories.stream()
                .map(this::convertToCategorieDTO)
                .collect(Collectors.toList());
    }
    
    // Statistiques des produits
    public ProduitStatsDTO getProduitStats() {
        List<Produit> produits = produitRepository.findAll();
        List<Produit> produitsStockFaible = produitRepository.findProduitsStockFaible();
        List<Produit> produitsEnRupture = produitRepository.findProduitsEnRupture();
        
        // Calculer les produits les plus vendus
        List<ProduitStatsDTO.ProduitPlusVenduDTO> produitsPlusVendus = ligneCommandeRepository.getProduitsPlusVendus()
                .stream()
                .limit(5) // Top 5
                .map(this::convertToProduitPlusVenduDTO)
                .collect(Collectors.toList());
        
        // Produits avec stock faible
        List<ProduitStatsDTO.ProduitStockFaibleDTO> produitsStockFaibleDTO = produitsStockFaible.stream()
                .map(this::convertToProduitStockFaibleDTO)
                .collect(Collectors.toList());
        
        // Calculer le chiffre d'affaires total
        BigDecimal chiffreAffairesTotal = ligneCommandeRepository.getChiffreAffairesTotal();
        if (chiffreAffairesTotal == null) {
            chiffreAffairesTotal = BigDecimal.ZERO;
        }
        
        return new ProduitStatsDTO(
                produitsPlusVendus,
                produitsStockFaibleDTO,
                chiffreAffairesTotal,
                produits.size(),
                produitsEnRupture.size()
        );
    }
    
    // Produits avec stock faible
    public List<ProduitSearchDTO> getProduitsStockFaible() {
        List<Produit> produits = produitRepository.findProduitsStockFaible();
        return produits.stream()
                .map(this::convertToProduitSearchDTO)
                .collect(Collectors.toList());
    }
    
    // Ajustement de stock manuel
    @Transactional
    public void ajusterStock(Long produitId, Integer quantite, String type) {
        Produit produit = getProduitById(produitId);
        if (produit == null) {
            throw new IllegalArgumentException("Produit non trouvé");
        }
        
        int quantiteActuelle = getQuantiteProduit(produitId);
        int nouvelleQuantite;
        
        if ("ajout".equals(type)) {
            nouvelleQuantite = quantiteActuelle + quantite;
        } else if ("retrait".equals(type)) {
            nouvelleQuantite = quantiteActuelle - quantite;
            if (nouvelleQuantite < 0) {
                throw new IllegalArgumentException("Stock insuffisant pour le retrait");
            }
        } else {
            throw new IllegalArgumentException("Type d'ajustement invalide. Utilisez 'ajout' ou 'retrait'");
        }
        
        updateLotProduit(produitId, nouvelleQuantite);
    }
    
    // Méthodes de conversion
    private ProduitSearchDTO convertToProduitSearchDTO(Produit produit) {
        ProduitSearchDTO dto = new ProduitSearchDTO();
        dto.setId(produit.getId());
        dto.setNom(produit.getNom());
        dto.setPoids(produit.getPoids());
        dto.setPrixVente(produit.getPrixVente());
        dto.setPrixRevient(produit.getPrixRevient());
        dto.setIngredients(produit.getIngredients());
        dto.setAllergenes(produit.getAllergenes());
        dto.setDatePeremption(produit.getDatePeremption());
        dto.setCategorieNom(produit.getCategorie() != null ? produit.getCategorie().getNom() : null);
        dto.setQuantiteDisponible(getQuantiteProduit(produit.getId()));
        return dto;
    }
    
    private CategorieProduitDTO convertToCategorieDTO(CategorieProduit categorie) {
        CategorieProduitDTO dto = new CategorieProduitDTO();
        dto.setId(categorie.getId());
        dto.setNom(categorie.getNom());
        dto.setNombreProduits(categorie.getProduits() != null ? categorie.getProduits().size() : 0);
        return dto;
    }
    
    private ProduitStatsDTO.ProduitPlusVenduDTO convertToProduitPlusVenduDTO(Object[] result) {
        ProduitStatsDTO.ProduitPlusVenduDTO dto = new ProduitStatsDTO.ProduitPlusVenduDTO();
        dto.setId(((Number) result[0]).longValue());
        dto.setNom((String) result[1]);
        dto.setQuantiteVendue(((Number) result[2]).intValue());
        dto.setChiffreAffaires((BigDecimal) result[3]);
        return dto;
    }
    
    private ProduitStatsDTO.ProduitStockFaibleDTO convertToProduitStockFaibleDTO(Produit produit) {
        ProduitStatsDTO.ProduitStockFaibleDTO dto = new ProduitStatsDTO.ProduitStockFaibleDTO();
        dto.setId(produit.getId());
        dto.setNom(produit.getNom());
        dto.setQuantiteDisponible(getQuantiteProduit(produit.getId()));
        dto.setSeuilAlerte(10); // Seuil par défaut
        return dto;
    }
}
