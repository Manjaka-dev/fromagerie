package itu.fromagerie.fromagerie.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    private String motDePasseActuel;
    private String nouveauMotDePasse;
    private String confirmerMotDePasse;
} 