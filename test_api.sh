#!/bin/bash

# Script de test automatisé pour l'API Fromagerie
# Usage: ./test_api.sh

BASE_URL="http://localhost:8080"
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fonction pour afficher les résultats
print_result() {
    local endpoint="$1"
    local status="$2"
    local description="$3"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    if [[ $status == 2* ]]; then
        echo -e "${GREEN}✓ PASS${NC} - $endpoint ($status) - $description"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    elif [[ $status == 4* ]]; then
        echo -e "${YELLOW}⚠ WARN${NC} - $endpoint ($status) - $description"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗ FAIL${NC} - $endpoint ($status) - $description"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
}

# Fonction pour tester un endpoint GET
test_get_endpoint() {
    local endpoint="$1"
    local description="$2"
    
    response=$(curl -s -w "%{http_code}" -o /tmp/api_response.json "$BASE_URL$endpoint")
    status_code="${response: -3}"
    
    print_result "$endpoint" "$status_code" "$description"
    
    # Si le test échoue, afficher la réponse
    if [[ ! $status_code == 2* ]] && [[ ! $status_code == 4* ]]; then
        echo "  Response: $(cat /tmp/api_response.json)"
    fi
}

# Fonction pour tester un endpoint POST
test_post_endpoint() {
    local endpoint="$1"
    local data="$2"
    local description="$3"
    
    response=$(curl -s -w "%{http_code}" -o /tmp/api_response.json \
        -X POST \
        -H "Content-Type: application/json" \
        -d "$data" \
        "$BASE_URL$endpoint")
    status_code="${response: -3}"
    
    print_result "$endpoint" "$status_code" "$description"
    
    # Si le test échoue, afficher la réponse
    if [[ ! $status_code == 2* ]] && [[ ! $status_code == 4* ]]; then
        echo "  Response: $(cat /tmp/api_response.json)"
    fi
}

echo -e "${BLUE}=================================${NC}"
echo -e "${BLUE}  TEST API FROMAGERIE - DÉBUT${NC}"
echo -e "${BLUE}=================================${NC}"
echo ""

# ===== TESTS CLIENTS =====
echo -e "${YELLOW}=== CLIENTS ===${NC}"
test_get_endpoint "/api/clients" "Liste des clients"
test_get_endpoint "/api/clients/1" "Client par ID"
test_get_endpoint "/api/clients/999" "Client inexistant (doit retourner 404)"

# ===== TESTS PRODUITS =====
echo -e "${YELLOW}=== PRODUITS ===${NC}"
test_get_endpoint "/api/produits" "Liste des produits"
test_get_endpoint "/api/produits/1" "Produit par ID"
test_get_endpoint "/api/produits/categories" "Catégories de produits"
test_get_endpoint "/api/produits/stats" "Statistiques produits"
test_get_endpoint "/api/produits/stock-faible" "Produits à stock faible"
test_get_endpoint "/api/produits/search?nom=fromage" "Recherche produits"

# ===== TESTS COMMANDES =====
echo -e "${YELLOW}=== COMMANDES ===${NC}"
test_get_endpoint "/api/commandes" "Liste des commandes"
test_get_endpoint "/api/commandes/1" "Commande par ID"
test_get_endpoint "/api/commandes/livreurs" "Liste des livreurs pour commandes"

# ===== TESTS LIVRAISONS =====
echo -e "${YELLOW}=== LIVRAISONS ===${NC}"
test_get_endpoint "/api/livraisons" "Liste des livraisons"
test_get_endpoint "/api/livraisons/commandes" "Commandes pour livraison"
test_get_endpoint "/api/livraisons/zones" "Zones de livraison"
test_get_endpoint "/api/livraisons/livraison" "Formulaire livraison"
test_get_endpoint "/api/livraisons/1" "Livraison par ID"

# ===== TESTS LIVREURS =====
echo -e "${YELLOW}=== LIVREURS ===${NC}"
test_get_endpoint "/api/livreurs" "Liste des livreurs"
test_get_endpoint "/api/livreurs/1" "Livreur par ID"

# ===== TESTS PAIEMENTS =====
echo -e "${YELLOW}=== PAIEMENTS ===${NC}"
test_get_endpoint "/api/paiements" "Liste des paiements"
test_get_endpoint "/api/paiements/commande/1" "Paiements par commande"

# ===== TESTS STOCK =====
echo -e "${YELLOW}=== STOCK ===${NC}"
test_get_endpoint "/api/stock/matieres-premieres" "Matières premières"
test_get_endpoint "/api/stock/mouvements" "Mouvements de stock"
test_get_endpoint "/api/stock/alerts" "Alertes de stock"
test_get_endpoint "/api/stock/dechets" "Liste des déchets"
test_get_endpoint "/api/stock/alertes-peremption" "Alertes péremption"
test_get_endpoint "/api/stock/statuts" "Statuts de stock"
test_get_endpoint "/api/stock/types-mouvement" "Types de mouvement"

# ===== TESTS UTILISATEURS =====
echo -e "${YELLOW}=== UTILISATEURS ===${NC}"
test_get_endpoint "/api/utilisateurs" "Liste des utilisateurs"
test_get_endpoint "/api/utilisateurs/roles" "Rôles disponibles"
test_get_endpoint "/api/utilisateurs/1" "Utilisateur par ID"

# ===== TESTS REVENUS =====
echo -e "${YELLOW}=== REVENUS ===${NC}"
test_get_endpoint "/api/revenus/1" "Revenu par ID"
test_get_endpoint "/api/revenus/date-range?dateDebut=2024-01-01&dateFin=2024-12-31" "Revenus par période"
test_get_endpoint "/api/revenus/total?dateDebut=2024-01-01&dateFin=2024-12-31" "Total revenus"
test_get_endpoint "/api/revenus/total/date?date=2024-01-01" "Total par date"

# ===== TESTS DEPENSES =====
echo -e "${YELLOW}=== DEPENSES ===${NC}"
test_get_endpoint "/api/depenses/1" "Dépense par ID"

# ===== TESTS RETOURS LIVRAISON =====
echo -e "${YELLOW}=== RETOURS LIVRAISON ===${NC}"
test_get_endpoint "/api/retours-livraison" "Liste des retours"
test_get_endpoint "/api/retours-livraison/1" "Retour par ID"

# ===== TESTS TRANSFORMATIONS =====
echo -e "${YELLOW}=== TRANSFORMATIONS ===${NC}"
test_get_endpoint "/api/transformations/matiere/1" "Transformations par matière"
test_get_endpoint "/api/transformations/date-range?dateDebut=2024-01-01&dateFin=2024-12-31" "Transformations par période"
test_get_endpoint "/api/transformations/total/1" "Total transformé"

# ===== TESTS LIVRAISONS-PRODUITS =====
echo -e "${YELLOW}=== LIVRAISONS-PRODUITS ===${NC}"
test_get_endpoint "/api/livraisons-produits/livraison/1" "Produits d'une livraison"

echo ""
echo -e "${BLUE}=================================${NC}"
echo -e "${BLUE}      RÉSUMÉ DES TESTS${NC}"
echo -e "${BLUE}=================================${NC}"
echo -e "Total tests: ${TOTAL_TESTS}"
echo -e "${GREEN}Tests réussis: ${PASSED_TESTS}${NC}"
echo -e "${RED}Tests échoués: ${FAILED_TESTS}${NC}"

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}✓ Tous les tests sont passés !${NC}"
    exit 0
else
    echo -e "${RED}⚠ ${FAILED_TESTS} test(s) ont échoué${NC}"
    exit 1
fi
