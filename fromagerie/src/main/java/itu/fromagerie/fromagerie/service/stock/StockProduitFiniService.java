package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.StockStatDTO;
import itu.fromagerie.fromagerie.repository.stock.StockProduitFiniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class StockProduitFiniService {

    @Autowired
    private StockProduitFiniRepository stockRepo;

    public StockStatDTO getStatStock(LocalDate dateDebut, LocalDate dateFin, int nbClients, int qttDemandee) {
        // Utiliser les données réelles de stock depuis le repository
        BigDecimal quantiteBD = stockRepo.getQuantiteTotaleStockDisponible();
        int quantite = quantiteBD != null ? quantiteBD.intValue() : 0;
        
        // Calculer le seuil critique basé sur les meilleures pratiques de l'industrie fromagère
        // Généralement, on considère un seuil critique de 30% du stock total maximal
        // Nous pourrions affiner cela avec des données historiques
        int seuilCritique = (int) Math.ceil(0.3 * quantite);
        
        // Calcul du seuil de demande basé sur le nombre de clients et la quantité moyenne demandée
        int seuilDemande = nbClients * qttDemandee;
        
        // Remplir le DTO avec les valeurs calculées à partir des données réelles
        StockStatDTO dto = new StockStatDTO();
        dto.date = dateFin;
        dto.quantiteTotale = quantite;
        dto.seuilCritiqueValeur = seuilCritique;
        dto.seuilDemandeValeur = seuilDemande;
        dto.seuilCritique = quantite <= seuilCritique;
        dto.suffisant = quantite >= seuilDemande;
        
        return dto;
    }

    /**
     * Récupère la quantité totale de produits finis disponibles en stock.
     * 
     * @param dateDebut Date de début pour filtrer les données (peut être ignorée si le repository ne gère pas les filtres par date)
     * @param dateFin Date de fin pour filtrer les données (peut être ignorée si le repository ne gère pas les filtres par date)
     * @return La quantité totale disponible en stock sous forme de BigDecimal
     */
    public BigDecimal getQuantiteTotale(LocalDate dateDebut, LocalDate dateFin) {
        // Utiliser le repository pour obtenir la quantité totale de stock disponible
        BigDecimal quantite = stockRepo.getQuantiteTotaleStockDisponible();
        
        // Si la valeur est null, retourner zéro pour éviter les NullPointerExceptions
        return quantite != null ? quantite : BigDecimal.ZERO;
    }
}