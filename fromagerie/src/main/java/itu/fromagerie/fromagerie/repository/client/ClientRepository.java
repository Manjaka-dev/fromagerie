package itu.fromagerie.fromagerie.repository.client;

import itu.fromagerie.fromagerie.entities.vente.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}