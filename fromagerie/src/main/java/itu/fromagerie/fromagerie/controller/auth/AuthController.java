package itu.fromagerie.fromagerie.controller.auth;

import itu.fromagerie.fromagerie.dto.auth.LoginRequestDTO;
import itu.fromagerie.fromagerie.dto.auth.LoginResponseDTO;
import itu.fromagerie.fromagerie.dto.auth.RegisterRequestDTO;
import itu.fromagerie.fromagerie.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")

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
} 