#!/bin/bash

# Ce script peut être utilisé pour exécuter directement le correctif SQL
# Nécessite que les variables d'environnement DB_HOST, DB_NAME, DB_USER et DB_PASSWORD soient définies
# Ou remplacez les variables ci-dessous avec vos propres valeurs

DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-fromagerie}
DB_USER=${DB_USER:-postgres}
DB_PASSWORD=${DB_PASSWORD:-postgres}

echo "Correction des séquences pour les tables commande et ligne_commande..."
echo "Connexion à la base de données PostgreSQL..."

# Exécution de la commande SQL
psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER << EOF
-- Correction de la séquence pour la table commande
SELECT setval('commande_id_seq', (SELECT MAX(id) FROM commande));
SELECT 'Commande sequence set to: ' || last_value FROM commande_id_seq;

-- Correction de la séquence pour la table ligne_commande
SELECT setval('ligne_commande_id_seq', (SELECT MAX(id) FROM ligne_commande));
SELECT 'Ligne commande sequence set to: ' || last_value FROM ligne_commande_id_seq;
EOF

echo ""
echo "Opération terminée."
echo "Si vous n'avez pas vu d'erreur ci-dessus, la séquence a été correctement réinitialisée."
echo "Vous pouvez maintenant essayer de créer une nouvelle commande."
