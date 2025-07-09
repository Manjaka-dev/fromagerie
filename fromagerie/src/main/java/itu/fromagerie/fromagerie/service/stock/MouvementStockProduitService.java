package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.MouvementDTO;
import itu.fromagerie.fromagerie.entities.stock.MouvementStockProduit;
import itu.fromagerie.fromagerie.repository.stock.MouvementStockProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MouvementStockProduitService {

    @Autowired
    private MouvementStockProduitRepository mouvementRepo;

    public List<MouvementDTO> getMouvements(LocalDateTime dateDebut, LocalDateTime dateFin, String type) {
        List<MouvementStockProduit> mouvements;
        if (type == null) {
            mouvements = mouvementRepo.findMouvementsByDate(dateDebut, dateFin);
        } else {
            mouvements = mouvementRepo.findMouvementsByTypeAndDate(type, dateDebut, dateFin);
        }
        return mouvements.stream()
                .map(this::mapToMouvementDTO)
                .collect(Collectors.toList());
    }
    
    private MouvementDTO mapToMouvementDTO(MouvementStockProduit mouvement) {
        MouvementDTO dto = new MouvementDTO();
        dto.id = mouvement.getId();
        dto.typeMouvement = mouvement.getTypeMouvement();
        dto.quantite = mouvement.getQuantite() != null ? mouvement.getQuantite() : 0;
        dto.dateMouvement = mouvement.getDateMouvement();
        dto.commentaire = mouvement.getCommentaire();
        dto.lot = mouvement.getLot() != null ? mouvement.getLot().getNumeroLot() : null;
        return dto;
    }

    public List<MouvementStockProduit> getMouvementsByDate(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return mouvementRepo.findMouvementsByDate(dateDebut, dateFin);
    }

    public List<MouvementStockProduit> getMouvementsByTypeAndDate(String type, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return mouvementRepo.findMouvementsByTypeAndDate(type, dateDebut, dateFin);
    }
} 