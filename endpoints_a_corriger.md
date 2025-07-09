# Endpoints REST vitaux non fonctionnels ou à corriger

## Dashboard
- `/api/dashboard` - **DashboardController.getDashboard()**
  - Utilise des valeurs par défaut pour les bénéfices si les méthodes du repository ne sont pas implémentées
  - `evolutionCA` utilise des valeurs simulées
  - `pertes.evolution` renvoie une liste vide (commentaire TODO)
  - `pertes.repartition` renvoie une liste vide (commentaire TODO)
  - `alertes` utilise une logique basique au lieu de données réelles

- `/api/dashboard/filtre` - **DashboardController.getDashboardFiltered()**
  - Même problème que `/api/dashboard` avec des valeurs par défaut
  - Implémente des méthodes non fonctionnelles dans les repositories

- `/api/dashboard/stats-globales` - **DashboardController.getStatsGlobales()**
  - Utilise des valeurs par défaut pour certains KPIs si les méthodes ne sont pas implémentées

- `/api/dashboard/productions-recent` - **DashboardController.getProductionsRecent()**
  - En cas d'erreur, génère des données fictives aléatoires

## Stock
- `/api/stock/mouvement` - **MouvementStockProduitController.getMouvements()**
  - Renvoie une liste vide au lieu de vraies données
  - Méthodes commentées pour le repository

- `/api/stock/stock-produit-fini` - **StockProduitFiniService**
  - Certaines méthodes utilisent des valeurs codées en dur

- `/api/stock/simulation` - **SimulationProductionController.getSimulations()**
  - Partiellement corrigé mais d'autres méthodes pourraient encore utiliser des valeurs par défaut

## Statistiques
- `/api/statistiques/production` - **StatistiqueProductionController** ✅ (CORRIGÉ)
  - ✅ `getPourcentageProductionJournaliere()` utilise maintenant des données réelles de la base
  - ✅ `getTauxQualiteJournalier()` calcule maintenant le taux réel basé sur les données de production et pertes
  - ✅ `getTauxPerteJournalier()` utilise les données réelles des pertes ou estime de façon plus précise
  - ✅ `getMoyenneCommandesParSemaine()` calcule maintenant la moyenne réelle des commandes
  - ✅ `getCapaciteJournaliere()` calcule la capacité basée sur l'historique des commandes
  - ✅ `getStatistiquesSurPeriode()` renvoie maintenant des statistiques précises par jour/semaine

## Production
- `/api/production/statistiques` - **PerteProductionService**
  - Méthodes liées aux pertes de production retournent des valeurs par défaut

## Comptabilité
- `/api/bilan` et `/api/revenu` - **BilanFinancierRepository et RevenuRepository**
  - Méthodes potentiellement non implémentées comme `getTotalProfitBetween`

## Livraisons
- `/api/livraisons/planifiees` - **LivraisonService.getLivraisonsPlanifiees()**
  - En cas d'erreur, retourne une liste vide

## Services à corriger

### DashboardService
- `getEvolutionPertesParJour` - Non implémenté (TODO)
- `getRepartitionPertes` - Non implémenté (TODO)
- Utilisation excessive de try/catch avec valeurs par défaut

### StatistiqueProductionService
- Plusieurs méthodes retournent des valeurs constantes ou des estimations:
  - `getMoyenneCommandesParSemaine()` - Retourne toujours 0.0
  - `getCapaciteJournaliere()` - Retourne toujours 100
  - `getCapaciteJournaliereNative()` - Retourne toujours 100

### MouvementStockProduitService
- `getMouvements()` - Retourne une liste vide
- `getMouvementsByDate()` - Retourne une liste vide
- `getMouvementsByTypeAndDate()` - Retourne une liste vide

### BilanFinancierRepository
- `getTotalProfitBetween()` - Probablement non implémentée

### FactureRepository
- `getTotalMontantBetween()` - Probablement non implémentée

### PerteProductionRepository
- `getAverageTauxPerte()` - Probablement non implémentée

## Repository nécessitant des méthodes additionnelles

### StockProduitFiniRepository
- Nécessite `getQuantiteTotaleStockDisponible()`

### MouvementStockProduitRepository
- Nécessite `findMouvementsByDate()`
- Nécessite `findMouvementsByTypeAndDate()`

### StatistiqueProductionRepository
- Nécessite `getPourcentageProductionJournaliere()`
- Nécessite `getTauxQualiteJournalier()`
- Nécessite `getTauxPerteJournalier()`
- Nécessite `getStatistiquesCompletesJournalieres()`
- Nécessite `getStatistiquesSurPeriode()`
- Nécessite `getMoyenneCommandesParSemaine()`
- Nécessite `getCapaciteJournaliere()`
- Nécessite `getCapaciteJournaliereNative()`
