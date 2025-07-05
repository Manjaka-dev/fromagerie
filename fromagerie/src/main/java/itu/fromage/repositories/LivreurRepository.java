package itu.fromage.repositories;

import itu.fromage.entities.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivreurRepository extends JpaRepository<Livreur, Integer> {
}