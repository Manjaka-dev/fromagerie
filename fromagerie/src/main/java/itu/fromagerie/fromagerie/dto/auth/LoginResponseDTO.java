package itu.fromagerie.fromagerie.dto.auth;

public class LoginResponseDTO {
    private boolean success;
    private String message;
    private String token;
    private UtilisateurInfoDTO utilisateur;
    
    public LoginResponseDTO() {}
    
    public LoginResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public LoginResponseDTO(boolean success, String message, String token, UtilisateurInfoDTO utilisateur) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.utilisateur = utilisateur;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UtilisateurInfoDTO getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(UtilisateurInfoDTO utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public static class UtilisateurInfoDTO {
        private Long id;
        private String nom;
        private String email;
        private String role;
        
        public UtilisateurInfoDTO() {}
        
        public UtilisateurInfoDTO(Long id, String nom, String email, String role) {
            this.id = id;
            this.nom = nom;
            this.email = email;
            this.role = role;
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getNom() {
            return nom;
        }
        
        public void setNom(String nom) {
            this.nom = nom;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
    }
} 