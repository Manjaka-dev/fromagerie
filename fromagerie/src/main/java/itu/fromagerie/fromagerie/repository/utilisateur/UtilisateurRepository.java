package itu.fromagerie.fromagerie.repository.utilisateur;

import itu.fromagerie.fromagerie.entities.utilisateur.Utilisateur;
import itu.fromagerie.fromagerie.entities.utilisateur.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findByRole(Role role);
    List<Utilisateur> findByNomContainingIgnoreCase(String nom);
}
