package itu.fromagerie.fromagerie.controller.stock;

import itu.fromagerie.fromagerie.dto.stock.StockStatDTO;
import itu.fromagerie.fromagerie.service.stock.StockProduitFiniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stock/produit-fini")
@CrossOrigin(origins = "*")
public class StockProduitFiniController {

    @Autowired
    private StockProduitFiniService stockService;

    @GetMapping
    public StockStatDTO getStatStockAll() {
        return stockService.getAll();
    }

    @GetMapping("/stat")
    public StockStatDTO getStatStock(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam int nbClients,
            @RequestParam int qttDemandee) {
        return stockService.getStatStock(dateDebut, dateFin, nbClients, qttDemandee);
    }
}