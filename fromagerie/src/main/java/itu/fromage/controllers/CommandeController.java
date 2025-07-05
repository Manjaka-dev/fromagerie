package itu.fromage.controllers;

import itu.fromage.entities.Commande;
import itu.fromage.services.CommandeService;
import itu.fromage.services.LigneCommandeService;
import itu.fromage.services.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("api/commande")
public class CommandeController {
    private final ProduitService produitService;
    private final LigneCommandeService ligneCommandeService;
    private final CommandeService commandeService;
    @Autowired
    public CommandeController(ProduitService produitService, LigneCommandeService ligneCommandeService, CommandeService commandeService) {
        this.produitService = produitService;
        this.ligneCommandeService = ligneCommandeService;
        this.commandeService = commandeService;
    }

    @PostMapping("/panier")
    public ModelAndView ajouterAuPanier(
            @RequestParam("clientId") int clientId,
            @RequestParam Map<String, String> formData
    ) {

        commandeService.creerCommandeAvecLignes(clientId, formData);
        return new ModelAndView("redirect:/commandes");
    }


}
