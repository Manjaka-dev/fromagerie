# Documentation des Endpoints de Statistiques de Production

## Endpoints implémentés

### 1. Pourcentage de Production Journalière
- **URL**: `/api/statistiques/production/journaliere/pourcentage`
- **Méthode**: `GET`
- **Paramètres**:
  - `date` (obligatoire): La date pour laquelle calculer le pourcentage (format ISO: YYYY-MM-DD)
  - `capaciteJournaliere` (obligatoire): La capacité journalière de production en unités
- **Réponse**:
  ```json
  {
    "date": "2023-07-09",
    "capaciteJournaliere": 100,
    "quantiteProduite": 85,
    "pourcentageProduction": "85.00%"
  }
  ```
- **Description**: Calcule le pourcentage de production par rapport à la capacité journalière pour une date donnée

### 2. Taux de Qualité Journalier
- **URL**: `/api/statistiques/production/journaliere/qualite`
- **Méthode**: `GET`
- **Paramètres**:
  - `date` (obligatoire): La date pour laquelle calculer le taux de qualité (format ISO: YYYY-MM-DD)
- **Réponse**:
  ```json
  {
    "date": "2023-07-09",
    "quantiteTotale": 85,
    "quantitePerdue": 2,
    "tauxQualite": "97.65%"
  }
  ```
- **Description**: Calcule le taux de qualité des produits fabriqués pour une journée spécifique (produits conformes / production totale)

### 3. Taux de Perte Journalier
- **URL**: `/api/statistiques/production/journaliere/perte`
- **Méthode**: `GET`
- **Paramètres**:
  - `date` (obligatoire): La date pour laquelle calculer le taux de perte (format ISO: YYYY-MM-DD)
- **Réponse**:
  ```json
  {
    "date": "2023-07-09",
    "quantiteTotale": 85,
    "quantitePerdue": 4,
    "tauxPerte": "4.71%"
  }
  ```
- **Description**: Calcule le pourcentage de pertes (déchets, produits défectueux) par rapport à la production journalière

### 4. Moyenne des Commandes par Semaine
- **URL**: `/api/statistiques/production/capacite/moyenne-semaine`
- **Méthode**: `GET`
- **Paramètres**:
  - `dateDebut` (obligatoire): Date de début de la période d'analyse (format ISO: YYYY-MM-DD)
  - `dateFin` (obligatoire): Date de fin de la période d'analyse (format ISO: YYYY-MM-DD)
- **Réponse**:
  ```json
  {
    "dateDebut": "2023-06-01",
    "dateFin": "2023-07-09",
    "moyenneCommandesParSemaine": 320.5
  }
  ```
- **Description**: Calcule le nombre moyen de commandes reçues par semaine sur une période donnée

### 5. Capacité Journalière de Production
- **URL**: `/api/statistiques/production/capacite/journaliere`
- **Méthode**: `GET`
- **Paramètres**:
  - `dateDebut` (obligatoire): Date de début pour l'analyse historique (format ISO: YYYY-MM-DD)
  - `dateFin` (obligatoire): Date de fin pour l'analyse historique (format ISO: YYYY-MM-DD)
- **Réponse**:
  ```json
  {
    "dateDebut": "2023-06-01",
    "dateFin": "2023-07-09",
    "capaciteJournaliere": 64
  }
  ```
- **Description**: Calcule la capacité journalière optimale de production basée sur l'historique des commandes

### 6. Statistiques Détaillées par Semaine
- **URL**: `/api/statistiques/production/capacite/statistiques-semaine`
- **Méthode**: `GET`
- **Paramètres**:
  - `dateDebut` (obligatoire): Date de début de la période d'analyse (format ISO: YYYY-MM-DD)
  - `dateFin` (obligatoire): Date de fin de la période d'analyse (format ISO: YYYY-MM-DD)
- **Réponse**:
  ```json
  [
    {
      "semaine": "Semaine du 2023-07-03 au 2023-07-09",
      "nombreJours": 7,
      "quantiteTotale": 450,
      "moyenneJournaliere": 64.3
    },
    {
      "semaine": "Semaine du 2023-06-26 au 2023-07-02",
      "nombreJours": 7,
      "quantiteTotale": 430,
      "moyenneJournaliere": 61.4
    }
  ]
  ```
- **Description**: Fournit les statistiques détaillées de production par semaine sur la période demandée

## Endpoints utilitaires complémentaires

### 7. Dashboard Statistiques d'Aujourd'hui
- **URL**: `/api/statistiques/production/aujourdhui`
- **Méthode**: `GET`
- **Description**: Calcule automatiquement la capacité journalière et les statistiques pour le jour actuel

### 8. Statistiques avec Capacité Automatique
- **URL**: `/api/statistiques/production/periode/auto-capacite`
- **Méthode**: `GET`
- **Paramètres**:
  - `dateDebut` (obligatoire): Date de début (format ISO: YYYY-MM-DD)
  - `dateFin` (obligatoire): Date de fin (format ISO: YYYY-MM-DD)
- **Description**: Calcule les statistiques pour une période avec détermination automatique de la capacité journalière

### 9. Dashboard Complet
- **URL**: `/api/statistiques/production/dashboard`
- **Méthode**: `GET`
- **Description**: Fournit un tableau de bord complet avec toutes les statistiques importantes sur les 3 derniers mois

## Notes techniques

- Tous les endpoints sont documentés avec Swagger/OpenAPI
- Les calculs sont basés sur les données réelles de la base de données ou estimés si les données sont insuffisantes
- Les pourcentages sont formatés avec deux décimales dans les réponses
- Les réponses incluent à la fois les valeurs brutes et les pourcentages calculés pour permettre différents affichages
