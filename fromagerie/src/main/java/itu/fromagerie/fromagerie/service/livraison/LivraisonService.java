package itu.fromagerie.fromagerie.service.livraison;

import itu.fromagerie.fromagerie.dto.livraison.CreateLivraisonDTO;
import itu.fromagerie.fromagerie.dto.livraison.LivraisonInfoDTO;
import itu.fromagerie.fromagerie.dto.livraison.UpdateLivraisonDTO;
import itu.fromagerie.fromagerie.entities.livraison.*;
import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.repository.livraison.*;
import itu.fromagerie.fromagerie.repository.vente.CommandeRepository;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivraisonService {
    
    @Autowired
    private LivraisonRepository livraisonRepository;
    
    @Autowired
    private LivraisonProduitRepository livraisonProduitRepository;
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private LivreurRepository livreurRepository;
    
    @Autowired
    private ProduitRepository produitRepository;
    
    @Autowired
    private StatutLivraisonRepository statutLivraisonRepository;
    
    // ==================== CRUD OPERATIONS ====================
    
    /**
     * Crée une nouvelle livraison
     */
    @Transactional
    public LivraisonInfoDTO createLivraison(CreateLivraisonDTO createDTO) {
        // Vérifier que la commande existe
        Commande commande = commandeRepository.findById(createDTO.getCommandeId())
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        
        // Vérifier que le livreur existe
        Livreur livreur = livreurRepository.findById(createDTO.getLivreurId())
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        
        // Créer la livraison
        Livraison livraison = new Livraison();
        livraison.setCommande(commande);
        livraison.setLivreur(livreur);
        livraison.setDateLivraison(createDTO.getDateLivraison());
        livraison.setZone(createDTO.getZone());
        livraison.setStatutLivraison(createDTO.getStatutLivraison());
        
        // Sauvegarder la livraison
        livraison = livraisonRepository.save(livraison);
        
        // Ajouter les produits à livrer
        if (createDTO.getProduitsALivrer() != null) {
            for (CreateLivraisonDTO.ProduitLivraisonCreateDTO produitDTO : createDTO.getProduitsALivrer()) {
                Produit produit = produitRepository.findById(produitDTO.getProduitId())
                        .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
                
                LivraisonProduit livraisonProduit = new LivraisonProduit();
                livraisonProduit.setLivraison(livraison);
                livraisonProduit.setProduit(produit);
                livraisonProduit.setQuantiteALivrer(produitDTO.getQuantiteALivrer());
                livraisonProduit.setQuantiteLivree(0);
                
                livraisonProduitRepository.save(livraisonProduit);
            }
        }
        
        // Créer un historique de statut initial
        createStatutHistory(livraison, createDTO.getStatutLivraison(), "Livraison créée");
        
        return convertToDTO(livraisonRepository.findById(livraison.getId()).orElse(livraison));
    }
    
    /**
     * Récupère une livraison par ID
     */
    public Optional<LivraisonInfoDTO> getLivraisonById(Long id) {
        return livraisonRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * Récupère toutes les livraisons
     */
    public List<LivraisonInfoDTO> getAllLivraisons() {
        return livraisonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Met à jour une livraison
     */
    @Transactional
    public LivraisonInfoDTO updateLivraison(Long id, UpdateLivraisonDTO updateDTO) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));
        
        // Mettre à jour les champs de base
        if (updateDTO.getLivreurId() != null) {
            Livreur livreur = livreurRepository.findById(updateDTO.getLivreurId())
                    .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
            livraison.setLivreur(livreur);
        }
        
        if (updateDTO.getDateLivraison() != null) {
            livraison.setDateLivraison(updateDTO.getDateLivraison());
        }
        
        if (updateDTO.getZone() != null) {
            livraison.setZone(updateDTO.getZone());
        }
        
        if (updateDTO.getStatutLivraison() != null) {
            StatutLivraisonEnum ancienStatut = livraison.getStatutLivraison();
            livraison.setStatutLivraison(updateDTO.getStatutLivraison());
            
            // Créer un historique si le statut a changé
            if (!ancienStatut.equals(updateDTO.getStatutLivraison())) {
                createStatutHistory(livraison, updateDTO.getStatutLivraison(), "Statut mis à jour");
            }
        }
        
        // Mettre à jour les produits si fournis
        if (updateDTO.getProduitsALivrer() != null) {
            updateProduitsLivraison(livraison, updateDTO.getProduitsALivrer());
        }
        
        livraison = livraisonRepository.save(livraison);
        return convertToDTO(livraison);
    }
    
    /**
     * Supprime une livraison
     */
    @Transactional
    public void deleteLivraison(Long id) {
        if (!livraisonRepository.existsById(id)) {
            throw new RuntimeException("Livraison non trouvée");
        }
        livraisonRepository.deleteById(id);
    }
    
    /**
     * Récupère toutes les informations de livraison pour une période donnée
     */
    public List<LivraisonInfoDTO> getLivraisonsInfo(LocalDate dateDebut, LocalDate dateFin) {
        List<Livraison> livraisons = livraisonRepository.findLivraisonsCompletes(dateDebut, dateFin);
        return livraisons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère les livraisons par statut
     */
    public List<LivraisonInfoDTO> getLivraisonsByStatut(StatutLivraisonEnum statut) {
        List<Livraison> livraisons = livraisonRepository.findByStatutWithDetails(statut);
        return livraisons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère les livraisons d'un livreur spécifique
     */
    public List<LivraisonInfoDTO> getLivraisonsByLivreur(Long livreurId) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        
        List<Livraison> livraisons = livraisonRepository.findByLivreur(livreur);
        return livraisons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère les livraisons d'une zone spécifique
     */
    public List<LivraisonInfoDTO> getLivraisonsByZone(String zone) {
        List<Livraison> livraisons = livraisonRepository.findByZone(zone);
        return livraisons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère les livraisons d'un livreur pour une période donnée
     */
    public List<LivraisonInfoDTO> getLivraisonsByLivreurEtPeriode(Long livreurId, LocalDate dateDebut, LocalDate dateFin) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        
        List<Livraison> livraisons = livraisonRepository.findByLivreur(livreur).stream()
                .filter(l -> !l.getDateLivraison().isBefore(dateDebut) && !l.getDateLivraison().isAfter(dateFin))
                .collect(Collectors.toList());
                
        return livraisons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité Livraison en DTO
     */
    private LivraisonInfoDTO convertToDTO(Livraison livraison) {
        LivraisonInfoDTO dto = new LivraisonInfoDTO();
        
        // Informations de base
        dto.setLivraisonId(livraison.getId());
        dto.setDateLivraison(livraison.getDateLivraison());
        dto.setStatut(livraison.getStatutLivraison());
        dto.setZone(livraison.getZone());
        
        // Informations commande
        if (livraison.getCommande() != null) {
            dto.setCommandeId(livraison.getCommande().getId());
            dto.setDateCommande(livraison.getCommande().getDateCommande());
            
            // Informations client
            if (livraison.getCommande().getClient() != null) {
                dto.setClientId(livraison.getCommande().getClient().getId());
                dto.setNomClient(livraison.getCommande().getClient().getNom());
                dto.setTelephoneClient(livraison.getCommande().getClient().getTelephone());
                dto.setAdresseClient(livraison.getCommande().getClient().getAdresse());
            }
        }
        
        // Informations livreur
        if (livraison.getLivreur() != null) {
            dto.setLivreurId(livraison.getLivreur().getId());
            dto.setNomLivreur(livraison.getLivreur().getNom());
            dto.setTelephoneLivreur(livraison.getLivreur().getTelephone());
        }
        
        // Produits à livrer
        if (livraison.getProduitsALivrer() != null) {
            List<LivraisonInfoDTO.ProduitLivraisonDTO> produits = livraison.getProduitsALivrer().stream()
                    .map(this::convertProduitToDTO)
                    .collect(Collectors.toList());
            dto.setProduitsALivrer(produits);
        }
        
        return dto;
    }
    
    /**
     * Convertit un LivraisonProduit en DTO
     */
    private LivraisonInfoDTO.ProduitLivraisonDTO convertProduitToDTO(LivraisonProduit livraisonProduit) {
        LivraisonInfoDTO.ProduitLivraisonDTO dto = new LivraisonInfoDTO.ProduitLivraisonDTO();
        
        if (livraisonProduit.getProduit() != null) {
            dto.setProduitId(livraisonProduit.getProduit().getId());
            dto.setNomProduit(livraisonProduit.getProduit().getNom());
            dto.setQuantiteALivrer(livraisonProduit.getQuantiteALivrer());
            dto.setQuantiteLivree(livraisonProduit.getQuantiteLivree());
            
            if (livraisonProduit.getProduit().getCategorie() != null) {
                dto.setCategorieProduit(livraisonProduit.getProduit().getCategorie().getNom());
            }
        }
        
        return dto;
    }
    
    /**
     * Met à jour le statut d'une livraison avec historique
     */
    @Transactional
    public void updateStatutLivraison(Long livraisonId, StatutLivraisonEnum nouveauStatut, String commentaire) {
        Livraison livraison = livraisonRepository.findById(livraisonId)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));
        
        StatutLivraisonEnum ancienStatut = livraison.getStatutLivraison();
        livraison.setStatutLivraison(nouveauStatut);
        livraisonRepository.save(livraison);
        
        // Créer un historique si le statut a changé
        if (!ancienStatut.equals(nouveauStatut)) {
            createStatutHistory(livraison, nouveauStatut, commentaire != null ? commentaire : "Statut mis à jour");
        }
    }
    
    // ==================== MÉTHODES UTILITAIRES ====================
    
    /**
     * Crée un historique de statut
     */
    private void createStatutHistory(Livraison livraison, StatutLivraisonEnum statut, String commentaire) {
        StatutLivraison statutHistory = new StatutLivraison();
        statutHistory.setLivraison(livraison);
        statutHistory.setStatut(statut.name());
        statutHistory.setDateStatut(LocalDateTime.now());
        statutLivraisonRepository.save(statutHistory);
    }
    
    /**
     * Met à jour les produits d'une livraison
     */
    private void updateProduitsLivraison(Livraison livraison, List<UpdateLivraisonDTO.ProduitLivraisonUpdateDTO> produitsDTO) {        
        for (UpdateLivraisonDTO.ProduitLivraisonUpdateDTO produitDTO : produitsDTO) {
            if (produitDTO.getId() != null) {
                // Mise à jour d'un produit existant
                LivraisonProduit existing = livraisonProduitRepository.findById(produitDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Produit de livraison non trouvé"));
                
                existing.setQuantiteALivrer(produitDTO.getQuantiteALivrer());
                if (produitDTO.getQuantiteLivree() != null) {
                    existing.setQuantiteLivree(produitDTO.getQuantiteLivree());
                }
                livraisonProduitRepository.save(existing);
            } else {
                // Nouveau produit
                Produit produit = produitRepository.findById(produitDTO.getProduitId())
                        .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
                
                LivraisonProduit nouveauProduit = new LivraisonProduit();
                nouveauProduit.setLivraison(livraison);
                nouveauProduit.setProduit(produit);
                nouveauProduit.setQuantiteALivrer(produitDTO.getQuantiteALivrer());
                nouveauProduit.setQuantiteLivree(produitDTO.getQuantiteLivree() != null ? produitDTO.getQuantiteLivree() : 0);
                
                livraisonProduitRepository.save(nouveauProduit);
            }
        }
    }
}
