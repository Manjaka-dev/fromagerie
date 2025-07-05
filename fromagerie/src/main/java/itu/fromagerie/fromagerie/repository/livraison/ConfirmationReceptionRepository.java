package itu.fromagerie.fromagerie.repository.livraison;

import itu.fromagerie.fromagerie.entities.livraison.ConfirmationReception;
import itu.fromagerie.fromagerie.entities.livraison.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationReceptionRepository extends JpaRepository<ConfirmationReception, Long> {
    Optional<ConfirmationReception> findByLivraison(Livraison livraison);
}
