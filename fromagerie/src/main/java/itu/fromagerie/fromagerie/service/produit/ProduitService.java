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
}
