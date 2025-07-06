package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivreurRepository extends JpaRepository<Livreur, Long> {
}