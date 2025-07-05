package itu.fromage.repositories;

import itu.fromage.entities.ConfirmationReception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationReceptionRepository extends JpaRepository<ConfirmationReception, Integer> {
} 