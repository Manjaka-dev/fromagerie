package itu.fromage.repositories;

import itu.fromage.entities.StatutLivraison;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatutLivraisonRepository extends JpaRepository<StatutLivraison, Integer> {
    Optional<StatutLivraison> findTopByLivraisonIdOrderByDateStatutDesc(Integer livraisonId);
}