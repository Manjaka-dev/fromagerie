package itu.fromage.services;

import itu.fromage.entities.Produit;
import itu.fromage.repositories.LotProduitRepository;
import itu.fromage.repositories.ProduitRepository;
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

    public int getQuantiteProduit(int id) {
        Integer quantite = lotProduitRepository.getQuantiteProduit(id);
        return quantite != null ? quantite : 0; // Return 0 if quantite is null
    }

    @Transactional
    public void updateLotProduit(int idProduit, int quantite) {
        lotProduitRepository.updateLotProduitBy(idProduit, quantite);
    }

//    public Produit getProduitByID(Integer idProduit) {
//        return produitRepository.findById(idProduit);
//    }


}
