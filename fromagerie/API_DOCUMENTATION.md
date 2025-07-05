# API REST Documentation - Fromagerie

## Base URL
```
http://localhost:8080/api
```

## Endpoints

### 1. Livraisons (`/api/livraisons`)

#### GET `/api/livraisons/commandes`
Récupère toutes les commandes avec leurs livreurs.
```json
{
  "commandes": [...],
  "livreurs": [...]
}
```

#### GET `/api/livraisons/commandes/{id}/form`
Récupère le formulaire de livraison pour une commande.
```json
{
  "commandeId": 1,
  "livreurs": [...]
}
```

#### POST `/api/livraisons/commandes/{commandeId}`
Récupère une commande par ID.
```json
{
  "commandeId": 1,
  "message": "Commande trouvée"
}
```

#### POST `/api/livraisons/livraison`
Crée une nouvelle livraison.
```json
{
  "livreur": 1,
  "dateLivraison": "2024-01-15",
  "commandeId": 1,
  "zone": "Paris centre"
}
```

#### POST `/api/livraisons/assign-livreur`
Assigne un livreur à une commande.
```json
{
  "commandeId": 1,
  "livreurId": 2
}
```

#### GET `/api/livraisons`
Récupère toutes les livraisons avec leurs détails.
```json
{
  "timestamp": 1705123456789,
  "livraisons": [...]
}
```

#### POST `/api/livraisons/{livraisonId}/statut`
Met à jour le statut d'une livraison.
```json
{
  "message": "Statut mis à jour : En cours !",
  "statutId": 5
}
```

#### GET `/api/livraisons/{livraisonId}/confirmation-paiement`
Récupère les informations pour la confirmation de paiement.
```json
{
  "livraison": {...},
  "livraisonId": 1
}
```

#### POST `/api/livraisons/{livraisonId}/confirmer-livraison-paiement`
Confirme une livraison et enregistre le paiement.
```json
{
  "montantPaiement": 150.50,
  "methodePaiement": "Carte bancaire",
  "datePaiement": "2024-01-15"
}
```

#### POST `/api/livraisons/retour-livraison`
Enregistre un retour de livraison.
```json
{
  "commandeId": 1
}
```

#### GET `/api/livraisons/commandes/{id}/export-pdf`
Exporte une commande en PDF.

### 2. Commandes (`/api/commandes`)

#### POST `/api/commandes/panier`
Crée une commande avec des produits.
```json
{
  "clientId": 1,
  "produit1": "10",
  "produit2": "5"
}
```

### 3. Produits (`/api/produits`)

#### GET `/api/produits`
Récupère tous les produits avec leurs quantités disponibles et la liste des clients.
```json
{
  "produits": [...],
  "quantiteDisponible": [100, 50, 75],
  "listeClient": [...]
}
```

### 4. Clients (`/api/clients`)

#### GET `/api/clients`
Récupère tous les clients.
```json
[
  {
    "id": 1,
    "nom": "Client 1",
    "telephone": "0123456789"
  }
]
```

#### GET `/api/clients/{id}`
Récupère un client par ID.

#### POST `/api/clients`
Crée un nouveau client.
```json
{
  "nom": "Nouveau Client",
  "telephone": "0987654321"
}
```

#### PUT `/api/clients/{id}`
Met à jour un client.
```json
{
  "nom": "Client Modifié",
  "telephone": "0987654321"
}
```

#### DELETE `/api/clients/{id}`
Supprime un client.

### 5. Livreurs (`/api/livreurs`)

#### GET `/api/livreurs`
Récupère tous les livreurs.
```json
[
  {
    "id": 1,
    "nom": "Jean Dupont"
  }
]
```

#### GET `/api/livreurs/{id}`
Récupère un livreur par ID.

#### POST `/api/livreurs`
Crée un nouveau livreur.
```json
{
  "nom": "Nouveau Livreur"
}
```

#### PUT `/api/livreurs/{id}`
Met à jour un livreur.
```json
{
  "nom": "Livreur Modifié"
}
```

#### DELETE `/api/livreurs/{id}`
Supprime un livreur.

### 6. Paiements (`/api/paiements`)

#### POST `/api/paiements`
Enregistre un nouveau paiement.
```json
{
  "commandeId": 1,
  "montant": 150.50,
  "methode": "Carte bancaire",
  "datePaiement": "2024-01-15"
}
```

#### GET `/api/paiements/commande/{commandeId}`
Récupère les paiements d'une commande.

### 7. Retours de Livraison (`/api/retours-livraison`)

#### GET `/api/retours-livraison`
Récupère tous les retours de livraison.

#### GET `/api/retours-livraison/{id}`
Récupère un retour de livraison par ID.

#### POST `/api/retours-livraison`
Crée un nouveau retour de livraison.
```json
{
  "livraison": {...},
  "produit": {...},
  "quantiteRetour": 5,
  "raison": "Produit défectueux"
}
```

#### PUT `/api/retours-livraison/{id}`
Met à jour un retour de livraison.

#### DELETE `/api/retours-livraison/{id}`
Supprime un retour de livraison.

## Codes de Réponse

- `200 OK` : Requête réussie
- `201 Created` : Ressource créée avec succès
- `400 Bad Request` : Requête malformée
- `404 Not Found` : Ressource non trouvée
- `500 Internal Server Error` : Erreur serveur

## Format des Réponses

Toutes les réponses d'erreur suivent ce format :
```json
{
  "error": "Message d'erreur détaillé"
}
```

Toutes les réponses de succès suivent ce format :
```json
{
  "message": "Message de succès",
  "data": {...}
}
```

## Exemples d'Utilisation

### Créer une livraison
```bash
curl -X POST http://localhost:8080/api/livraisons/livraison \
  -H "Content-Type: application/json" \
  -d '{
    "livreur": 1,
    "dateLivraison": "2024-01-15",
    "commandeId": 1,
    "zone": "Paris centre"
  }'
```

### Mettre à jour le statut d'une livraison
```bash
curl -X POST http://localhost:8080/api/livraisons/1/statut \
  -H "Content-Type: application/json"
```

### Récupérer tous les produits
```bash
curl -X GET http://localhost:8080/api/produits
``` 