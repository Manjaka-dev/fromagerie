// src/main/java/itu/fromagerie/fromagerie/dto/UtilisateurDTO.java
package itu.fromagerie.fromagerie.dto.utilisateur;

public class UtilisateurDTO {
    private Long id;
    private String nom;
    private String email;
    private Long roleId;
    private String roleNom;
    
    // Getters and Setters
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
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleNom() {
        return roleNom;
    }
    
    public void setRoleNom(String roleNom) {
        this.roleNom = roleNom;
    }
}