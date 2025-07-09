package itu.fromagerie.fromagerie.service.stock;

import itu.fromagerie.fromagerie.dto.stock.MouvementStockDTO;
import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import itu.fromagerie.fromagerie.entities.stock.MouvementStockProduit;
import itu.fromagerie.fromagerie.repository.produit.LotProduitRepository;
import itu.fromagerie.fromagerie.repository.stock.MouvementStockProduitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MouvementStockProduitService {

    @Autowired
    private MouvementStockProduitRepository mouvementRepo;

    @Autowired
    private LotProduitRepository produitService;

    // public List<MouvementDTO> getMouvements(LocalDateTime dateDebut,
    // LocalDateTime dateFin, String type) {
    // // TODO: Implémenter les méthodes manquantes dans
    // // MouvementStockProduitRepository
    // // Pour l'instant, retourner une liste vide
    // // return new ArrayList<>();

    // // Code original commenté :
    // List<MouvementStockProduit> mouvements;
    // if (type == null) {
    // mouvements = mouvementRepo.findMouvementsByDate(dateDebut, dateFin);
    // } else {
    // mouvements = mouvementRepo.findMouvementsByTypeAndDate(type, dateDebut,
    // dateFin);
    // }
    // return mouvements.stream()
    // .map(this::mapToMouvementDTO)
    // .collect(Collectors.toList());
    // }

    public List<MouvementStockDTO> getMouvements(Long produitId, LocalDate dateDebut, LocalDate dateFin) {
        List<MouvementStockProduit> mouvements;

        if (produitId != null && dateDebut != null && dateFin != null) {
            mouvements = mouvementRepo.findByProduitIdAndDateMouvementBetween(
                    produitId, dateDebut.atStartOfDay(), dateFin.atTime(23, 59, 59));
        } else if (produitId != null) {
            mouvements = mouvementRepo.findByProduitIdOrderByDateMouvementDesc(produitId);
        } else if (dateDebut != null && dateFin != null) {
            mouvements = mouvementRepo.findByDateMouvementBetween(
                    dateDebut.atStartOfDay(), dateFin.atTime(23, 59, 59));
        } else {
            mouvements = mouvementRepo.findAllByOrderByDateMouvementDesc();
        }

        return mouvements.stream()
                .map(this::convertToMouvementStockDTO) // Tu dois avoir une méthode équivalente pour produit
                .collect(Collectors.toList());
    }

    public List<MouvementStockDTO> getStockCourant() {
        List<LotProduit> produits = produitService.findAll();
        List<MouvementStockDTO> mouvementsCourants = getMouvements(null, null, null);
        List<MouvementStockDTO> stockCourant = new ArrayList<>();
        for (LotProduit produit : produits) {
            MouvementStockDTO mouvementCourant = new MouvementStockDTO();
            mouvementCourant.setMatiereId(produit.getId());
            mouvementCourant.setNomMatiere(produit.getProduit().getNom());
            mouvementCourant.setLot(produit.getNumeroLot()); // Pas de lot pour le stock courant
            mouvementCourant.setQuantite(BigDecimal.ZERO);
            mouvementCourant.setTypeMouvement("STOCK_COURANT");
            mouvementCourant.setDateMouvement(LocalDateTime.now().toString());
            mouvementCourant.setCommentaire("Stock courant");

            // Calculer la quantité totale pour ce produit
            for (MouvementStockDTO mouvement : mouvementsCourants) {
                if (mouvement.getMatiereId().equals(produit.getId())) {
                    mouvementCourant.setQuantite(mouvementCourant.getQuantite().add(mouvement.getQuantite()));
                }
            }
            stockCourant.add(mouvementCourant);
        }
        return stockCourant;
    }

    // private MouvementDTO mapToMouvementDTO(MouvementStockProduit mouvement) {
    //     MouvementDTO dto = new MouvementDTO();
    //     dto.id = mouvement.getId();
    //     dto.typeMouvement = mouvement.getTypeMouvement();
    //     dto.quantite = mouvement.getQuantite() != null ? mouvement.getQuantite() : 0;
    //     dto.dateMouvement = mouvement.getDateMouvement();
    //     dto.commentaire = mouvement.getCommentaire();
    //     dto.lot = mouvement.getLot() != null ? mouvement.getLot().getNumeroLot() : null;
    //     return dto;
    // }

    private MouvementStockDTO convertToMouvementStockDTO(MouvementStockProduit mouvement) {
        MouvementStockDTO dto = new MouvementStockDTO();
        dto.setId(mouvement.getId());
        dto.setMatiereId(mouvement.getLot().getId());
        dto.setLot(mouvement.getLot().getNumeroLot());
        dto.setNomMatiere(mouvement.getLot().getProduit().getNom());
        dto.setTypeMouvement(mouvement.getTypeMouvement());
        dto.setQuantite(
                mouvement.getQuantite() != null ? BigDecimal.valueOf(mouvement.getQuantite()) : BigDecimal.ZERO);
        dto.setDateMouvement(mouvement.getDateMouvement().toString());
        dto.setCommentaire(mouvement.getCommentaire());
        return dto;
    }

    public List<Object> getMouvementsByDate(LocalDateTime dateDebut, LocalDateTime dateFin) {
        // TODO: Implémenter findMouvementsByDate dans MouvementStockProduitRepository
        // Pour l'instant, retourner une liste vide
        return new ArrayList<>();

        // Code original commenté :
        // return mouvementRepo.findMouvementsByDate(dateDebut, dateFin);
    }

    public List<Object> getMouvementsByTypeAndDate(String type, LocalDateTime dateDebut, LocalDateTime dateFin) {
        // TODO: Implémenter findMouvementsByTypeAndDate dans
        // MouvementStockProduitRepository
        // Pour l'instant, retourner une liste vide
        return new ArrayList<>();

        // Code original commenté :
        // return mouvementRepo.findMouvementsByTypeAndDate(type, dateDebut, dateFin);
    }
}