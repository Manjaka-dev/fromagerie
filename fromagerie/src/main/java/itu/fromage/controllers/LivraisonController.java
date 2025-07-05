package itu.fromage.controllers;

import itu.fromage.entities.Livraison;
import itu.fromage.entities.Commande;
import itu.fromage.entities.Livreur;
import itu.fromage.entities.StatutLivraison;
import itu.fromage.projection.LivraisonProjection;
import itu.fromage.services.LivraisonService;
import itu.fromage.services.RetourLivraisonService;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Controller
@RequestMapping("/")
public class LivraisonController {
    private final LivraisonService livraisonService;
    private final RetourLivraisonService retourLivraisonService;

    @Autowired
    public LivraisonController(LivraisonService livraisonService, RetourLivraisonService retourLivraisonService) {
        this.livraisonService = livraisonService;
        this.retourLivraisonService = retourLivraisonService;
    }

    @GetMapping("/commandes")
    public ModelAndView showCommand() {
        ModelAndView mv = new ModelAndView("commandes");
        mv.addObject("liste", livraisonService.getCommandeLivraison());
        mv.addObject("livreur", livraisonService.listeLivreur());
        return mv;
    }

    @GetMapping("/create/livraison/{id}")
    public ModelAndView showFormLivraison(@PathVariable("id") int id){
        ModelAndView mv = new ModelAndView("livraison-form");
        mv.addObject("id", id);
        mv.addObject("livreurs", livraisonService.listeLivreur());
        return mv;
    }

    @PostMapping("/create/commandeID")
    public ModelAndView getCommandeID(@RequestParam("commandeId") Integer commandeID){
        return new ModelAndView("redirect:/create/livraison/"+commandeID);
    }

    @PostMapping("/livraison/save")
    public ModelAndView saveLivraison(
            @RequestParam("livreur") Integer livreur,
            @RequestParam("dateLivraison") LocalDate dateLivraison,
            @RequestParam("commandeId") Integer commandeId,
            @RequestParam("zone") String zone
            ){
        livraisonService.saveLivraison(commandeId, dateLivraison, zone, livreur);
        return new ModelAndView("redirect:/commandes");
    }


    @PostMapping("/assign-livreur")
    public String assignLivreur(@RequestParam("commandeId") Integer commandeId,
                                @RequestParam("livreurId") Integer livreurId,
                                RedirectAttributes redirectAttributes) {
        try {
            livraisonService.assignLivreur(commandeId, livreurId);
            redirectAttributes.addFlashAttribute("message", "Livreur assigné avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'assignation du livreur : " + e.getMessage());
        }
        return "redirect:/commandes";
    }


    @GetMapping("/livraisons")
    public ModelAndView showLivraisonDetails() {
        ModelAndView mv = new ModelAndView("livraisons");
        List<LivraisonProjection> livraisons = livraisonService.getLivraisonDetails();
        if (livraisons.isEmpty()) {
            System.out.println("Aucune livraison trouvée !");
        }
        mv.addObject("livraisons", livraisons);
        return mv;
    }



    @PostMapping("/add-statut")
    public String addStatutLivraison(@RequestParam("livraisonId") Integer livraisonId,
                                     RedirectAttributes redirectAttributes) {

        if (livraisonId == null) {
            redirectAttributes.addFlashAttribute("error", "ID de livraison invalide.");
            return "redirect:/livraisons";
        }

        Optional<Livraison> livraisonOpt = livraisonService.findLivraisonById(livraisonId);
        if (livraisonOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Livraison non trouvée.");
            return "redirect:/livraisons";
        }

        LivraisonProjection livraisonProjection = livraisonService.getLivraisonDetails()
                .stream()
                .filter(l -> l.getLivraisonId().equals(livraisonId))
                .findFirst()
                .orElse(null);

        if (livraisonProjection == null) {
            redirectAttributes.addFlashAttribute("error", "Détails de la livraison introuvables.");
            return "redirect:/livraisons";
        }

        // Création du nouveau statut
        StatutLivraison nouveauStatut = new StatutLivraison();
        nouveauStatut.setLivraison(livraisonOpt.get());

        String statutActuel = livraisonProjection.getStatutLivraison();
        if ("En cours".equalsIgnoreCase(statutActuel)) {
            nouveauStatut.setStatut("Livrée");
        } else {
            nouveauStatut.setStatut("En cours");
        }

        nouveauStatut.setDateStatut(Instant.now());
        livraisonService.saveStatutLivraison(nouveauStatut);

        redirectAttributes.addFlashAttribute("message", "Nouveau statut ajouté avec succès !");
        return "redirect:/livraisons";
    }


    @PostMapping("/retour-livraison")
    public String retourLivraison(@RequestParam("commandeId") Integer commandeId, RedirectAttributes redirectAttributes) {
        try {
            livraisonService.annulerCommandeEtRetourLivraison(commandeId);
            redirectAttributes.addFlashAttribute("message", "Commande annulée et retour livraison enregistré !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du retour livraison : " + e.getMessage());
        }
        return "redirect:/commandes";
    }

    @GetMapping("/commandes/export-pdf/{id}")
    public void exportCommandePdf(@PathVariable("id") Integer commandeId, HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=commande_" + commandeId + ".pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            var commandes = livraisonService.getCommandeLivraison();
            var commande = commandes.stream()
                    .filter(c -> c.getCommandeId().equals(commandeId))
                    .findFirst()
                    .orElse(null);

            if (commande != null) {
                document.add(new Paragraph("Commande n°" + commande.getCommandeId()));
                document.add(new Paragraph("Client : " + commande.getClientNom()));
                document.add(new Paragraph("Date commande : " +
                        (commande.getDateCommande() != null ? commande.getDateCommande().toString() : "")));
                document.add(new Paragraph(" "));

                PdfPTable table = new PdfPTable(2);
                table.addCell("Produit");
                table.addCell("Quantité");

                for (var p : commande.getProduits()) {
                    table.addCell(p.getNom());
                    table.addCell(p.getQuantite().toString());
                }
                document.add(table);
            } else {
                document.add(new Paragraph("Commande introuvable."));
            }
        } catch (DocumentException | IOException e) {
            // Log l'erreur et renvoyer une réponse d'erreur
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Erreur lors de la génération du PDF: " + e.getMessage());
            } catch (IOException ex) {
                // Si même l'écriture de l'erreur échoue
                throw new RuntimeException("Erreur critique lors de la génération du PDF", ex);
            }
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }

}