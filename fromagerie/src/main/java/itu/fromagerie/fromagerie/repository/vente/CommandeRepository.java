package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.Commande;
import itu.fromagerie.fromagerie.entities.vente.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClient(Client client);
    List<Commande> findByStatut(String statut);
    List<Commande> findByDateCommandeBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT c FROM Commande c WHERE c.dateCommande = :date")
    List<Commande> findByDateCommande(LocalDate date);
    
    @Query("SELECT COUNT(c) FROM Commande c WHERE c.statut = :statut")
    Long countByStatut(String statut);
}
