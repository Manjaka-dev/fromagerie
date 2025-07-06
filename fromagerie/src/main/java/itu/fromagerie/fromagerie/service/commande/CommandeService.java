package itu.fromagerie.fromagerie.service.commande;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.vente.LigneCommande;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.vente.Client;
import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import itu.fromagerie.fromagerie.repository.vente.CommandeRepository;
import itu.fromagerie.fromagerie.repository.vente.LigneCommandeRepository;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;
import itu.fromagerie.fromagerie.repository.vente.ClientRepository;
import itu.fromagerie.fromagerie.repository.produit.LotProduitRepository;
import itu.fromagerie.fromagerie.service.produit.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import itu.fromagerie.fromagerie.dto.commande.CommandeDTO;

@Service
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final ProduitRepository produitRepository;
    private final ClientRepository clientRepository;
    private final ProduitService produitService;
    private final LotProduitRepository lotProduitRepository;
    @Autowired
    public CommandeService(CommandeRepository commandeRepository, LigneCommandeRepository ligneCommandeRepository, ProduitRepository produitRepository, ClientRepository clientRepository, ProduitService produitService, LotProduitRepository lotProduitRepository) {
        this.commandeRepository = commandeRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.produitRepository = produitRepository;
        this.clientRepository = clientRepository;
        this.produitService = produitService;
        this.lotProduitRepository = lotProduitRepository;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findCommandesSansLivraison();
    }
    
    public List<CommandeDTO> getAllCommandesAsDTO() {
        List<Commande> commandes = commandeRepository.findCommandesSansLivraison();
        System.out.println("=== COMMANDES SANS LIVRAISON ===");
        System.out.println("Nombre de commandes trouvées: " + commandes.size());
        for (Commande commande : commandes) {
            System.out.println("Commande ID: " + commande.getId() + " - Client: " + 
                              (commande.getClient() != null ? commande.getClient().getNom() : "Client inconnu"));
        }
        System.out.println("=== FIN COMMANDES SANS LIVRAISON ===");
        return commandes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    private CommandeDTO convertToDTO(Commande commande) {
        CommandeDTO dto = new CommandeDTO();
        dto.setId(commande.getId());
        dto.setDateCommande(commande.getDateCommande());
        dto.setStatut(commande.getStatut());
        dto.setMontantTotal(commande.getMontantTotal());
        
        // Convertir le client
        if (commande.getClient() != null) {
            CommandeDTO.ClientDTO clientDTO = new CommandeDTO.ClientDTO();
            clientDTO.setId(commande.getClient().getId());
            clientDTO.setNom(commande.getClient().getNom());
            clientDTO.setTelephone(commande.getClient().getTelephone());
            clientDTO.setAdresse(commande.getClient().getAdresse());
            dto.setClient(clientDTO);
        }
        
        // Convertir les lignes de commande
        if (commande.getLignesCommande() != null) {
            List<CommandeDTO.LigneCommandeDTO> lignesDTO = commande.getLignesCommande().stream()
                .map(this::convertLigneToDTO)
                .collect(Collectors.toList());
            dto.setLignesCommande(lignesDTO);
        }
        
        return dto;
    }
    
    private CommandeDTO.LigneCommandeDTO convertLigneToDTO(LigneCommande ligne) {
        CommandeDTO.LigneCommandeDTO ligneDTO = new CommandeDTO.LigneCommandeDTO();
        ligneDTO.setId(ligne.getId());
        ligneDTO.setQuantite(ligne.getQuantite());
        
        // Utiliser le prix unitaire de la ligne s'il existe, sinon utiliser le prix du produit
        BigDecimal prixUnitaire = ligne.getPrixUnitaire();
        if (prixUnitaire == null && ligne.getProduit() != null) {
            prixUnitaire = ligne.getProduit().getPrixVente();
        }
        ligneDTO.setPrixUnitaire(prixUnitaire);
        
        // Convertir le produit
        if (ligne.getProduit() != null) {
            CommandeDTO.ProduitDTO produitDTO = new CommandeDTO.ProduitDTO();
            produitDTO.setId(ligne.getProduit().getId());
            produitDTO.setNom(ligne.getProduit().getNom());
            produitDTO.setPrixVente(ligne.getProduit().getPrixVente());
            produitDTO.setPoids(ligne.getProduit().getPoids());
            produitDTO.setIngredients(ligne.getProduit().getIngredients());
            ligneDTO.setProduit(produitDTO);
        }
        
        return ligneDTO;
    }

    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    public Commande updateCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }

    public int getLastInsertCommandID(){
        return commandeRepository.getLastInsertCommandeId().intValue();
    }

    public Commande saveCommande(Long idClient){
        Commande commande = new Commande();
        commande.setDateCommande(LocalDate.from(Instant.now()));
        commande.setStatut("En attente");
        return commandeRepository.save(commande);
    }

    public void creerCommandeAvecLignes(Long clientId, Map<String, String> formData) {
        // 1. Créer la commande

        Client cl = clientRepository.findById(clientId).orElse(null);

        Commande commande = new Commande();
        commande.setClient(cl);
        commande.setDateCommande(LocalDate.now());
        commande.setStatut("EN ATTENTE");
        commande = commandeRepository.save(commande);

        // 2. Parcourir les données de type produits[ID].selected
        for (String key : formData.keySet()) {
            if (key.matches("produits\\[\\d+\\]\\.selected")) {
                // extraire l'id du produit depuis "produits[42].selected"
                Long produitId = Long.valueOf(key.replaceAll("\\D+", ""));
                Produit produit = produitRepository.findById(produitId).orElse(null);
                String quantiteKey = "produits[" + produitId + "].quantite";
                int quantite = Integer.parseInt(formData.get(quantiteKey));

                // mis a jour quantite
                Integer quantiteActuel = produitService.getQuantiteProduit(produitId);
                Integer nouveauQuantity = quantiteActuel - quantite;
                produitService.updateLotProduit(produitId, nouveauQuantity);


                // facultatif : vérifier si le produit existe
                if (quantite > 0) {
                    LigneCommande ligne = new LigneCommande();
                    ligne.setCommande(commande);
                    ligne.setProduit(produit);
                    ligne.setQuantite(quantite);
                    ligne.setPrixUnitaire(produit.getPrixVente());
                    ligneCommandeRepository.save(ligne);
                }
            }
        }
    }

    public void saveCommande(Commande commande, List<LigneCommande> lignesCommande) {
        // Sauvegarder la commande
        commande = commandeRepository.save(commande);
        
        // Sauvegarder les lignes de commande
        for (LigneCommande ligne : lignesCommande) {
            ligne.setCommande(commande);
            ligneCommandeRepository.save(ligne);
        }
    }

    public void saveCommandeWithClient(Long clientId, LocalDate dateCommande, String statut) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new IllegalArgumentException("Client non trouvé");
        }

        Commande commande = new Commande();
        commande.setClient(client);
        commande.setDateCommande(dateCommande);
        commande.setStatut(statut);
        
        commandeRepository.save(commande);
    }

    public void saveLigneCommande(Long commandeId, Long produitId, int quantite) {
        Commande commande = commandeRepository.findById(commandeId).orElse(null);
        Produit produit = produitRepository.findById(produitId).orElse(null);
        
        if (commande == null || produit == null) {
            throw new IllegalArgumentException("Commande ou produit non trouvé");
        }

        LigneCommande ligne = new LigneCommande();
        ligne.setCommande(commande);
        ligne.setProduit(produit);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(produit.getPrixVente());
        
        ligneCommandeRepository.save(ligne);
    }

    public Commande createCommandeWithProducts(Long clientId, String dateCommande, List<Map<String, Object>> produits) {
        // 1. Récupérer le client
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + clientId));

        // 2. Créer la commande
        Commande commande = new Commande();
        commande.setClient(client);
        commande.setStatut("en_attente");
        
        // Parser la date si fournie, sinon utiliser la date actuelle
        if (dateCommande != null && !dateCommande.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(dateCommande);
                commande.setDateCommande(date);
            } catch (Exception e) {
                commande.setDateCommande(LocalDate.now());
            }
        } else {
            commande.setDateCommande(LocalDate.now());
        }

        // 3. Sauvegarder la commande
        commande = commandeRepository.save(commande);

        // 4. Calculer le montant total
        BigDecimal montantTotal = BigDecimal.ZERO;

        // 5. Créer les lignes de commande pour chaque produit
        for (Map<String, Object> produitData : produits) {
            Long produitId = Long.valueOf(produitData.get("produitId").toString());
            Integer quantite = Integer.valueOf(produitData.get("quantite").toString());
            
            // Récupérer le produit
            Produit produit = produitRepository.findById(produitId)
                    .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé avec l'ID: " + produitId));

            // Créer la ligne de commande
            LigneCommande ligneCommande = new LigneCommande();
            ligneCommande.setCommande(commande);
            ligneCommande.setProduit(produit);
            ligneCommande.setQuantite(quantite);
            ligneCommande.setPrixUnitaire(produit.getPrixVente());
            
            // Sauvegarder la ligne de commande
            ligneCommandeRepository.save(ligneCommande);

            // Ajouter au montant total
            BigDecimal prixLigne = produit.getPrixVente().multiply(BigDecimal.valueOf(quantite));
            montantTotal = montantTotal.add(prixLigne);

            // Mettre à jour le stock (diminuer la quantité disponible)
            Integer quantiteActuelle = produitService.getQuantiteProduit(produitId);
            Integer nouvelleQuantite = quantiteActuelle - quantite;
            if (nouvelleQuantite < 0) {
                throw new IllegalArgumentException("Stock insuffisant pour le produit: " + produit.getNom());
            }
            produitService.updateLotProduit(produitId, nouvelleQuantite);
        }

        // 6. Mettre à jour le montant total de la commande
        commande.setMontantTotal(montantTotal);
        commande = commandeRepository.save(commande);
        
        return commande;
    }
}
