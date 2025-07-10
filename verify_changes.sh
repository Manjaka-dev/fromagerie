#!/bin/bash

# Script de vérification des modifications
echo "===== VÉRIFICATION DES MODIFICATIONS ====="

# 1. Vérifier que la fonction formatDate a été mise à jour
echo -e "\n1. Vérification de la fonction formatDate"
grep -A 20 "formatDate" /Users/mac/Documents/L2/Gestion_proj/fromagerie_front/src/pages/commande/Commande.jsx

# 2. Vérifier les classes CSS
echo -e "\n2. Vérification des classes CSS"
grep -n "commandesList" /Users/mac/Documents/L2/Gestion_proj/fromagerie_front/src/pages/commande/Commande.jsx

# 3. Vérifier l'import du CSS
echo -e "\n3. Vérification de l'import CSS"
grep -n "import.*commande.css" /Users/mac/Documents/L2/Gestion_proj/fromagerie_front/src/pages/commande/Commande.jsx

echo -e "\n===== FIN DE LA VÉRIFICATION ====="
