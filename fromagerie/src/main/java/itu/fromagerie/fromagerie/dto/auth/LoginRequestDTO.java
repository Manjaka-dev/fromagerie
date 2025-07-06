package itu.fromagerie.fromagerie.dto.auth;

public class LoginRequestDTO {
    private String email;
    private String motDePasse;
    
    public LoginRequestDTO() {}
    
    public LoginRequestDTO(String email, String motDePasse) {
        this.email = email;
        this.motDePasse = motDePasse;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
} 