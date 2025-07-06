package itu.fromagerie.fromagerie.service.utilisateur;

import itu.fromagerie.fromagerie.entities.utilisateur.Utilisateur;
import itu.fromagerie.fromagerie.entities.utilisateur.Role;
import itu.fromagerie.fromagerie.repository.utilisateur.UtilisateurRepository;
import itu.fromagerie.fromagerie.repository.utilisateur.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    /**
     * Créer un nouvel utilisateur
     */
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        // Vérifier si l'email existe déjà
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        
        // En production, hasher le mot de passe avec BCrypt
        // utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        
        return utilisateurRepository.save(utilisateur);
    }
    
    /**
     * Récupérer tous les utilisateurs
     */
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
    
    /**
     * Récupérer un utilisateur par ID
     */
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }
    
    /**
     * Récupérer un utilisateur par email
     */
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    
    /**
     * Mettre à jour un utilisateur
     */
    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateurDetails) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        utilisateur.setNom(utilisateurDetails.getNom());
        utilisateur.setEmail(utilisateurDetails.getEmail());
        
        // Ne mettre à jour le mot de passe que s'il est fourni
        if (utilisateurDetails.getMotDePasse() != null && !utilisateurDetails.getMotDePasse().isEmpty()) {
            // En production, hasher le mot de passe
            // utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDetails.getMotDePasse()));
            utilisateur.setMotDePasse(utilisateurDetails.getMotDePasse());
        }
        
        if (utilisateurDetails.getRole() != null) {
            utilisateur.setRole(utilisateurDetails.getRole());
        }
        
        return utilisateurRepository.save(utilisateur);
    }
    
    /**
     * Supprimer un utilisateur
     */
    public void deleteUtilisateur(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        utilisateurRepository.deleteById(id);
    }
    
    /**
     * Récupérer les utilisateurs par rôle
     */
    public List<Utilisateur> getUtilisateursByRole(String roleNom) {
        Role role = roleRepository.findByNom(roleNom)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        return utilisateurRepository.findByRole(role);
    }
    
    /**
     * Rechercher des utilisateurs par nom
     */
    public List<Utilisateur> searchUtilisateursByNom(String nom) {
        return utilisateurRepository.findByNomContainingIgnoreCase(nom);
    }
    
    /**
     * Vérifier si un utilisateur existe par email
     */
    public boolean existsByEmail(String email) {
        return utilisateurRepository.findByEmail(email).isPresent();
    }
    
    /**
     * Changer le mot de passe d'un utilisateur
     */
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier l'ancien mot de passe
        if (!utilisateur.getMotDePasse().equals(oldPassword)) {
            return false;
        }
        
        // En production, hasher le nouveau mot de passe
        // utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
        utilisateur.setMotDePasse(newPassword);
        
        utilisateurRepository.save(utilisateur);
        return true;
    }
} 