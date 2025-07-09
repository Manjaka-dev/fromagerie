package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.SimulationDTO;
import itu.fromagerie.fromagerie.repository.stock.SimulationProductionRepository;
import itu.fromagerie.fromagerie.entities.stock.SimulationProduction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationProductionService {

    @Autowired
    private SimulationProductionRepository simulationRepo;

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
                
                // Pour l'instant nous utilisons des valeurs fixes mais 
                // idéalement elles devraient provenir d'autres repositories
                dto.stockActuel = 0; 
                dto.demandeEstimee = 0;
                dto.nbClients = 0;
                dto.moyenneQttDemandee = 0;
                
                return dto;
            }).toList();
    }
}