-- Script SQL pour corriger les séquences des IDs de commande, ligne_commande et livraison dans PostgreSQL
-- Exécuter ce script directement dans PostgreSQL via psql ou pgAdmin

-- Correction de la séquence pour la table commande
SELECT setval('commande_id_seq', (SELECT MAX(id) FROM commande));
SELECT 'Commande sequence set to: ' || last_value FROM commande_id_seq;

-- Correction de la séquence pour la table ligne_commande
SELECT setval('ligne_commande_id_seq', (SELECT MAX(id) FROM ligne_commande));
SELECT 'Ligne commande sequence set to: ' || last_value FROM ligne_commande_id_seq;

-- Correction de la séquence pour la table livraison
SELECT setval('livraison_id_seq', (SELECT MAX(id) FROM livraison));
SELECT 'Livraison sequence set to: ' || last_value FROM livraison_id_seq;

-- Correction de la séquence pour la table categorie_produit
SELECT setval('categorie_produit_id_seq', (SELECT MAX(id) FROM categorie_produit));
SELECT 'Categorie produit sequence set to: ' || last_value FROM categorie_produit_id_seq;

-- Correction de la séquence pour la table produit
SELECT setval('produit_id_seq', (SELECT MAX(id) FROM produit));
SELECT 'Produit sequence set to: ' || last_value FROM produit_id_seq;

-- Correction de la séquence pour la table fiche_production
SELECT setval('fiche_production_id_seq', (SELECT MAX(id) FROM fiche_production));
SELECT 'Fiche production sequence set to: ' || last_value FROM fiche_production_id_seq;
