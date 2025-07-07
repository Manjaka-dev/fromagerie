package itu.fromagerie.fromagerie.service.auth;

import itu.fromagerie.fromagerie.dto.auth.*;
import itu.fromagerie.fromagerie.entities.utilisateur.TokenReset;
import itu.fromagerie.fromagerie.repository.utilisateur.TokenResetRepository;
import itu.fromagerie.fromagerie.entities.utilisateur.Utilisateur;
import itu.fromagerie.fromagerie.entities.utilisateur.JournalConnexion;
import itu.fromagerie.fromagerie.entities.utilisateur.Role;
import itu.fromagerie.fromagerie.repository.utilisateur.UtilisateurRepository;
import itu.fromagerie.fromagerie.repository.utilisateur.JournalConnexionRepository;
import itu.fromagerie.fromagerie.repository.utilisateur.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
public class AuthService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private JournalConnexionRepository journalConnexionRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private TokenResetRepository tokenResetRepository;
    
    // Stockage simple des tokens en mémoire (pour un système basique)
    // En production, utilisez Redis ou une base de données
    private static final Map<String, Long> tokenStore = new HashMap<>();
    private static final Map<String, String> refreshTokenStore = new HashMap<>();
    
    /**
     * Inscrit un nouvel utilisateur
     */
    public LoginResponseDTO register(RegisterRequestDTO registerRequest) {
        try {
            System.out.println("=== Début de l'inscription ===");
            System.out.println("Données reçues: " + registerRequest);
            
            // Validation des données
            if (registerRequest.getNom() == null || registerRequest.getNom().trim().isEmpty()) {
                System.out.println("❌ Erreur: Nom manquant");
                return new LoginResponseDTO(false, "Le nom est obligatoire");
            }
            
            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                System.out.println("❌ Erreur: Email manquant");
                return new LoginResponseDTO(false, "L'email est obligatoire");
            }
            
            if (registerRequest.getMotDePasse() == null || registerRequest.getMotDePasse().trim().isEmpty()) {
                System.out.println("❌ Erreur: Mot de passe manquant");
                return new LoginResponseDTO(false, "Le mot de passe est obligatoire");
            }
            
            if (registerRequest.getMotDePasse().length() < 6) {
                System.out.println("❌ Erreur: Mot de passe trop court (" + registerRequest.getMotDePasse().length() + " caractères)");
                return new LoginResponseDTO(false, "Le mot de passe doit contenir au moins 6 caractères");
            }
            
            if (!registerRequest.getMotDePasse().equals(registerRequest.getConfirmerMotDePasse())) {
                System.out.println("❌ Erreur: Mots de passe différents");
                return new LoginResponseDTO(false, "Les mots de passe ne correspondent pas");
            }
            
            // Vérifier si l'email existe déjà
            if (utilisateurRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                System.out.println("❌ Erreur: Email déjà existant: " + registerRequest.getEmail());
                return new LoginResponseDTO(false, "Un utilisateur avec cet email existe déjà");
            }
            
            // Récupérer le rôle
            Role role = null;
            if (registerRequest.getRoleId() != null) {
                System.out.println("Recherche du rôle avec ID: " + registerRequest.getRoleId());
                role = roleRepository.findById(registerRequest.getRoleId())
                        .orElse(null);
                if (role == null) {
                    System.out.println("❌ Erreur: Rôle non trouvé avec ID: " + registerRequest.getRoleId());
                    return new LoginResponseDTO(false, "Rôle invalide");
                }
                System.out.println("✅ Rôle trouvé: " + role.getNom());
            } else {
                System.out.println("Aucun rôle spécifié");
            }
            
            // Créer le nouvel utilisateur
            Utilisateur nouvelUtilisateur = new Utilisateur();
            nouvelUtilisateur.setNom(registerRequest.getNom().trim());
            nouvelUtilisateur.setEmail(registerRequest.getEmail().trim().toLowerCase());
            nouvelUtilisateur.setMotDePasse(registerRequest.getMotDePasse());
            nouvelUtilisateur.setRole(role);
            
            System.out.println("Création de l'utilisateur: " + nouvelUtilisateur.getNom() + " (" + nouvelUtilisateur.getEmail() + ")");
            
            // Sauvegarder l'utilisateur
            nouvelUtilisateur = utilisateurRepository.save(nouvelUtilisateur);
            System.out.println("✅ Utilisateur créé avec ID: " + nouvelUtilisateur.getId());
            
            // Générer un token et connecter automatiquement
            String token = generateToken();
            tokenStore.put(token, nouvelUtilisateur.getId());
            System.out.println("Token généré et stocké : " + token);
            
            // Enregistrer la connexion dans le journal
            enregistrerConnexion(nouvelUtilisateur);
            
            // Créer la réponse
            LoginResponseDTO.UtilisateurInfoDTO userInfo = new LoginResponseDTO.UtilisateurInfoDTO(
                nouvelUtilisateur.getId(),
                nouvelUtilisateur.getNom(),
                nouvelUtilisateur.getEmail(),
                nouvelUtilisateur.getRole() != null ? nouvelUtilisateur.getRole().getNom() : "Aucun rôle"
            );
            
            System.out.println("=== Inscription réussie ===");
            return new LoginResponseDTO(true, "Inscription réussie et connexion automatique", token, userInfo);
            
        } catch (Exception e) {
            System.err.println("❌ Exception lors de l'inscription: " + e.getMessage());
            e.printStackTrace();
            return new LoginResponseDTO(false, "Erreur lors de l'inscription: " + e.getMessage());
        }
    }
    
    /**
     * Authentifie un utilisateur
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // Vérifier si l'utilisateur existe
            Utilisateur utilisateur = utilisateurRepository.findByEmail(loginRequest.getEmail())
                    .orElse(null);
            
            if (utilisateur == null) {
                return new LoginResponseDTO(false, "Email ou mot de passe incorrect");
            }
            
            // Vérifier le mot de passe (en production, utilisez BCrypt)
            if (!utilisateur.getMotDePasse().equals(loginRequest.getMotDePasse())) {
                return new LoginResponseDTO(false, "Email ou mot de passe incorrect");
            }
            
            // Générer un token simple
            String token = generateToken();
            tokenStore.put(token, utilisateur.getId());
            
            // Enregistrer la connexion dans le journal
            enregistrerConnexion(utilisateur);
            
            // Créer la réponse
            LoginResponseDTO.UtilisateurInfoDTO userInfo = new LoginResponseDTO.UtilisateurInfoDTO(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getRole() != null ? utilisateur.getRole().getNom() : "Aucun rôle"
            );
            
            return new LoginResponseDTO(true, "Connexion réussie", token, userInfo);
            
        } catch (Exception e) {
            return new LoginResponseDTO(false, "Erreur lors de la connexion: " + e.getMessage());
        }
    }
    
    /**
     * Déconnecte un utilisateur
     */
    public boolean logout(String token) {
        if (token != null && tokenStore.containsKey(token)) {
            tokenStore.remove(token);
            return true;
        }
        return false;
    }
    
    /**
     * Vérifie si un token est valide
     */
    public boolean isTokenValid(String token) {
        System.out.println("Token reçu pour vérification : [" + token + "]");
        System.out.println("Tokens valides actuellement : " + tokenStore.keySet());
        return tokenStore.containsKey(token);
    }
    
    /**
     * Récupère l'ID de l'utilisateur à partir du token
     */
    public Long getUserIdFromToken(String token) {
        return tokenStore.get(token);
    }
    
    /**
     * Récupère l'utilisateur à partir du token
     */
    public Utilisateur getUtilisateurFromToken(String token) {
        Long userId = getUserIdFromToken(token);
        if (userId != null) {
            return utilisateurRepository.findById(userId).orElse(null);
        }
        return null;
    }
    
    /**
     * Vérifie si l'utilisateur a un rôle spécifique
     */
    public boolean hasRole(String token, String roleName) {
        Utilisateur utilisateur = getUtilisateurFromToken(token);
        if (utilisateur != null && utilisateur.getRole() != null) {
            return utilisateur.getRole().getNom().equals(roleName);
        }
        return false;
    }
    
    /**
     * Génère un token simple
     */
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Enregistre la connexion dans le journal
     */
    private void enregistrerConnexion(Utilisateur utilisateur) {
        try {
            JournalConnexion journalConnexion = new JournalConnexion();
            journalConnexion.setUtilisateur(utilisateur);
            journalConnexion.setDateConnexion(LocalDateTime.now());
            journalConnexion.setAction("Connexion réussie");
            
            journalConnexionRepository.save(journalConnexion);
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer la connexion
            System.err.println("Erreur lors de l'enregistrement de la connexion: " + e.getMessage());
        }
    }
    
    // ==================== NOUVELLES FONCTIONNALITÉS ====================
    
    /**
     * Demande de mot de passe oublié
     */
    public boolean forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(forgotPasswordDTO.getEmail())
                    .orElse(null);
            
            if (utilisateur == null) {
                // Pour des raisons de sécurité, ne pas révéler si l'email existe ou non
                return true;
            }
            
            // Générer un token de réinitialisation
            String resetToken = generateToken();
            LocalDateTime expiration = LocalDateTime.now().plusHours(24); // Expire dans 24h
            
            // Supprimer les anciens tokens pour cet utilisateur
            tokenResetRepository.findValidTokenByUtilisateur(utilisateur.getId(), LocalDateTime.now())
                    .ifPresent(oldToken -> tokenResetRepository.delete(oldToken));
            
            // Créer le nouveau token
            TokenReset tokenReset = new TokenReset();
            tokenReset.setUtilisateur(utilisateur);
            tokenReset.setToken(resetToken);
            tokenReset.setDateExpiration(expiration);
            tokenReset.setUtilise(false);
            
            tokenResetRepository.save(tokenReset);
            
            // TODO: Envoyer l'email avec le lien de réinitialisation
            // Pour l'instant, on simule l'envoi
            System.out.println("Email de réinitialisation envoyé à: " + utilisateur.getEmail());
            System.out.println("Token de réinitialisation: " + resetToken);
            
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de la demande de réinitialisation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Réinitialisation de mot de passe
     */
    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO) {
        try {
            // Valider les données
            if (resetPasswordDTO.getNouveauMotDePasse() == null || 
                resetPasswordDTO.getNouveauMotDePasse().length() < 6) {
                return false;
            }
            
            if (!resetPasswordDTO.getNouveauMotDePasse().equals(resetPasswordDTO.getConfirmerMotDePasse())) {
                return false;
            }
            
            // Vérifier le token
            TokenReset tokenReset = tokenResetRepository.findValidToken(
                resetPasswordDTO.getToken(), LocalDateTime.now()).orElse(null);
            
            if (tokenReset == null) {
                return false;
            }
            
            // Mettre à jour le mot de passe
            Utilisateur utilisateur = tokenReset.getUtilisateur();
            utilisateur.setMotDePasse(resetPasswordDTO.getNouveauMotDePasse());
            utilisateurRepository.save(utilisateur);
            
            // Marquer le token comme utilisé
            tokenReset.setUtilise(true);
            tokenResetRepository.save(tokenReset);
            
            // Supprimer tous les tokens de l'utilisateur de la session
            tokenStore.entrySet().removeIf(entry -> entry.getValue().equals(utilisateur.getId()));
            refreshTokenStore.entrySet().removeIf(entry -> entry.getValue().equals(utilisateur.getId().toString()));
            
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de la réinitialisation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Changement de mot de passe (utilisateur connecté)
     */
    public boolean changePassword(String token, ChangePasswordDTO changePasswordDTO) {
        try {
            // Récupérer l'utilisateur
            Utilisateur utilisateur = getUtilisateurFromToken(token);
            if (utilisateur == null) {
                return false;
            }
            
            // Valider les données
            if (changePasswordDTO.getNouveauMotDePasse() == null || 
                changePasswordDTO.getNouveauMotDePasse().length() < 6) {
                return false;
            }
            
            if (!changePasswordDTO.getNouveauMotDePasse().equals(changePasswordDTO.getConfirmerMotDePasse())) {
                return false;
            }
            
            // Vérifier l'ancien mot de passe
            if (!utilisateur.getMotDePasse().equals(changePasswordDTO.getMotDePasseActuel())) {
                return false;
            }
            
            // Mettre à jour le mot de passe
            utilisateur.setMotDePasse(changePasswordDTO.getNouveauMotDePasse());
            utilisateurRepository.save(utilisateur);
            
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors du changement de mot de passe: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Rafraîchissement de token
     */
    public LoginResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        try {
            // Vérifier le refresh token
            String userIdStr = refreshTokenStore.get(refreshTokenDTO.getRefreshToken());
            if (userIdStr == null) {
                return new LoginResponseDTO(false, "Refresh token invalide");
            }
            
            Long userId = Long.valueOf(userIdStr);
            Utilisateur utilisateur = utilisateurRepository.findById(userId).orElse(null);
            if (utilisateur == null) {
                return new LoginResponseDTO(false, "Utilisateur non trouvé");
            }
            
            // Générer un nouveau token
            String newToken = generateToken();
            tokenStore.put(newToken, utilisateur.getId());
            
            // Créer la réponse
            LoginResponseDTO.UtilisateurInfoDTO userInfo = new LoginResponseDTO.UtilisateurInfoDTO(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getRole() != null ? utilisateur.getRole().getNom() : "Aucun rôle"
            );
            
            return new LoginResponseDTO(true, "Token rafraîchi avec succès", newToken, userInfo);
        } catch (Exception e) {
            return new LoginResponseDTO(false, "Erreur lors du rafraîchissement: " + e.getMessage());
        }
    }
    
    /**
     * Obtenir les permissions de l'utilisateur
     */
    public PermissionsDTO getPermissions(String token) {
        try {
            Utilisateur utilisateur = getUtilisateurFromToken(token);
            if (utilisateur == null) {
                return null;
            }
            
            // Déterminer les permissions basées sur le rôle
            List<String> permissions = new ArrayList<>();
            boolean isAdmin = false;
            
            if (utilisateur.getRole() != null) {
                String roleName = utilisateur.getRole().getNom();
                isAdmin = "ADMIN".equalsIgnoreCase(roleName);
                
                // Permissions basées sur le rôle
                switch (roleName.toUpperCase()) {
                    case "ADMIN":
                        permissions.addAll(List.of(
                            "READ_ALL", "WRITE_ALL", "DELETE_ALL", "MANAGE_USERS", 
                            "MANAGE_ROLES", "VIEW_LOGS", "MANAGE_SYSTEM"
                        ));
                        break;
                    case "MANAGER":
                        permissions.addAll(List.of(
                            "READ_ALL", "WRITE_ALL", "MANAGE_STOCK", "VIEW_REPORTS"
                        ));
                        break;
                    case "USER":
                        permissions.addAll(List.of(
                            "READ_OWN", "WRITE_OWN", "VIEW_BASIC"
                        ));
                        break;
                    default:
                        permissions.add("READ_BASIC");
                }
            }
            
            return new PermissionsDTO(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getRole() != null ? utilisateur.getRole().getNom() : "Aucun rôle",
                permissions,
                isAdmin,
                true // isActive - à implémenter selon vos besoins
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des permissions: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Génère un refresh token
     */
    private String generateRefreshToken() {
        return UUID.randomUUID().toString() + "_refresh";
    }
} 