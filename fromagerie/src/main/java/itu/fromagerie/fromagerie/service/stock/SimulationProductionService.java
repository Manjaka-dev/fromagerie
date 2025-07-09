package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.SimulationDTO;
import itu.fromagerie.fromagerie.repository.stock.SimulationProductionRepository;
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
        return new ArrayList<>();
    }

    public List<SimulationDTO> getSimulations() {
        return simulationRepo.findAll()
                .stream()
                .map(s -> {
                    SimulationDTO dto = new SimulationDTO();
                    dto.quantiteAProduire = s.getQuantiteSuggeree();
                    dto.stockActuel = 0; // à compléter
                    dto.demandeEstimee = 0; // à compléter
                    dto.nbClients = 0; // à compléter
                    dto.moyenneQttDemandee = 0; // à compléter
                    return dto;
                }).toList();
    }
} 