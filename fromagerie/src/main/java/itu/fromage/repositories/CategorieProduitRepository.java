package itu.fromage.repositories;

import itu.fromage.entities.CategorieProduit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieProduitRepository extends JpaRepository<CategorieProduit, Integer> {

}