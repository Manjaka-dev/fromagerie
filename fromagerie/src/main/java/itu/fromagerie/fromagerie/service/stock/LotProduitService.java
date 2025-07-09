package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.LotTraceDTO;
import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import itu.fromagerie.fromagerie.repository.produit.LotProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LotProduitService {

    @Autowired
    private LotProduitRepository lotRepo;

    public List<LotTraceDTO> getLotsByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        // TODO: Implémenter findLotsByPeriode dans LotProduitRepository
        // Pour l'instant, retourner une liste vide
        // return new ArrayList<>();
        
        // Code original commenté :
        List<LotProduit> lots = lotRepo.findLotsByPeriode(dateDebut, dateFin);
        return lots.stream()
                .map(this::mapToLotTraceDTO)
                .collect(Collectors.toList());
    }

    public LotTraceDTO getLotByNumero(String numeroLot) {
        // TODO: Implémenter findLotByNumero dans LotProduitRepository
        // Pour l'instant, retourner null
        return null;
        
        // Code original commenté :
        // return lotRepo.findLotByNumero(numeroLot)
        //         .map(this::mapToLotTraceDTO)
        //         .orElse(null);
    }
    
    private LotTraceDTO mapToLotTraceDTO(LotProduit lot) {
        LotTraceDTO dto = new LotTraceDTO();
        dto.numeroLot = lot.getNumeroLot();
        dto.dateFabrication = lot.getDateFabrication();
        dto.poids = lot.getQuantite() != null ? lot.getQuantite() : 0;
        dto.etat = lot.getDatePeremption() != null && lot.getDatePeremption().isBefore(LocalDate.now()) ? "Périmé" : "Valide";
        dto.quantite = lot.getQuantite() != null ? lot.getQuantite() : 0;
        return dto;
    }
} 