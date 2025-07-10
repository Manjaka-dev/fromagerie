#!/bin/bash

# Script pour tester l'API de création de commande

# URL de l'API
API_URL="http://localhost:8080/api/commandes/create"

# Données de la commande au format JSON
# Remplacer clientId par un ID valide de votre base de données
JSON_DATA='{
  "clientId": "1",
  "dateCommande": "2023-12-10",
  "produits": [
    {
      "produitId": "1",
      "quantite": 2
    },
    {
      "produitId": "2",
      "quantite": 1
    }
  ]
}'

# Exécution de la requête curl
echo "Envoi de la requête à $API_URL"
echo "Données: $JSON_DATA"
echo ""

curl -X POST $API_URL \
  -H "Content-Type: application/json" \
  -d "$JSON_DATA" \
  -v

echo ""
echo "Requête terminée"
