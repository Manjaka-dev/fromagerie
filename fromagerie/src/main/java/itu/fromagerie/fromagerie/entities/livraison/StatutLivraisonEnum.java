package itu.fromagerie.fromagerie.entities.livraison;

public enum StatutLivraisonEnum {
    PLANIFIEE("Planifiée"),
    EN_COURS("En cours"),
    LIVREE("Livrée"),
    ANNULEE("Annulée"),
    ECHEC("Échec de livraison");
    
    private final String libelle;
    
    StatutLivraisonEnum(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}
