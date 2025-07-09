package itu.fromagerie.fromagerie.controller.auth;

import itu.fromagerie.fromagerie.dto.auth.*;
import itu.fromagerie.fromagerie.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Endpoint d'inscription
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        LoginResponseDTO response = authService.register(registerRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Endpoint de connexion
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Endpoint de déconnexion
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token != null && token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);
            boolean success = authService.logout(actualToken);
            
            if (success) {
                response.put("success", true);
                response.put("message", "Déconnexion réussie");
                return ResponseEntity.ok(response);
            }
        }
        
        response.put("success", false);
        response.put("message", "Token invalide");
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Endpoint pour vérifier si un token est valide
     */
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token != null && token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);
            boolean isValid = authService.isTokenValid(actualToken);
            
            if (isValid) {
                response.put("valid", true);
                response.put("message", "Token valide");
                return ResponseEntity.ok(response);
            }
        }
        
        response.put("valid", false);
        response.put("message", "Token invalide");
        return ResponseEntity.status(401).body(response);
    }
    
    /**
     * Endpoint pour obtenir les informations de l'utilisateur connecté
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token != null && token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);
            
            if (authService.isTokenValid(actualToken)) {
                var utilisateur = authService.getUtilisateurFromToken(actualToken);
                if (utilisateur != null) {
                    response.put("success", true);
                    response.put("utilisateur", Map.of(
                        "id", utilisateur.getId(),
                        "nom", utilisateur.getNom(),
                        "email", utilisateur.getEmail(),
                        "role", utilisateur.getRole() != null ? utilisateur.getRole().getNom() : "Aucun rôle"
                    ));
                    return ResponseEntity.ok(response);
                }
            }
        }
        
        response.put("success", false);
        response.put("message", "Token invalide");
        return ResponseEntity.status(401).body(response);
    }
    
    // ==================== NOUVEAUX ENDPOINTS ====================
    
    /**
     * Endpoint pour demander la réinitialisation de mot de passe
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        Map<String, Object> response = new HashMap<>();
        
        if (forgotPasswordDTO.getEmail() == null || forgotPasswordDTO.getEmail().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Email requis");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean success = authService.forgotPassword(forgotPasswordDTO);
        
        if (success) {
            response.put("success", true);
            response.put("message", "Si l'email existe dans notre base, un lien de réinitialisation a été envoyé");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Erreur lors de l'envoi de l'email de réinitialisation");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Endpoint pour réinitialiser le mot de passe
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        Map<String, Object> response = new HashMap<>();
        
        if (resetPasswordDTO.getToken() == null || resetPasswordDTO.getToken().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Token requis");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (resetPasswordDTO.getNouveauMotDePasse() == null || resetPasswordDTO.getNouveauMotDePasse().length() < 6) {
            response.put("success", false);
            response.put("message", "Le nouveau mot de passe doit contenir au moins 6 caractères");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (!resetPasswordDTO.getNouveauMotDePasse().equals(resetPasswordDTO.getConfirmerMotDePasse())) {
            response.put("success", false);
            response.put("message", "Les mots de passe ne correspondent pas");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean success = authService.resetPassword(resetPasswordDTO);
        
        if (success) {
            response.put("success", true);
            response.put("message", "Mot de passe réinitialisé avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Token invalide ou expiré");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Endpoint pour changer le mot de passe (utilisateur connecté)
     */
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangePasswordDTO changePasswordDTO) {
        Map<String, Object> response = new HashMap<>();
        
        if (token == null || !token.startsWith("Bearer ")) {
            response.put("success", false);
            response.put("message", "Token d'authentification requis");
            return ResponseEntity.status(401).body(response);
        }
        
        String actualToken = token.substring(7);
        
        if (!authService.isTokenValid(actualToken)) {
            response.put("success", false);
            response.put("message", "Token invalide");
            return ResponseEntity.status(401).body(response);
        }
        
        if (changePasswordDTO.getMotDePasseActuel() == null || changePasswordDTO.getMotDePasseActuel().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Mot de passe actuel requis");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (changePasswordDTO.getNouveauMotDePasse() == null || changePasswordDTO.getNouveauMotDePasse().length() < 6) {
            response.put("success", false);
            response.put("message", "Le nouveau mot de passe doit contenir au moins 6 caractères");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (!changePasswordDTO.getNouveauMotDePasse().equals(changePasswordDTO.getConfirmerMotDePasse())) {
            response.put("success", false);
            response.put("message", "Les mots de passe ne correspondent pas");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean success = authService.changePassword(actualToken, changePasswordDTO);
        
        if (success) {
            response.put("success", true);
            response.put("message", "Mot de passe changé avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Mot de passe actuel incorrect");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Endpoint pour rafraîchir le token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        if (refreshTokenDTO.getRefreshToken() == null || refreshTokenDTO.getRefreshToken().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new LoginResponseDTO(false, "Refresh token requis"));
        }
        
        LoginResponseDTO response = authService.refreshToken(refreshTokenDTO);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Endpoint pour obtenir les permissions de l'utilisateur
     */
    @GetMapping("/permissions")
    public ResponseEntity<Map<String, Object>> getPermissions(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token == null || !token.startsWith("Bearer ")) {
            response.put("success", false);
            response.put("message", "Token d'authentification requis");
            return ResponseEntity.status(401).body(response);
        }
        
        String actualToken = token.substring(7);
        
        if (!authService.isTokenValid(actualToken)) {
            response.put("success", false);
            response.put("message", "Token invalide");
            return ResponseEntity.status(401).body(response);
        }
        
        PermissionsDTO permissions = authService.getPermissions(actualToken);
        
        if (permissions != null) {
            response.put("success", true);
            response.put("permissions", permissions);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Erreur lors de la récupération des permissions");
            return ResponseEntity.badRequest().body(response);
        }
    }
} 