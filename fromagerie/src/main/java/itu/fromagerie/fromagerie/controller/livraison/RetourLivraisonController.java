package itu.fromagerie.fromagerie.controller.livraison;

import itu.fromagerie.fromagerie.entities.livraison.RetourLivraison;
import itu.fromagerie.fromagerie.service.livraison.RetourLivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RetourLivraison> getById(@PathVariable Long id) {
        Optional<RetourLivraison> retour = retourLivraisonService.findById(id);
        return retour.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RetourLivraison create(@RequestBody RetourLivraison retourLivraison) {
        return retourLivraisonService.save(retourLivraison);
    }

    @PutMapping("/{id}")
    public RetourLivraison update(@PathVariable Long id, @RequestBody RetourLivraison retourLivraison) {
        retourLivraison.setId(id);
        return retourLivraisonService.save(retourLivraison);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        retourLivraisonService.deleteById(id);
    }
}
