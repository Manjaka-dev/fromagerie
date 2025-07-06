package itu.fromagerie.fromagerie.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionsDTO {
    private Long utilisateurId;
    private String nomUtilisateur;
    private String email;
    private String role;
    private List<String> permissions;
    private boolean isAdmin;
    private boolean isActive;
} 