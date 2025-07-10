#!/bin/bash

echo "Correction de la séquence pour la table commande..."
curl -X POST http://localhost:8080/api/commandes/fix-sequence \
    -H "Content-Type: application/json" \
    -v

echo ""
echo "Opération terminée."
