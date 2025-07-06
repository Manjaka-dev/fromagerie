package itu.fromagerie.fromagerie.projection;

import java.time.LocalDate;
import java.util.List;

public class CommandeLivraisonGroup {
    private Long commandeId;
    private String clientNom;
    private List<ProductInfo> produits;
    private LocalDate dateCommande;
    private LocalDate dateLivraison;

    public CommandeLivraisonGroup(Long commandeId, String clientNom, List<ProductInfo> produits, 
                                 LocalDate dateCommande, LocalDate dateLivraison) {
        this.commandeId = commandeId;
        this.clientNom = clientNom;
        this.produits = produits;
        this.dateCommande = dateCommande;
        this.dateLivraison = dateLivraison;
    }

    public Long getCommandeId() { return commandeId; }
    public String getClientNom() { return clientNom; }
    public List<ProductInfo> getProduits() { return produits; }
    public LocalDate getDateCommande() { return dateCommande; }
    public LocalDate getDateLivraison() { return dateLivraison; }

    public static class ProductInfo {
        private final String nom;
        private final Integer poids;
        private final Integer quantite;

        public ProductInfo(String nom, Integer poids, Integer quantite) {
            this.nom = nom;
            this.poids = poids;
            this.quantite = quantite;
        }
        public Integer getPoids(){ return poids; }

        public String getNom() {
            return nom;
        }

        public Integer getQuantite() {
            return quantite;
        }
    }
}
