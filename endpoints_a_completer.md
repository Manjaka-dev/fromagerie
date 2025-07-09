# Liste des Endpoints et Méthodes à Implémenter/Compléter

Ce document recense tous les endpoints REST et méthodes backend qui nécessitent une implémentation ou une complétion dans le projet de la fromagerie.

## 1. LivraisonController (`/api/livraisons`)

### Endpoints à compléter

| Endpoint | Méthode | Description | État |
| --- | --- | --- | --- |
| `/api/livraisons/zones` | GET | Liste des zones de livraison disponibles | À implémenter dans LivraisonService |
| `/api/livraisons/{id}` | GET | Récupérer une livraison par ID | À implémenter dans LivraisonService |
| `/api/livraisons/livreur/{livreurId}` | GET | Récupère les livraisons d'un livreur | À implémenter dans LivraisonService |
| `/api/livraisons/zone/{zone}` | GET | Récupère les livraisons d'une zone | À implémenter dans LivraisonService |
| `/api/livraisons/commandes/{id}/export-pdf` | GET | Exporter une commande au format PDF | À implémenter |

### Méthodes à ajouter à LivraisonService

```java
public List<String> getZonesLivraisonDisponibles() {
    // Retourner une liste des zones de livraison disponibles
}

public LivraisonDTO getLivraisonById(Long id) {
    // Récupérer une livraison par son ID et la convertir en DTO
}

public List<LivraisonInfoDTO> getLivraisonsByLivreur(Long livreurId) {
    // Récupérer les livraisons d'un livreur spécifique
}

public List<LivraisonInfoDTO> getLivraisonsByZone(String zone) {
    // Récupérer les livraisons d'une zone spécifique
}
```

## 2. LivreurController (`/api/livreurs`)

### Méthodes à ajouter à LivraisonService

```java
public Livreur saveLivreur(Livreur livreur) {
    // Enregistrer un nouveau livreur
    return livreurRepository.save(livreur);
}

public void deleteLivreur(Long id) {
    // Supprimer un livreur
    livreurRepository.deleteById(id);
}

public Livreur updateLivreur(Long id, Livreur livreur) {
    // Mettre à jour un livreur
    livreur.setId(id);
    return livreurRepository.save(livreur);
}
```

## 3. ProduitController (`/api/produits`)

### Endpoints à compléter

| Endpoint | Méthode | Description | État |
| --- | --- | --- | --- |
| `/api/produits/{id}/ajuster-stock` | POST | Ajuster le stock d'un produit | À implémenter dans ProduitService |

### Méthodes à ajouter à ProduitService

```java
public Map<String, Object> ajusterStock(Long produitId, Map<String, Object> ajustement) {
    // Récupérer les données d'ajustement
    Integer nouvelleQuantite = (Integer) ajustement.get("quantite");
    String raison = (String) ajustement.get("raison");
    
    // Vérifier que le produit existe
    Produit produit = getProduitById(produitId);
    if (produit == null) {
        throw new RuntimeException("Produit non trouvé");
    }
    
    // Mettre à jour le stock
    updateLotProduit(produitId, nouvelleQuantite);
    
    // Créer une réponse
    Map<String, Object> response = new HashMap<>();
    response.put("produitId", produitId);
    response.put("nouvelleQuantite", nouvelleQuantite);
    response.put("message", "Stock ajusté avec succès");
    
    return response;
}
```

## 4. ClientController (`/api/clients`)

### Méthodes à ajouter à ClientService

```java
public Client saveClient(Client client) {
    // Enregistrer un nouveau client
    return clientRepository.save(client);
}

public Optional<Client> findClientById(Long id) {
    // Récupérer un client par ID
    return clientRepository.findById(id);
}
```

## 5. BilanFinancierController (`/api/bilans`)

Le contrôleur semble complet, mais vérifiez les méthodes suivantes dans BilanFinancierService :

```java
public BilanFinancier findByPeriode(LocalDate periode)
public List<BilanFinancier> findByPeriodeBetween(LocalDate dateDebut, LocalDate dateFin)
public List<BilanFinancier> findByProfitGreaterThan(BigDecimal profit)
public List<BilanFinancier> findAllByOrderByPeriodeDesc()
public BigDecimal calculateTotalProfit()
public BilanFinancier findById(Long id)
public BilanFinancier saveBilanFinancier(BilanFinancier bilanFinancier)
```

## 6. PaiementController (`/api/paiements`)

### Endpoints à compléter

| Endpoint | Méthode | Description | État |
| --- | --- | --- | --- |
| `/api/paiements/commande/{commandeId}` | GET | Récupérer les paiements d'une commande | À implémenter |

### Méthodes à ajouter à LivraisonService ou créer PaiementService

```java
public List<Paiement> getPaiementsByCommande(Long commandeId) {
    // Récupérer tous les paiements associés à une commande
    return paiementRepository.findByCommandeId(commandeId);
}
```

## 7. FicheProductionController (`/api/fiches`)

Le contrôleur semble complet, mais vérifiez les méthodes suivantes dans FicheProductionService :

```java
public List<FicheProduction> getFichesByProduit(Long produitId)
public List<FicheProduction> getFichesByMatiere(Long matiereId)
public FicheProduction createFicheProduction(FicheProduction ficheProduction)
public FicheProduction getFicheById(Long id)
```

## 8. Implémentations prioritaires

Voici les implémentations à prioriser en fonction de leur importance pour le fonctionnement de l'application :

1. **LivraisonService.getZonesLivraisonDisponibles()** - Nécessaire pour afficher les options de zones dans les formulaires de livraison
2. **ProduitService.ajusterStock()** - Fonctionnalité essentielle pour la gestion du stock
3. **LivraisonService.getLivraisonById()** - Nécessaire pour afficher les détails d'une livraison
4. **PaiementService.getPaiementsByCommande()** - Nécessaire pour la comptabilité et les factures
5. **LivraisonService.getLivraisonsByLivreur()** et **getLivraisonsByZone()** - Filtres importants pour les gestionnaires de livraison

## 9. Méthodes TODO ou partiellement implémentées

### StockMatiereService

```java
public EstimationProductionDTO estimerProduction(Long produitId, Integer quantiteSouhaitee) {
    // TODO: Implémenter la logique de calcul basée sur les recettes
}
```

### DashboardService

```java
public DashboardDTO getDashboardFiltered(LocalDate dateDebut, LocalDate dateFin, Long clientId, Long produitId) {
    // TODO: Implémenter les méthodes manquantes dans ProductionEffectueeRepository
    // Actuellement, utilise des valeurs par défaut pour prod.prodHebdo, prod.tauxQualite
}
```

### Autres méthodes à vérifier/compléter
- Vérifier l'implémentation de tous les endpoints du StatistiqueProductionController
- Compléter la documentation Swagger dans les nouveaux endpoints
- Ajouter des validations @Valid sur les DTOs
