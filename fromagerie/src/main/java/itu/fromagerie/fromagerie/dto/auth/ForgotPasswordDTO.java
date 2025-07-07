package itu.fromagerie.fromagerie.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDTO {
    private String email;
    
    // Getters et setters manuels si Lombok ne fonctionne pas
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
} 