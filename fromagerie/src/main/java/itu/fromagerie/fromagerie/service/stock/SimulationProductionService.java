package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.SimulationDTO;
import itu.fromagerie.fromagerie.repository.stock.SimulationProductionRepository;
import itu.fromagerie.fromagerie.repository.stock.StockProduitFiniRepository;
import itu.fromagerie.fromagerie.repository.vente.LigneCommandeRepository;
import itu.fromagerie.fromagerie.repository.vente.ClientRepository;
import itu.fromagerie.fromagerie.entities.stock.SimulationProduction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Service
public class SimulationProductionService {

    @Autowired
    private SimulationProductionRepository simulationRepo;
    
    @Autowired
    private StockProduitFiniRepository stockRepo;
    
    @Autowired
    private LigneCommandeRepository ligneCommandeRepo;
    
    @Autowired
    private ClientRepository clientRepo;

    public List<SimulationDTO> getSimulations(LocalDate dateDebut, LocalDate dateFin) {
        // Utiliser le repository pour récupérer les données de la base de données
        List<SimulationProduction> simulations = simulationRepo.findByDateSimulationBetween(
                dateDebut.atStartOfDay(), 
                dateFin.plusDays(1).atStartOfDay());
        
        // Convertir les entités en DTOs
        return simulations.stream()
            .map(s -> {
                SimulationDTO dto = new SimulationDTO();
                dto.quantiteAProduire = s.getQuantiteSuggeree();
                
                // Récupérer le stock actuel des produits finis
                BigDecimal stockTotal = stockRepo.getQuantiteTotaleStockDisponible();
                dto.stockActuel = stockTotal != null ? stockTotal.intValue() : 0;
                
                // Récupérer la demande estimée (total des quantités des lignes de commande)
                Integer demandeTotal = ligneCommandeRepo.getTotalQuantiteByProduit(s.getProduit());
                dto.demandeEstimee = demandeTotal != null ? demandeTotal : 0;
                
                // Récupérer le nombre de clients
                Long nbClients = (long) clientRepo.findAll().size();
                dto.nbClients = nbClients.intValue();
                
                // Calculer la moyenne des quantités demandées
                dto.moyenneQttDemandee = dto.nbClients > 0 ? dto.demandeEstimee / dto.nbClients : 0;
                
                return dto;
            }).toList();
    }
}