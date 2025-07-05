package itu.fromagerie.fromagerie.repository.utilisateur;

import itu.fromagerie.fromagerie.entities.utilisateur.JournalConnexion;
import itu.fromagerie.fromagerie.entities.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JournalConnexionRepository extends JpaRepository<JournalConnexion, Long> {
    List<JournalConnexion> findByUtilisateur(Utilisateur utilisateur);
    List<JournalConnexion> findByDateConnexionBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    
    @Query("SELECT j FROM JournalConnexion j WHERE j.utilisateur = :utilisateur ORDER BY j.dateConnexion DESC")
    List<JournalConnexion> findByUtilisateurOrderByDateDesc(Utilisateur utilisateur);
}
