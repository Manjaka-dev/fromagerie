# Liste des endpoints vitaux non implémentés

Ce document liste les endpoints REST critiques qui n'ont pas encore été créés ou complètement implémentés dans l'API de gestion de fromagerie.

## Résumé

| Catégorie | Nombre d'endpoints manquants | Priorité |
|-----------|------------------------------|----------|
| Production et Statistiques | 1 | Élevée |
| Stock et Alertes | 1 | Élevée |
| Livraisons | 1 | Moyenne |
| Commandes | Nettoyage | Basse |
| Production et Transformation | 4 | Moyenne |
| Comptabilité et Finance | 2 | Moyenne |
| Autres | 2 | Basse |
| **Total** | **11** | - |

## Légende

- 🚫 Non implémenté
- ⚠️ Partiellement implémenté / TODO
- 🔄 En cours d'implémentation

## Endpoints manquants par catégorie

### Production et Statistiques

#### StatistiqueProductionController

- ⚠️ `GET /api/statistiques/production/produit/{produitId}` - Récupère les statistiques de production pour un produit spécifique (méthode marquée TODO)

### Stock et Alertes

#### StockMatiereController

- ✅ `GET /api/stock/alertes-peremption` - Obtenir les alertes de péremption actives (implémenté et corrigé avec une requête native PostgreSQL pour calculer correctement la différence entre dates)

### Livraisons

#### LivreurController

- 🚫 `POST /api/livreurs` - Créer un nouveau livreur (commentaire indique que la méthode save est manquante dans LivraisonService)

### Gestion des commandes

#### CommandeController

- ⚠️ Certains endpoints possèdent des sections de debug qui devraient être nettoyées ou désactivées en production

### Production et Transformation

#### TransformationDechetController

- ⚠️ `GET /api/transformations/date-range` - Méthode incomplète, ne retourne pas la ResponseEntity

#### ProductionEffectueeController

- ⚠️ `GET /api/productions/date` - Méthode incomplète, ne retourne pas la ResponseEntity

#### PerteProductionController

- ⚠️ `GET /api/pertes/taux` - Méthode incomplète, ne retourne pas la ResponseEntity

#### FicheProductionController

- ⚠️ `GET /api/fiches/matiere/{matiereId}` - Méthode incomplète, ne retourne pas la ResponseEntity

### Comptabilité et Finance

#### BilanFinancierController

- ⚠️ `GET /api/bilans/date-range` - Méthode incomplète, ne retourne pas la ResponseEntity

#### RevenuController

- ⚠️ `GET /api/revenus/date-range` - Méthode incomplète, ne retourne pas la ResponseEntity

### Autres endpoints critiques

#### StockMatiereService

- ⚠️ Méthode `estimerProduction` - La logique de calcul basée sur les recettes n'est pas implémentée (simulée)

#### UtilisateurController

- ⚠️ `GET /api/utilisateurs` - Méthode incomplète, ne retourne pas la ResponseEntity

## Recommandations pour l'implémentation

### Priorités d'implémentation

1. **Haute priorité**
   - Implémenter la méthode `findActiveAlertes` dans `AlertePeremptionRepository` pour activer les alertes de péremption
   - Compléter les méthodes des contrôleurs qui ne retournent pas de ResponseEntity, en commençant par ceux liés aux statistiques de production

2. **Moyenne priorité**
   - Ajouter la méthode `save` manquante dans `LivraisonService` pour les livreurs
   - Compléter l'implémentation de la logique de calcul dans `estimerProduction` en utilisant les données réelles des recettes
   - Finaliser les endpoints de comptabilité et finance pour permettre la génération précise des bilans

3. **Basse priorité**
   - Nettoyer les sections de debug avant la mise en production
   - Standardiser la structure des réponses d'API pour tous les endpoints

### Méthode recommandée

1. **Pour chaque endpoint manquant :**
   - Vérifier les dépendances (repositories, services, etc.)
   - Implémenter d'abord les méthodes des repositories manquantes
   - Compléter les services associés
   - Finaliser l'implémentation du contrôleur en ajoutant la gestion des erreurs appropriée

