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
        // Utiliser des vraies données de stock au lieu des valeurs en dur
        
        // Convertir BigDecimal en int pour la compatibilité avec le DTO
        BigDecimal quantiteBD = stockRepo.getQuantiteTotaleStockDisponible();
        int quantite = quantiteBD != null ? quantiteBD.intValue() : 0;
        
        // Calculer les seuils basés sur les valeurs réelles
        int seuilCritique = (int) Math.ceil(0.5 * quantite);
        int seuilDemande = nbClients * qttDemandee;
        
        // Remplir le DTO avec les valeurs calculées
        StockStatDTO dto = new StockStatDTO();
        dto.date = dateFin;
        dto.quantiteTotale = quantite;
        dto.seuilCritiqueValeur = seuilCritique;
        dto.seuilDemandeValeur = seuilDemande;
        dto.seuilCritique = quantite <= seuilCritique;
        dto.suffisant = quantite >= seuilDemande;
        
        return dto;
    }

    public BigDecimal getQuantiteTotale(LocalDate dateDebut, LocalDate dateFin) {
        // Utiliser le repository pour obtenir la quantité totale de stock
        BigDecimal quantite = stockRepo.getQuantiteTotaleStockDisponible();
        
        // Si la valeur est null, retourner zéro
        return quantite != null ? quantite : BigDecimal.ZERO;
    }
}