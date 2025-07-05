package itu.fromagerie.fromagerie.repository.stock;

import itu.fromagerie.fromagerie.entities.stock.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatierePremiereRepository extends JpaRepository<MatierePremiere, Long> {
    Optional<MatierePremiere> findByNom(String nom);
    List<MatierePremiere> findByUnite(String unite);
    List<MatierePremiere> findByNomContainingIgnoreCase(String nom);
}
