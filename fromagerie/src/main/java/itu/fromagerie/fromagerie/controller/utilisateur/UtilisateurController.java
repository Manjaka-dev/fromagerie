package itu.fromagerie.fromagerie.controller.utilisateur;

import itu.fromagerie.fromagerie.entities.utilisateur.Utilisateur;
import itu.fromagerie.fromagerie.entities.utilisateur.Role;
import itu.fromagerie.fromagerie.service.utilisateur.UtilisateurService;
import itu.fromagerie.fromagerie.repository.utilisateur.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")

public class UtilisateurController {
    
    @Autowired
    private UtilisateurService utilisateurService;
    
    @Autowired
    private RoleRepository roleRepository;
    
    /**
     * Créer un nouvel utilisateur
     */
    @PostMapping
    public ResponseEntity<?> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur created = utilisateurService.createUtilisateur(utilisateur);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Récupérer tous les utilisateurs
     */
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }
    
    /**
     * Récupérer un utilisateur par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Mettre à jour un utilisateur
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateurDetails) {
        try {
            Utilisateur updated = utilisateurService.updateUtilisateur(id, utilisateurDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Supprimer un utilisateur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Utilisateur supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Récupérer les utilisateurs par rôle
     */
    @GetMapping("/role/{roleNom}")
    public ResponseEntity<?> getUtilisateursByRole(@PathVariable String roleNom) {
        try {
            List<Utilisateur> utilisateurs = utilisateurService.getUtilisateursByRole(roleNom);
            return ResponseEntity.ok(utilisateurs);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Rechercher des utilisateurs par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<Utilisateur>> searchUtilisateurs(@RequestParam String nom) {
        List<Utilisateur> utilisateurs = utilisateurService.searchUtilisateursByNom(nom);
        return ResponseEntity.ok(utilisateurs);
    }
    
    /**
     * Vérifier si un email existe
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = utilisateurService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Changer le mot de passe
     */
    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        
        if (oldPassword == null || newPassword == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ancien et nouveau mot de passe requis");
            return ResponseEntity.badRequest().body(error);
        }
        
        boolean success = utilisateurService.changePassword(id, oldPassword, newPassword);
        
        if (success) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Mot de passe changé avec succès");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ancien mot de passe incorrect");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Récupérer tous les rôles
     */
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }
} 