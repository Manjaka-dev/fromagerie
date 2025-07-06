package itu.fromagerie.fromagerie.dto.auth;

public class RegisterRequestDTO {
    private String nom;
    private String email;
    private String motDePasse;
    private String confirmerMotDePasse;
    private Long roleId;
    
    public RegisterRequestDTO() {}
    
    public RegisterRequestDTO(String nom, String email, String motDePasse, String confirmerMotDePasse, Long roleId) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.confirmerMotDePasse = confirmerMotDePasse;
        this.roleId = roleId;
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
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    public String getConfirmerMotDePasse() {
        return confirmerMotDePasse;
    }
    
    public void setConfirmerMotDePasse(String confirmerMotDePasse) {
        this.confirmerMotDePasse = confirmerMotDePasse;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
} 