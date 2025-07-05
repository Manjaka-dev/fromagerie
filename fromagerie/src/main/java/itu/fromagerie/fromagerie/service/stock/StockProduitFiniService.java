package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.StockStatDTO;
import itu.fromagerie.fromagerie.repository.stock.StockProduitFiniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StockProduitFiniService {

    @Autowired
    private StockProduitFiniRepository stockRepo;

    public StockStatDTO getStatStock(LocalDate dateDebut, LocalDate dateFin, int nbClients, int qttDemandee) {
        int quantite = stockRepo.getQuantiteTotaleByPeriode(dateDebut, dateFin);
        int seuilCritique = (int) Math.ceil(0.5 * quantite);
        int seuilDemande = nbClients * qttDemandee;
        StockStatDTO dto = new StockStatDTO();
        dto.date = dateFin;
        dto.quantiteTotale = quantite;
        dto.seuilCritiqueValeur = seuilCritique;
        dto.seuilDemandeValeur = seuilDemande;
        dto.seuilCritique = quantite <= seuilCritique;
        dto.suffisant = quantite >= seuilDemande;
        return dto;
    }
} 