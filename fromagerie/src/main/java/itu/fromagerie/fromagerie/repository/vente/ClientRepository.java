package itu.fromagerie.fromagerie.repository.vente;

import itu.fromagerie.fromagerie.entities.vente.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByNomContainingIgnoreCase(String nom);
    List<Client> findByTelephone(String telephone);
}