2. **Tests :**
   - Ajouter chaque nouvel endpoint au script `test_api.sh`
   - Vérifier que tous les tests passent après chaque implémentation

## Impact sur le frontend

Les endpoints manquants affectent principalement les fonctionnalités suivantes dans le frontend :

- **Statistiques et tableaux de bord** : Certaines données de statistiques de production par produit ne sont pas disponibles
- **Gestion des stocks** : Les alertes de péremption ne fonctionnent pas correctement
- **Module de livraison** : Impossible de créer de nouveaux livreurs via l'API
- **Rapports financiers** : Certaines données de bilans financiers par période sont indisponibles

## Documentation

Il est fortement recommandé de :

1. Mettre à jour la documentation Swagger pour chaque endpoint nouvellement implémenté
2. Ajouter des annotations @ApiOperation et @ApiResponses pour tous les endpoints
3. Documenter clairement les formats de requête/réponse attendus
4. Maintenir à jour le fichier README.md du projet avec les nouvelles fonctionnalités

## Plan d'action

| Semaine | Tâches | Endpoints concernés |
|---------|--------|-------------------|
| Semaine 1 | Implémenter les endpoints critiques de stock et alertes | `/api/stock/alertes-actives` |
| Semaine 1 | Corriger les endpoints de statistiques de production | `/api/statistiques/production/produit/{produitId}` |
| Semaine 2 | Compléter les endpoints de production et transformation | Endpoints dans TransformationDechetController, ProductionEffectueeController, etc. |
| Semaine 2 | Implémenter les endpoints de livraison manquants | `POST /api/livreurs` |
| Semaine 3 | Finaliser les endpoints de comptabilité | Endpoints dans BilanFinancierController et RevenuController |
| Semaine 3 | Tests complets et optimisation | Tous les endpoints |
| Semaine 4 | Documentation et nettoyage du code | Tous les endpoints |

## Exemple d'implémentation

Voici un exemple d'implémentation pour l'endpoint manquant `GET /api/stock/alertes-actives` :

### 1. Implémenter dans AlertePeremptionRepository

```java
@Query("SELECT a FROM AlertePeremption a WHERE a.datePeremption <= :dateReference AND a.statut = 'ACTIVE'")
List<AlertePeremption> findActiveAlertes(@Param("dateReference") LocalDate dateReference);
```

### 2. Compléter dans StockMatiereService

```java
public List<AlertePeremptionDTO> getAlertesActives() {
    LocalDate today = LocalDate.now();
    LocalDate dateLimite = today.plusDays(15); // Alertes pour les 15 prochains jours
    
    List<AlertePeremption> alertesActives = alertePeremptionRepository.findActiveAlertes(dateLimite);
    
    return alertesActives.stream()
        .map(alertePeremptionMapper::toDto)
        .collect(Collectors.toList());
}
```

### 3. Finaliser dans StockMatiereController

```java
@GetMapping("/alertes-actives")
@ApiOperation(value = "Obtenir la liste des alertes de péremption actives", 
              notes = "Retourne toutes les alertes actives pour les matières dont la date de péremption approche")
public ResponseEntity<List<AlertePeremptionDTO>> getAlertesActives() {
    log.debug("REST request to get active expiry alerts");
    List<AlertePeremptionDTO> alertes = stockMatiereService.getAlertesActives();
    return ResponseEntity.ok().body(alertes);
}
```

## Conclusion

L'implémentation complète des endpoints vitaux manquants est essentielle pour assurer le bon fonctionnement de l'application de gestion de fromagerie. Ce document identifie 11 endpoints critiques à compléter, avec un plan d'action structuré sur 4 semaines.

Les priorités ont été établies en fonction de l'impact sur les fonctionnalités utilisateur et les dépendances techniques. Une fois ces endpoints implémentés, l'application sera beaucoup plus robuste et complète.

## Suivi des modifications

| Date | Version | Auteur | Modifications |
|------|---------|--------|---------------|
| 2023-11-23 | 1.0 | Équipe technique | Création du document |
| 2023-11-23 | 1.1 | Équipe technique | Ajout du plan d'action et des exemples d'implémentation |
