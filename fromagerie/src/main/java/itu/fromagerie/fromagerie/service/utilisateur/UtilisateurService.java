// src/main/java/itu/fromagerie/fromagerie/service/UtilisateurService.java
package itu.fromagerie.fromagerie.service.utilisateur;

import itu.fromagerie.fromagerie.dto.utilisateur.CreateUtilisateurDTO;
import itu.fromagerie.fromagerie.dto.utilisateur.UtilisateurDTO;
import itu.fromagerie.fromagerie.entities.utilisateur.Utilisateur;
import itu.fromagerie.fromagerie.repository.utilisateur.RoleRepository;
import itu.fromagerie.fromagerie.repository.utilisateur.UtilisateurRepository;
import itu.fromagerie.fromagerie.entities.utilisateur.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public UtilisateurDTO getUtilisateurById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return convertToDTO(utilisateur);
    }
    
    public UtilisateurDTO createUtilisateur(CreateUtilisateurDTO dto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(dto.getMotDePasse()); // Note: Devrait être crypté en production
        
        Role role = roleRepository.findById(dto.getRoleId())
            .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        utilisateur.setRole(role);
        
        Utilisateur saved = utilisateurRepository.save(utilisateur);
        return convertToDTO(saved);
    }
    
    public UtilisateurDTO updateUtilisateur(Long id, CreateUtilisateurDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(dto.getMotDePasse()); // Note: Devrait être crypté
        }
        
        Role role = roleRepository.findById(dto.getRoleId())
            .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        utilisateur.setRole(role);
        
        Utilisateur updated = utilisateurRepository.save(utilisateur);
        return convertToDTO(updated);
    }
    
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
    
    private UtilisateurDTO convertToDTO(Utilisateur utilisateur) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(utilisateur.getId());
        dto.setNom(utilisateur.getNom());
        dto.setEmail(utilisateur.getEmail());
        if (utilisateur.getRole() != null) {
            dto.setRoleId(utilisateur.getRole().getId());
            dto.setRoleNom(utilisateur.getRole().getNom());
        }
        return dto;
    }
}