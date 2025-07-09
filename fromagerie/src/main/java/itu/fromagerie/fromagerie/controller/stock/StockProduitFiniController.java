package itu.fromagerie.fromagerie.controller.stock;

import itu.fromagerie.fromagerie.dto.stock.StockStatDTO;
import itu.fromagerie.fromagerie.service.stock.StockProduitFiniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stock/produit-fini")
@CrossOrigin(origins = "*")
@Tag(name = "Stock Produit Fini", description = "Endpoints pour gérer le stock de produits finis")
public class StockProduitFiniController {

    @Autowired
    private StockProduitFiniService stockService;

    /**
     * Récupère les statistiques de stock de produit fini
     */
    @GetMapping("/stat")
    @Operation(
        summary = "Obtenir les statistiques de stock produit fini",
        description = "Récupère les statistiques de stock des produits finis entre deux dates, en prenant en compte le nombre de clients et la quantité moyenne demandée",
        tags = {"Stock", "Statistiques"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Statistiques de stock récupérées avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockStatDTO.class))
        )
    })
    public StockStatDTO getStatStock(
        @Parameter(description = "Date de début pour les statistiques", required = true)
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        
        @Parameter(description = "Date de fin pour les statistiques", required = true)
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
        
        @Parameter(description = "Nombre de clients pour le calcul des seuils de demande", required = true)
        @RequestParam int nbClients,
        
        @Parameter(description = "Quantité moyenne demandée par client", required = true)
        @RequestParam int qttDemandee
    ) {
        return stockService.getStatStock(dateDebut, dateFin, nbClients, qttDemandee);
    }
} 