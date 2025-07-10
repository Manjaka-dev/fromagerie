#!/bin/bash

# URL de l'API backend
API_URL="http://localhost:8080/api/commandes"

# En-têtes de la requête
HEADERS="Content-Type: application/json"

# Effectuer la requête GET à l'API commandes
echo "Requête à l'API commandes: $API_URL"
echo "-----------------------------------"
curl -s -X GET $API_URL -H "$HEADERS" | jq

echo ""
echo "Terminé."
