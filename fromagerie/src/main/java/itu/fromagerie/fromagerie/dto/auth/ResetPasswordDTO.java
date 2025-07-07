package itu.fromagerie.fromagerie.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {
    private String token;
    private String nouveauMotDePasse;
    private String confirmerMotDePasse;
    
    // Getters et setters manuels si Lombok ne fonctionne pas
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getNouveauMotDePasse() { return nouveauMotDePasse; }
    public void setNouveauMotDePasse(String nouveauMotDePasse) { this.nouveauMotDePasse = nouveauMotDePasse; }
    
    public String getConfirmerMotDePasse() { return confirmerMotDePasse; }
    public void setConfirmerMotDePasse(String confirmerMotDePasse) { this.confirmerMotDePasse = confirmerMotDePasse; }
} 