# Diagnostic des problèmes de création de fiches de production

## Problème identifié

Lors de la création d'une nouvelle fiche de production via l'interface utilisateur, l'API renvoyait l'erreur suivante :

```json
JSON parse error: Cannot construct instance of `itu.fromagerie.fromagerie.entities.produit.Produit` (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value ('TRUC')
```

## Analyse du problème

### Côté frontend

1. Le formulaire envoyait les données du produit sous forme de chaîne de caractères simple :

   ```javascript
   const ficheData = {
     produit: product.name,  // Ici : juste une chaîne au lieu d'un objet
     categorie: product.category,
     // ...autres propriétés
   };
   ```

### Côté backend

1. L'entité `FicheProduction` attend un objet `Produit` complexe, pas une simple chaîne :

   ```java
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "produit_id")
   private Produit produit;
   ```

2. Jackson (la bibliothèque de sérialisation JSON) essayait de convertir la chaîne "TRUC" en objet `Produit` mais ne trouvait pas de constructeur approprié.

## Solutions implémentées

### 1. Création d'un DTO spécifique

Nous avons créé un Data Transfer Object (DTO) dédié à la création de fiches de production :

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FicheProductionDTO {
    // Informations du produit
    private String nomProduit;
    private String categorie;
    private String poids;
    private String prixRevient;
    private String prixVente;
    private String ingredients;
    private String allergenes;
    private String dateExpiration;
    private String dateCreation;
    
    // Informations spécifiques à la fiche
    private BigDecimal quantiteNecessaire;
}
```

### 2. Adaptation du contrôleur backend

Nous avons modifié le contrôleur pour accepter ce DTO et mieux gérer les erreurs :

```java
@PostMapping
public ResponseEntity<?> createFicheProduction(@RequestBody FicheProductionDTO ficheDTO) {
    try {
        FicheProduction savedFiche = ficheProductionService.creerFicheProductionDepuisDTO(ficheDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFiche);
    } catch (Exception e) {
        // Log l'erreur et retourne un message d'erreur approprié
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Erreur lors de la création de la fiche: " + e.getMessage());
    }
}
```

### 3. Ajout de la logique de conversion DTO → entités

Nous avons implémenté une méthode dans le service pour convertir le DTO en entités :

```java
public FicheProduction creerFicheProductionDepuisDTO(FicheProductionDTO ficheDTO) {
    // Recherche ou création de la catégorie
    CategorieProduit categorie = categorieRepository.findByNom(ficheDTO.getCategorie())
        .orElseGet(() -> {
            CategorieProduit nouvCat = new CategorieProduit();
            nouvCat.setNom(ficheDTO.getCategorie());
            return categorieRepository.save(nouvCat);
        });
    
    // Création du produit avec les bonnes conversions de types
    Produit produit = new Produit();
    produit.setNom(ficheDTO.getNomProduit());
    produit.setCategorie(categorie);
    // ...conversion des autres champs...
    
    // Création de la fiche de production
    FicheProduction fiche = new FicheProduction();
    fiche.setProduit(produit);
    fiche.setQuantiteNecessaire(ficheDTO.getQuantiteNecessaire());
    
    return ficheProductionRepo.save(fiche);
}
```

### 4. Adaptation du frontend

Le frontend a été modifié pour envoyer les données dans le format attendu :

```javascript
const ficheData = {
  nomProduit: product.name,
  categorie: product.category,
  poids: product.weight,
  prixRevient: product.costPrice,
  prixVente: product.sellingPrice,
  ingredients: product.ingredients,
  allergenes: product.allergens,
  dateExpiration: product.expirationDate,
  dateCreation: new Date().toISOString().split('T')[0],
  quantiteNecessaire: 1.0
};
```

## Améliorations additionnelles

1. **Gestion des erreurs améliorée** : Affichage des messages d'erreur plus clairs dans l'interface utilisateur
2. **Validation des données** : Ajout de conversions et nettoyages des données côté backend
3. **Robustesse** : Gestion des cas où certaines données sont manquantes

## Problème supplémentaire identifié : Séquences SQL

Nous avons également constaté un problème avec les séquences PostgreSQL pour plusieurs tables, notamment :

- `categorie_produit_id_seq`
- `produit_id_seq`
- `fiche_production_id_seq`

Le problème se manifestait par des erreurs de clés primaires dupliquées :

```sql
ERROR: duplicate key value violates unique constraint "categorie_produit_pkey"
Détail : Key (id)=(1) already exists.
```

### Solution

Nous avons ajouté des commandes SQL pour réinitialiser toutes les séquences concernées dans le script `fix_commande_sequence.sql` :

```sql
-- Correction de la séquence pour la table categorie_produit
SELECT setval('categorie_produit_id_seq', (SELECT MAX(id) FROM categorie_produit));

-- Correction de la séquence pour la table produit
SELECT setval('produit_id_seq', (SELECT MAX(id) FROM produit));

-- Correction de la séquence pour la table fiche_production
SELECT setval('fiche_production_id_seq', (SELECT MAX(id) FROM fiche_production));
```

Ce script doit être exécuté après chaque restauration de base de données ou lorsque des problèmes de séquence sont détectés.

## Recommandations

1. Utiliser systématiquement des DTOs pour les opérations de création/mise à jour d'entités complexes
2. Implémenter une validation plus stricte des données côté frontend
3. Ajouter des tests unitaires pour ces fonctionnalités critiques
4. Exécuter le script de correction des séquences après chaque restauration de la base de données
5. Vérifier et maintenir la cohérence entre les entités et les DTOs lors de modifications du modèle de données
