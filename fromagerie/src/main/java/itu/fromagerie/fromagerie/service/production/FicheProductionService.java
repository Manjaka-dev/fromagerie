package itu.fromagerie.fromagerie.service.production;

import org.springframework.stereotype.Service;

import itu.fromagerie.fromagerie.dto.FicheProductionDTO;
import itu.fromagerie.fromagerie.entities.production.FicheProduction;
import itu.fromagerie.fromagerie.entities.produit.CategorieProduit;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import itu.fromagerie.fromagerie.repository.production.FicheProductionRepository;
import itu.fromagerie.fromagerie.repository.produit.CategorieProduitRepository;
import itu.fromagerie.fromagerie.repository.produit.ProduitRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FicheProductionService {

    private final FicheProductionRepository ficheProductionRepo;
    private final ProduitRepository produitRepository;
    private final CategorieProduitRepository categorieRepository;

    public FicheProductionService(
            FicheProductionRepository ficheProductionRepo,
            ProduitRepository produitRepository,
            CategorieProduitRepository categorieRepository) {
        this.ficheProductionRepo = ficheProductionRepo;
        this.produitRepository = produitRepository;
        this.categorieRepository = categorieRepository;
    }

    /**
     * Retrieves all production sheet records for a given product.
     * @param produit The product to query production sheets for.
     * @return List of production sheet records.
     * @throws IllegalArgumentException if produit is null.
     */
    public List<FicheProduction> findByProduit(Produit produit) {
        Objects.requireNonNull(produit, "Produit cannot be null");
        return ficheProductionRepo.findByProduit(produit);
    }

    /**
     * Retrieves all production sheet records for a given raw material.
     * @param matiere The raw material to query production sheets for.
     * @return List of production sheet records.
     * @throws IllegalArgumentException if matiere is null.
     */
    public List<FicheProduction> findByMatiere(MatierePremiere matiere) {
        Objects.requireNonNull(matiere, "MatierePremiere cannot be null");
        return ficheProductionRepo.findByMatiere(matiere);
    }

    /**
     * Saves a new production sheet record.
     * @param ficheProduction The production sheet to save.
     * @return The saved production sheet.
     * @throws IllegalArgumentException if ficheProduction is null.
     */
    public FicheProduction saveFicheProduction(FicheProduction ficheProduction) {
        Objects.requireNonNull(ficheProduction, "FicheProduction cannot be null");
        return ficheProductionRepo.save(ficheProduction);
    }

    /**
     * Retrieves a production sheet by its ID.
     * @param id The ID of the production sheet.
     * @return The production sheet, or null if not found.
     * @throws IllegalArgumentException if id is null.
     */
    public FicheProduction findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        Optional<FicheProduction> fiche = ficheProductionRepo.findById(id);
        return fiche.orElse(null);
    }
    
    /**
     * Crée une fiche de production à partir d'un DTO.
     * @param ficheDTO Le DTO contenant les informations de la fiche de production.
     * @return La fiche de production créée et sauvegardée.
     */
    public FicheProduction creerFicheProductionDepuisDTO(FicheProductionDTO ficheDTO) {
        // Rechercher ou créer la catégorie
        CategorieProduit categorie = null;
        if (ficheDTO.getCategorie() != null && !ficheDTO.getCategorie().isEmpty()) {
            try {
                // D'abord, chercher par nom exact
                categorie = categorieRepository.findByNom(ficheDTO.getCategorie()).orElse(null);
                
                if (categorie == null) {
                    // Si non trouvé, créer une nouvelle catégorie
                    CategorieProduit nouvCat = new CategorieProduit();
                    nouvCat.setNom(ficheDTO.getCategorie());
                    categorie = categorieRepository.save(nouvCat);
                    System.out.println("Nouvelle catégorie créée: " + categorie.getNom() + " avec ID: " + categorie.getId());
                } else {
                    System.out.println("Catégorie existante trouvée: " + categorie.getNom() + " avec ID: " + categorie.getId());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la recherche/création de la catégorie: " + e.getMessage());
                e.printStackTrace();
                throw new IllegalArgumentException("Impossible de créer ou retrouver la catégorie: " + e.getMessage());
            }
        }
        
        // Créer et sauvegarder le produit
        Produit produit = new Produit();
        produit.setNom(ficheDTO.getNomProduit());
        produit.setCategorie(categorie);
        
        // Conversion des champs
        try {
            if (ficheDTO.getPoids() != null && !ficheDTO.getPoids().isEmpty()) {
                produit.setPoids(new BigDecimal(ficheDTO.getPoids().replace(" ", "")));
            }
            if (ficheDTO.getPrixVente() != null && !ficheDTO.getPrixVente().isEmpty()) {
                produit.setPrixVente(new BigDecimal(ficheDTO.getPrixVente().replace(" ", "")));
            }
            if (ficheDTO.getPrixRevient() != null && !ficheDTO.getPrixRevient().isEmpty()) {
                produit.setPrixRevient(new BigDecimal(ficheDTO.getPrixRevient().replace(" ", "")));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Format de nombre invalide: " + e.getMessage());
        }
        
        produit.setIngredients(ficheDTO.getIngredients());
        produit.setAllergenes(ficheDTO.getAllergenes());
        
        if (ficheDTO.getDateExpiration() != null && !ficheDTO.getDateExpiration().isEmpty()) {
            produit.setDatePeremption(LocalDate.parse(ficheDTO.getDateExpiration()));
        }
        
        produit = produitRepository.save(produit);
        
        // Créer et sauvegarder la fiche de production
        FicheProduction fiche = new FicheProduction();
        fiche.setProduit(produit);
        fiche.setQuantiteNecessaire(ficheDTO.getQuantiteNecessaire() != null ? 
            ficheDTO.getQuantiteNecessaire() : BigDecimal.ONE);
        
        return ficheProductionRepo.save(fiche);
    }
}