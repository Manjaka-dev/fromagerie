package itu.fromage.controllers;

import itu.fromage.entities.Client;
import itu.fromage.entities.Produit;
import itu.fromage.services.ClientService;
import itu.fromage.services.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;
    private final ClientService clientService;
    
    @Autowired
    public ProduitController(ProduitService produitService, ClientService clientService) {
        this.produitService = produitService;
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProduits() {
        List<Produit> produits = produitService.AllProduit();

        List<Integer> quantites = produits.stream()
                .map(produit -> produitService.getQuantiteProduit(produit.getId()))
                .collect(Collectors.toList());

        List<Client> clients = clientService.AllClient();

        Map<String, Object> response = new HashMap<>();
        response.put("produits", produits);
        response.put("quantiteDisponible", quantites);
        response.put("listeClient", clients);

        return ResponseEntity.ok(response);
    }
}
