package itu.fromage.controllers;

import itu.fromage.entities.RetourLivraison;
import itu.fromage.services.RetourLivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/retours-livraison")
public class RetourLivraisonController {
    private final RetourLivraisonService retourLivraisonService;

    @Autowired
    public RetourLivraisonController(RetourLivraisonService retourLivraisonService) {
        this.retourLivraisonService = retourLivraisonService;
    }

    @GetMapping
    public List<RetourLivraison> getAll() {
        return retourLivraisonService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<RetourLivraison> getById(@PathVariable Integer id) {
        return retourLivraisonService.findById(id);
    }

    @PostMapping
    public RetourLivraison create(@RequestBody RetourLivraison retourLivraison) {
        return retourLivraisonService.save(retourLivraison);
    }

    @PutMapping("/{id}")
    public RetourLivraison update(@PathVariable Integer id, @RequestBody RetourLivraison retourLivraison) {
        retourLivraison.setId(id);
        return retourLivraisonService.save(retourLivraison);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        retourLivraisonService.deleteById(id);
    }
}
