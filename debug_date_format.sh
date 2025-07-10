#!/bin/bash

# Script de débogage pour les problèmes d'affichage des commandes
# Ce script vérifie le format des dates et des commandes dans l'API et le front-end

echo "===== DÉBOGAGE DE L'AFFICHAGE DES COMMANDES ====="

# 1. Vérifier la structure de la réponse API
echo -e "\n1. Structure de la réponse API pour /api/commandes"
curl -s http://localhost:8080/api/commandes | jq '.'

# 2. Vérifier le format des dates d'une commande spécifique
echo -e "\n2. Format des dates pour une commande spécifique"
COMMANDE_ID=$(curl -s http://localhost:8080/api/commandes | jq '.commandes[0].id')
echo "ID de commande extrait: $COMMANDE_ID"

if [ "$COMMANDE_ID" != "null" ]; then
  echo "Détails de la commande $COMMANDE_ID:"
  curl -s "http://localhost:8080/api/commandes/$COMMANDE_ID" | jq '.'
  
  echo "Structure de la date de commande:"
  curl -s "http://localhost:8080/api/commandes/$COMMANDE_ID" | jq '.dateCommande | type'
  
  echo "Valeur de la date de commande:"
  curl -s "http://localhost:8080/api/commandes/$COMMANDE_ID" | jq '.dateCommande'
else
  echo "Aucune commande trouvée dans la réponse de l'API"
fi

# 3. Vérifier comment le front-end devrait interpréter les dates
echo -e "\n3. Test d'interprétation des dates en JavaScript"
node -e "
const dateArray = $(curl -s "http://localhost:8080/api/commandes/$COMMANDE_ID" | jq '.dateCommande');
console.log('Date reçue:', dateArray);
if (Array.isArray(dateArray)) {
  const [year, month, day] = dateArray;
  // Les mois dans JavaScript sont 0-indexés (0 = janvier)
  const date = new Date(year, month - 1, day);
  console.log('Date formatée:', date.toLocaleDateString('fr-FR'));
} else {
  console.log('Format non géré:', typeof dateArray);
}
"

echo -e "\n===== FIN DU DÉBOGAGE ====="
