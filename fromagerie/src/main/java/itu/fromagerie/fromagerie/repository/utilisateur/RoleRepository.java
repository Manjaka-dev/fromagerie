package itu.fromagerie.fromagerie.repository.utilisateur;

import itu.fromagerie.fromagerie.entities.utilisateur.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNom(String nom);
}
