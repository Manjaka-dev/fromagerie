package itu.fromage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {

    @GetMapping
    public ModelAndView home() {
        return new ModelAndView("hello");
    }

    @GetMapping("/commandes")
    public ModelAndView commandes() {
        return new ModelAndView("commandes");
    }

    @GetMapping("/livraisons")
    public ModelAndView livraisons() {
        return new ModelAndView("livraisons");
    }

    @GetMapping("/produits")
    public ModelAndView produits() {
        return new ModelAndView("produits");
    }

    @GetMapping("/create/livraison/{id}")
    public ModelAndView createLivraison(@PathVariable("id") int id) {
        ModelAndView mv = new ModelAndView("livraison-form");
        mv.addObject("id", id);
        return mv;
    }

    @GetMapping("/confirmation-paiement/{livraisonId}")
    public ModelAndView confirmationPaiement(@PathVariable("livraisonId") Integer livraisonId) {
        ModelAndView mv = new ModelAndView("confirmation-paiement");
        mv.addObject("livraisonId", livraisonId);
        return mv;
    }
} 