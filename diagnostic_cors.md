# Diagnostic des problèmes CORS dans l'application Fromagerie

## Problème identifié

Lors de l'accès au tableau de bord et à d'autres fonctionnalités, l'application frontend (`http://localhost:3000`) rencontrait des erreurs CORS (Cross-Origin Resource Sharing) en tentant d'accéder aux API du backend (`http://localhost:8080`), avec des erreurs comme :

```log
Access to fetch at 'http://localhost:8080/api/dashboard/stats-globales' from origin 'http://localhost:3000' has been blocked by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

## Analyse du problème

Le problème était causé par une incompatibilité entre :

1. La configuration CORS globale dans `WebConfig.java` qui définissait des paramètres corrects, incluant `allowCredentials(true)` qui est crucial pour les sessions
2. Des annotations `@CrossOrigin(origins = "*")` spécifiques au niveau des contrôleurs, qui :
   - Remplaçaient la configuration globale pour ces contrôleurs spécifiques
   - Utilisaient un wildcard `*` qui est incompatible avec `allowCredentials(true)` selon la spécification CORS

## Solution implémentée

Nous avons mis en œuvre une solution en deux parties :

### 1. Suppression des annotations CORS au niveau des contrôleurs

Nous avons supprimé toutes les annotations `@CrossOrigin` des contrôleurs suivants :

- `DashboardController`
- `StockProduitFiniController`
- `StatistiqueProductionController`
- `LivreurController`
- `LivraisonProduitController`
- `MouvementStockProduitController`
- `SimulationProductionController`
- `StockMatiereController`
- `LotProduitController`
- `UtilisateurController`
- `AuthController`

### 2. Amélioration de la configuration CORS globale

Nous avons amélioré la configuration CORS globale dans `WebConfig.java` pour :

- Ajouter une configuration pour `/**` en plus de celle pour `/api/**`
- Spécifier plus précisément les headers autorisés
- Exposer certains headers CORS explicitement
- S'assurer que tous les endpoints utilisent la même configuration CORS

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // Configuration globale pour tous les endpoints
            .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With")
            .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
            .allowCredentials(true)
            .maxAge(3600);
    
    // Configuration spécifique pour /api/**
    registry.addMapping("/api/**") 
            .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With")
            .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
            .allowCredentials(true)
            .maxAge(3600);
}
```

## Points d'attention pour l'avenir

1. **Configuration uniforme** : Privilégier une configuration CORS globale plutôt que des annotations au niveau des contrôleurs pour assurer la cohérence.

2. **Wildcards et Credentials** : Il est important de noter que selon la spécification CORS, `origins="*"` est incompatible avec `allowCredentials=true`. Toujours spécifier explicitement les origines autorisées lorsque `allowCredentials=true` est nécessaire.

3. **Environnements de production** : Pour un déploiement en production, limiter les origines autorisées aux domaines d'application spécifiques plutôt que d'utiliser des origines de développement local.

4. **Tests préflight** : Les requêtes CORS avec méthodes non simples (comme PUT, DELETE) ou avec certains headers personnalisés déclenchent des requêtes "preflight" OPTIONS que le serveur doit gérer correctement.
