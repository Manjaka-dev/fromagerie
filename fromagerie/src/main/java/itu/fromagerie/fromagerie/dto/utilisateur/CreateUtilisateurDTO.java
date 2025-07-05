// src/main/java/itu/fromagerie/fromagerie/dto/CreateUtilisateurDTO.java
package itu.fromagerie.fromagerie.dto.utilisateur;

public class CreateUtilisateurDTO {
    private String nom;
    private String email;
    private String motDePasse;
    private Long roleId;
    
    // Getters and Setters
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
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}