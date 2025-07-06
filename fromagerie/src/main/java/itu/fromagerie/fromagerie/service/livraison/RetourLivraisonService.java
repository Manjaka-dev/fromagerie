package itu.fromagerie.fromagerie.service.livraison;

import itu.fromagerie.fromagerie.entities.livraison.RetourLivraison;
import itu.fromagerie.fromagerie.repository.livraison.RetourLivraisonRepository;
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

    public Optional<RetourLivraison> findById(Long id) {
        return retourLivraisonRepository.findById(id);
    }

    public RetourLivraison save(RetourLivraison retourLivraison) {
        return retourLivraisonRepository.save(retourLivraison);
    }

    public void deleteById(Long id) {
        retourLivraisonRepository.deleteById(id);
    }
}
