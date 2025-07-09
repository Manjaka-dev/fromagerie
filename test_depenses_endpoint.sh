#!/bin/bash

echo "Test de l'endpoint des dépenses par catégorie"
echo "============================================="

# URL de base
BASE_URL="http://localhost:8080"

# Tester l'endpoint avec les catégories exactes trouvées dans la base
echo "Test avec 'Matières premières'..."
curl -s "$BASE_URL/api/depenses/categorie?categorie=Mati%C3%A8res%20premi%C3%A8res" | jq length

echo "Test avec 'matières premières' (minuscules)..."
curl -s "$BASE_URL/api/depenses/categorie?categorie=mati%C3%A8res%20premi%C3%A8res" | jq length

echo "Test avec 'Matieres premieres' (sans accents)..."
curl -s "$BASE_URL/api/depenses/categorie?categorie=Matieres%20premieres" | jq length

echo "Test avec 'Matière' (partiel)..."
curl -s "$BASE_URL/api/depenses/categorie?categorie=Mati%C3%A8re" | jq length

# Tester l'endpoint total avec les mêmes catégories
echo "Test du total avec 'Matières premières'..."
curl -s "$BASE_URL/api/depenses/total/categorie?categorie=Mati%C3%A8res%20premi%C3%A8res"

echo "Test du total avec 'matières premières' (minuscules)..."
curl -s "$BASE_URL/api/depenses/total/categorie?categorie=mati%C3%A8res%20premi%C3%A8res"

echo "Test du total avec 'Matieres premieres' (sans accents)..."
curl -s "$BASE_URL/api/depenses/total/categorie?categorie=Matieres%20premieres"

# Tester avec les versions encodées en URL
echo "Test avec la catégorie correctement encodée..."
curl -s "$BASE_URL/api/depenses/categorie?categorie=Mati%C3%A8res%20premi%C3%A8res" | jq length

# Lister toutes les dépenses pour vérifier
echo "Liste de toutes les dépenses..."
curl -s "$BASE_URL/api/depenses/date-range?dateDebut=2024-01-01&dateFin=2025-12-31" | jq 'map(.categorie)' | jq unique

echo "Test terminé."
