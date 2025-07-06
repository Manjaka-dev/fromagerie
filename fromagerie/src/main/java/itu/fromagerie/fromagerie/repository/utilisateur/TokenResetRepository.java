package itu.fromagerie.fromagerie.repository.utilisateur;

import itu.fromagerie.fromagerie.entities.utilisateur.TokenReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenResetRepository extends JpaRepository<TokenReset, Long> {
    
    Optional<TokenReset> findByToken(String token);
    
    @Query("SELECT t FROM TokenReset t WHERE t.token = :token AND t.dateExpiration > :now AND t.utilise = false")
    Optional<TokenReset> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM TokenReset t WHERE t.utilisateur.id = :utilisateurId AND t.dateExpiration > :now AND t.utilise = false")
    Optional<TokenReset> findValidTokenByUtilisateur(@Param("utilisateurId") Long utilisateurId, @Param("now") LocalDateTime now);
    
    @Query("DELETE FROM TokenReset t WHERE t.dateExpiration < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
} 