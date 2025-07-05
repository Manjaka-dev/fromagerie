package itu.fromagerie.fromagerie.controller.utilisateur;

import itu.fromagerie.fromagerie.dto.utilisateur.CreateUtilisateurDTO;
import itu.fromagerie.fromagerie.dto.utilisateur.UtilisateurDTO;
import itu.fromagerie.fromagerie.service.utilisateur.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;
    
    @GetMapping
    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateurById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getUtilisateurById(id));
    }
    
    @PostMapping
    public ResponseEntity<UtilisateurDTO> createUtilisateur(@RequestBody CreateUtilisateurDTO dto) {
        return ResponseEntity.ok(utilisateurService.createUtilisateur(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(
            @PathVariable Long id,
            @RequestBody CreateUtilisateurDTO dto) {
        return ResponseEntity.ok(utilisateurService.updateUtilisateur(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}