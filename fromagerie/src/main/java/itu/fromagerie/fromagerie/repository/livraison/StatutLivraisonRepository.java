package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.StatutLivraison;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatutLivraisonRepository extends JpaRepository<StatutLivraison, Long> {
    Optional<StatutLivraison> findTopByLivraisonIdOrderByDateStatutDesc(Integer livraisonId);
}