package itu.fromage.repositories;

import itu.fromage.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Integer> {
//    Produit findById(Integer integer);
}