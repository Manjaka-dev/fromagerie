package itu.fromagerie.fromagerie.repository.produit;

import itu.fromagerie.fromagerie.entities.produit.LotProduit;
import itu.fromagerie.fromagerie.entities.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LotProduitRepository extends JpaRepository<LotProduit, Long> {
    List<LotProduit> findByProduit(Produit produit);
    Optional<LotProduit> findByNumeroLot(String numeroLot);
    List<LotProduit> findByDatePeremptionBefore(LocalDate date);
    List<LotProduit> findByDateFabricationBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT l FROM LotProduit l WHERE l.datePeremption BETWEEN :dateDebut AND :dateFin")
    List<LotProduit> findLotsExpirantEntre(LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT l FROM LotProduit l WHERE l.dateFabrication BETWEEN :dateDebut AND :dateFin")
    List<LotProduit> findLotsByPeriode(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT l FROM LotProduit l WHERE l.numeroLot = :numero")
    Optional<LotProduit> findLotByNumero(String numero);
}
