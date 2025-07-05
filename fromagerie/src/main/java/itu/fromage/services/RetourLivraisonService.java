package itu.fromage.services;

import itu.fromage.entities.RetourLivraison;
import itu.fromage.repositories.RetourLivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RetourLivraisonService {
    private final RetourLivraisonRepository retourLivraisonRepository;

    @Autowired
    public RetourLivraisonService(RetourLivraisonRepository retourLivraisonRepository) {
        this.retourLivraisonRepository = retourLivraisonRepository;
    }

    public List<RetourLivraison> findAll() {
        return retourLivraisonRepository.findAll();
    }

    public Optional<RetourLivraison> findById(Integer id) {
        return retourLivraisonRepository.findById(id);
    }

    public RetourLivraison save(RetourLivraison retourLivraison) {
        return retourLivraisonRepository.save(retourLivraison);
    }

    public void deleteById(Integer id) {
        retourLivraisonRepository.deleteById(id);
    }
}
