package itu.fromagerie.fromagerie.controller.stock;

import itu.fromagerie.fromagerie.dto.stock.MouvementStockDTO;
import itu.fromagerie.fromagerie.service.stock.MouvementStockProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stock/mouvement")
@CrossOrigin(origins = "*")
public class MouvementStockProduitController {

    @Autowired
    private MouvementStockProduitService mouvementService;

    @GetMapping
    public List<MouvementStockDTO> getMouvementsByPeriode(
            @Parameter(description = "ID de la matière première (optionnel)") @RequestParam(required = false) Long matiereId,
            @Parameter(description = "Date de début (format YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin (format YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return mouvementService.getMouvements(matiereId, dateDebut, dateFin);
    }

    @GetMapping("/courant")
    public List<MouvementStockDTO> getMouvementsCourant() {
        return mouvementService.getStockCourant();
    }

    // @GetMapping
    // public List<MouvementDTO> getMouvements(
    // @RequestParam(required = false) @DateTimeFormat(iso =
    // DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
    // @RequestParam(required = false) @DateTimeFormat(iso =
    // DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
    // @RequestParam(required = false) String type) {
    // return mouvementService.getMouvements(dateDebut, dateFin, type);
    // }
}