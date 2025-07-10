package itu.fromagerie.fromagerie.controller.stock;

import itu.fromagerie.fromagerie.dto.stock.SimulationDTO;
import itu.fromagerie.fromagerie.service.stock.SimulationProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stock/simulation")

public class SimulationProductionController {

    @Autowired
    private SimulationProductionService simulationService;

    @GetMapping
    public List<SimulationDTO> getSimulations(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
    ) {
        return simulationService.getSimulations(dateDebut, dateFin);
    }
} 