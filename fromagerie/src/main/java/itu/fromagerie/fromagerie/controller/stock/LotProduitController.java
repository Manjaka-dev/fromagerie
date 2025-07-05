package itu.fromagerie.fromagerie.controller.stock;

import itu.fromagerie.fromagerie.dto.stock.LotTraceDTO;
import itu.fromagerie.fromagerie.service.stock.LotProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stock/lot")
@CrossOrigin(origins = "*")
public class LotProduitController {

    @Autowired
    private LotProduitService lotService;

    @GetMapping("/periode")
    public List<LotTraceDTO> getLotsByPeriode(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
    ) {
        return lotService.getLotsByPeriode(dateDebut, dateFin);
    }

    @GetMapping("/{numeroLot}")
    public LotTraceDTO getLotByNumero(@PathVariable String numeroLot) {
        return lotService.getLotByNumero(numeroLot);
    }
} 