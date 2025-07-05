package itu.fromagerie.fromagerie.controller.stock;

import itu.fromagerie.fromagerie.dto.stock.MouvementDTO;
import itu.fromagerie.fromagerie.service.stock.MouvementStockProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock/mouvement")
@CrossOrigin(origins = "*")
public class MouvementStockProduitController {

    @Autowired
    private MouvementStockProduitService mouvementService;

    @GetMapping
    public List<MouvementDTO> getMouvements(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
        @RequestParam(required = false) String type
    ) {
        return mouvementService.getMouvements(dateDebut, dateFin, type);
    }
} 