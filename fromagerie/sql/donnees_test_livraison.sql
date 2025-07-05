-- ===============================================
-- SCRIPT SQL DE DONNÉES DE TEST - MODULE LIVRAISON
-- Fromagerie - Gestion des livraisons
-- ===============================================

-- Désactiver les contraintes de clé étrangère temporairement
SET FOREIGN_KEY_CHECKS = 0;

-- ===============================================
-- 1. DONNÉES DE TEST POUR LES CLIENTS
-- ===============================================

INSERT INTO client (id, nom, telephone, adresse) VALUES
(1, 'Restaurant Le Gourmet', '0342345678', '15 Avenue de la République, Antananarivo'),
(2, 'Hôtel Colbert', '0342456789', '45 Rue Printsy Ratsimamanga, Antananarivo'),
(3, 'Supermarché Score', '0342567890', 'Ankorondrano, Antananarivo'),
(4, 'Épicerie Fine de la Capitale', '0342678901', '12 Rue Rabearivelo, Antananarivo'),
(5, 'Restaurant La Table Française', '0342789012', '8 Rue Rainandriamampandry, Antananarivo'),
(6, 'Boulangerie Artisanale Martin', '0342890123', '25 Avenue de l''Indépendance, Antananarivo'),
(7, 'Traiteur Excellence', '0342901234', '33 Lalana Rainivoninahitriniony, Antananarivo'),
(8, 'Café de la Gare', '0343012345', '5 Avenue de la Gare, Antananarivo'),
(9, 'Fromagerie Rivière', '0343123456', '18 Rue Andrianary, Fianarantsoa'),
(10, 'Epicerie du Marché', '0343234567', '42 Analakely, Antananarivo');

-- ===============================================
-- 2. DONNÉES DE TEST POUR LES CATÉGORIES DE PRODUITS
-- ===============================================

INSERT INTO categorie_produit (id, nom) VALUES
(1, 'Fromages Frais'),
(2, 'Fromages Affinés'),
(3, 'Fromages à Pâte Dure'),
(4, 'Fromages de Chèvre'),
(5, 'Spécialités Artisanales');

-- ===============================================
-- 3. DONNÉES DE TEST POUR LES PRODUITS
-- ===============================================

INSERT INTO produit (id, nom, poids, prix_vente, prix_revient, ingredients, allergenes, date_peremption, categorie_id) VALUES
(1, 'Gouda Artisanal 250g', 0.25, 15000.00, 8000.00, 'Lait de vache, ferments lactiques, présure, sel', 'Lait', '2024-04-15', 3),
(2, 'Gouda Artisanal 500g', 0.50, 28000.00, 15000.00, 'Lait de vache, ferments lactiques, présure, sel', 'Lait', '2024-04-20', 3),
(3, 'Gouda Artisanal 1kg', 1.00, 52000.00, 28000.00, 'Lait de vache, ferments lactiques, présure, sel', 'Lait', '2024-04-25', 3),
(4, 'Fromage Frais Nature 200g', 0.20, 8000.00, 4000.00, 'Lait de vache, ferments lactiques', 'Lait', '2024-03-10', 1),
(5, 'Fromage Frais aux Herbes 200g', 0.20, 9500.00, 5000.00, 'Lait de vache, ferments lactiques, herbes de Provence', 'Lait', '2024-03-12', 1),
(6, 'Tome de Montagne 400g', 0.40, 22000.00, 12000.00, 'Lait de vache, ferments lactiques, présure, sel', 'Lait', '2024-05-01', 2),
(7, 'Chèvre Frais 150g', 0.15, 12000.00, 6500.00, 'Lait de chèvre, ferments lactiques', 'Lait', '2024-03-08', 4),
(8, 'Crottin de Chèvre 80g', 0.08, 7500.00, 4000.00, 'Lait de chèvre, ferments lactiques, présure', 'Lait', '2024-04-05', 4),
(9, 'Spécialité Malgache 300g', 0.30, 18000.00, 10000.00, 'Lait de zébu, ferments traditionnels, épices locales', 'Lait', '2024-04-30', 5),
(10, 'Yaourt Fermier 500ml', 0.50, 6000.00, 3000.00, 'Lait de vache, ferments lactiques', 'Lait', '2024-03-05', 1);

-- ===============================================
-- 4. DONNÉES DE TEST POUR LES COMMANDES
-- ===============================================

INSERT INTO commande (id, client_id, date_commande, statut) VALUES
(1, 1, '2024-02-15', 'confirmée'),
(2, 2, '2024-02-16', 'confirmée'),
(3, 3, '2024-02-17', 'en_preparation'),
(4, 4, '2024-02-18', 'confirmée'),
(5, 5, '2024-02-19', 'confirmée'),
(6, 6, '2024-02-20', 'en_preparation'),
(7, 7, '2024-02-21', 'confirmée'),
(8, 8, '2024-02-22', 'livrée'),
(9, 9, '2024-02-23', 'confirmée'),
(10, 10, '2024-02-24', 'en_preparation'),
(11, 1, '2024-02-25', 'confirmée'),
(12, 3, '2024-02-26', 'confirmée');

-- ===============================================
-- 5. DONNÉES DE TEST POUR LES LIGNES DE COMMANDES
-- ===============================================

INSERT INTO ligne_commande (id, commande_id, produit_id, quantite, prix_unitaire) VALUES
-- Commande 1 (Restaurant Le Gourmet)
(1, 1, 1, 10, 15000.00),
(2, 1, 2, 5, 28000.00),
(3, 1, 6, 3, 22000.00),

-- Commande 2 (Hôtel Colbert)
(4, 2, 3, 2, 52000.00),
(5, 2, 4, 12, 8000.00),
(6, 2, 5, 8, 9500.00),

-- Commande 3 (Supermarché Score)
(7, 3, 1, 20, 15000.00),
(8, 3, 2, 15, 28000.00),
(9, 3, 10, 30, 6000.00),

-- Commande 4 (Épicerie Fine)
(10, 4, 7, 6, 12000.00),
(11, 4, 8, 10, 7500.00),
(12, 4, 9, 4, 18000.00),

-- Commande 5 (Restaurant La Table)
(13, 5, 2, 8, 28000.00),
(14, 5, 6, 5, 22000.00),
(15, 5, 7, 4, 12000.00),

-- Commande 6 (Boulangerie Martin)
(16, 6, 4, 15, 8000.00),
(17, 6, 5, 10, 9500.00),

-- Commande 7 (Traiteur Excellence)
(18, 7, 1, 12, 15000.00),
(19, 7, 3, 3, 52000.00),
(20, 7, 9, 5, 18000.00),

-- Commande 8 (Café de la Gare) - Déjà livrée
(21, 8, 4, 8, 8000.00),
(22, 8, 10, 20, 6000.00),

-- Commande 9 (Fromagerie Rivière)
(23, 9, 2, 6, 28000.00),
(24, 9, 6, 4, 22000.00),
(25, 9, 8, 8, 7500.00),

-- Commande 10 (Epicerie du Marché)
(26, 10, 1, 25, 15000.00),
(27, 10, 4, 20, 8000.00),
(28, 10, 10, 40, 6000.00),

-- Commande 11 (Restaurant Le Gourmet - 2ème commande)
(29, 11, 3, 4, 52000.00),
(30, 11, 7, 6, 12000.00),

-- Commande 12 (Supermarché Score - 2ème commande)
(31, 12, 1, 15, 15000.00),
(32, 12, 2, 10, 28000.00),
(33, 12, 9, 3, 18000.00);

-- ===============================================
-- 6. DONNÉES DE TEST POUR LES LIVREURS
-- ===============================================

INSERT INTO livreur (id, nom, telephone) VALUES
(1, 'Rakoto Jean', '0343456789'),
(2, 'Rabe Paul', '0343567890'),
(3, 'Andry Michel', '0343678901'),
(4, 'Hery Patrick', '0343789012'),
(5, 'Nivo Daniel', '0343890123');

-- ===============================================
-- 7. DONNÉES DE TEST POUR LES LIVRAISONS
-- ===============================================

INSERT INTO livraison (id, commande_id, livreur_id, date_livraison, zone, statut_livraison) VALUES
-- Livraisons planifiées
(1, 1, 1, '2024-02-28', 'Centre-ville', 'PLANIFIEE'),
(2, 2, 2, '2024-02-28', 'Ankorondrano', 'PLANIFIEE'),
(3, 4, 3, '2024-03-01', 'Analakely', 'PLANIFIEE'),
(4, 5, 1, '2024-03-01', 'Tsaralalana', 'PLANIFIEE'),
(5, 7, 4, '2024-03-02', 'Isotry', 'PLANIFIEE'),

-- Livraisons en cours
(6, 9, 2, '2024-02-27', 'Fianarantsoa', 'EN_COURS'),
(7, 11, 3, '2024-02-27', 'Centre-ville', 'EN_COURS'),

-- Livraisons terminées
(8, 8, 5, '2024-02-26', '67 Ha', 'LIVREE'),

-- Livraisons avec problèmes
(9, 3, 4, '2024-02-29', 'Ankorondrano', 'ECHEC'),
(10, 6, 1, '2024-03-01', 'Andravoahangy', 'ANNULEE');

-- ===============================================
-- 8. DONNÉES DE TEST POUR LES PRODUITS DE LIVRAISON
-- ===============================================

INSERT INTO livraison_produit (id, livraison_id, produit_id, quantite_a_livrer, quantite_livree) VALUES
-- Livraison 1 (Restaurant Le Gourmet)
(1, 1, 1, 10, 0),
(2, 1, 2, 5, 0),
(3, 1, 6, 3, 0),

-- Livraison 2 (Hôtel Colbert)
(4, 2, 3, 2, 0),
(5, 2, 4, 12, 0),
(6, 2, 5, 8, 0),

-- Livraison 3 (Épicerie Fine)
(7, 3, 7, 6, 0),
(8, 3, 8, 10, 0),
(9, 3, 9, 4, 0),

-- Livraison 4 (Restaurant La Table)
(10, 4, 2, 8, 0),
(11, 4, 6, 5, 0),
(12, 4, 7, 4, 0),

-- Livraison 5 (Traiteur Excellence)
(13, 5, 1, 12, 0),
(14, 5, 3, 3, 0),
(15, 5, 9, 5, 0),

-- Livraison 6 (Fromagerie Rivière) - En cours
(16, 6, 2, 6, 3),
(17, 6, 6, 4, 2),
(18, 6, 8, 8, 5),

-- Livraison 7 (Restaurant Le Gourmet - 2ème) - En cours
(19, 7, 3, 4, 2),
(20, 7, 7, 6, 6),

-- Livraison 8 (Café de la Gare) - Terminée
(21, 8, 4, 8, 8),
(22, 8, 10, 20, 20),

-- Livraison 9 (Supermarché Score) - Échec
(23, 9, 1, 20, 0),
(24, 9, 2, 15, 0),
(25, 9, 10, 30, 0),

-- Livraison 10 (Boulangerie Martin) - Annulée
(26, 10, 4, 15, 0),
(27, 10, 5, 10, 0);

-- ===============================================
-- 9. DONNÉES DE TEST POUR L'HISTORIQUE DES STATUTS
-- ===============================================

INSERT INTO statut_livraison (id, livraison_id, statut, date_statut, commentaire) VALUES
-- Historique pour la livraison 1
(1, 1, 'PLANIFIEE', '2024-02-25 09:00:00', 'Livraison programmée pour le Restaurant Le Gourmet'),

-- Historique pour la livraison 2
(2, 2, 'PLANIFIEE', '2024-02-25 09:15:00', 'Livraison programmée pour l''Hôtel Colbert'),

-- Historique pour la livraison 6 (en cours)
(3, 6, 'PLANIFIEE', '2024-02-25 10:00:00', 'Livraison programmée pour Fianarantsoa'),
(4, 6, 'EN_COURS', '2024-02-27 08:00:00', 'Départ du livreur vers Fianarantsoa'),

-- Historique pour la livraison 7 (en cours)
(5, 7, 'PLANIFIEE', '2024-02-25 10:30:00', 'Livraison programmée pour le centre-ville'),
(6, 7, 'EN_COURS', '2024-02-27 09:30:00', 'Livraison en cours de distribution'),

-- Historique pour la livraison 8 (terminée)
(7, 8, 'PLANIFIEE', '2024-02-24 14:00:00', 'Livraison programmée pour le Café de la Gare'),
(8, 8, 'EN_COURS', '2024-02-26 08:00:00', 'Départ pour la livraison'),
(9, 8, 'LIVREE', '2024-02-26 10:30:00', 'Livraison effectuée avec succès'),

-- Historique pour la livraison 9 (échec)
(10, 9, 'PLANIFIEE', '2024-02-26 11:00:00', 'Livraison programmée pour le Supermarché Score'),
(11, 9, 'EN_COURS', '2024-02-29 08:00:00', 'Départ pour la livraison'),
(12, 9, 'ECHEC', '2024-02-29 10:00:00', 'Client absent, magasin fermé'),

-- Historique pour la livraison 10 (annulée)
(13, 10, 'PLANIFIEE', '2024-02-27 15:00:00', 'Livraison programmée pour la Boulangerie Martin'),
(14, 10, 'ANNULEE', '2024-03-01 08:00:00', 'Annulation demandée par le client');

-- ===============================================
-- 10. DONNÉES DE TEST POUR LES CONFIRMATIONS DE RÉCEPTION
-- ===============================================

INSERT INTO confirmation_reception (id, livraison_id, signature, photo_reception) VALUES
(1, 8, 'M. Dupont - Café de la Gare', 'photo_reception_cafe_gare_20240226.jpg');

-- ===============================================
-- 11. DONNÉES DE TEST POUR LES RETOURS DE LIVRAISON
-- ===============================================

INSERT INTO retour_livraison (id, livraison_id, produit_id, quantite_retour, raison) VALUES
-- Retour partiel sur livraison en cours (Fianarantsoa)
(1, 6, 2, 1, 'Emballage endommagé pendant le transport'),
(2, 6, 8, 2, 'Date de péremption trop proche selon le client'),

-- Retour suite à l'échec de livraison
(3, 9, 1, 20, 'Client absent - retour complet des produits'),
(4, 9, 2, 15, 'Client absent - retour complet des produits'),
(5, 9, 10, 30, 'Client absent - retour complet des produits');

-- ===============================================
-- 12. DONNÉES DE TEST POUR LES FACTURES (optionnel)
-- ===============================================

INSERT INTO facture (id, commande_id, date_facture, montant_total) VALUES
(1, 1, '2024-02-15', 216000.00), -- Restaurant Le Gourmet
(2, 2, '2024-02-16', 276000.00), -- Hôtel Colbert
(3, 4, '2024-02-18', 219000.00), -- Épicerie Fine
(4, 5, '2024-02-19', 382000.00), -- Restaurant La Table
(5, 7, '2024-02-21', 396000.00), -- Traiteur Excellence
(6, 8, '2024-02-22', 184000.00), -- Café de la Gare (livrée)
(7, 9, '2024-02-23', 316000.00), -- Fromagerie Rivière
(8, 11, '2024-02-25', 280000.00); -- Restaurant Le Gourmet (2ème)

-- ===============================================
-- 13. DONNÉES DE TEST POUR LES PAIEMENTS (optionnel)
-- ===============================================

INSERT INTO paiement (id, commande_id, date_paiement, montant, methode) VALUES
-- Paiements reçus
(1, 8, '2024-02-26', 184000.00, 'Espèces'), -- Café de la Gare (livraison terminée)
(2, 1, '2024-02-20', 100000.00, 'Virement'), -- Acompte Restaurant Le Gourmet
(3, 2, '2024-02-18', 276000.00, 'Chèque'), -- Hôtel Colbert (paiement complet)
(4, 5, '2024-02-22', 200000.00, 'Virement'), -- Acompte Restaurant La Table
(5, 9, '2024-02-25', 316000.00, 'Virement'); -- Fromagerie Rivière (paiement anticipé)

-- Réactiver les contraintes de clé étrangère
SET FOREIGN_KEY_CHECKS = 1;

-- ===============================================
-- REQUÊTES DE VÉRIFICATION
-- ===============================================

-- Vérification du nombre d'enregistrements créés
SELECT 'Clients' as Table_Name, COUNT(*) as Count FROM client
UNION ALL
SELECT 'Produits', COUNT(*) FROM produit
UNION ALL
SELECT 'Commandes', COUNT(*) FROM commande
UNION ALL
SELECT 'Lignes de commande', COUNT(*) FROM ligne_commande
UNION ALL
SELECT 'Livreurs', COUNT(*) FROM livreur
UNION ALL
SELECT 'Livraisons', COUNT(*) FROM livraison
UNION ALL
SELECT 'Produits de livraison', COUNT(*) FROM livraison_produit
UNION ALL
SELECT 'Historiques de statuts', COUNT(*) FROM statut_livraison
UNION ALL
SELECT 'Confirmations de réception', COUNT(*) FROM confirmation_reception
UNION ALL
SELECT 'Retours de livraison', COUNT(*) FROM retour_livraison
UNION ALL
SELECT 'Factures', COUNT(*) FROM facture
UNION ALL
SELECT 'Paiements', COUNT(*) FROM paiement;

-- Vérification des livraisons par statut
SELECT 
    statut_livraison,
    COUNT(*) as nombre_livraisons
FROM livraison
GROUP BY statut_livraison
ORDER BY nombre_livraisons DESC;

-- Vérification des livraisons avec leurs détails
SELECT 
    l.id as livraison_id,
    c.id as commande_id,
    cl.nom as client,
    lr.nom as livreur,
    l.date_livraison,
    l.zone,
    l.statut_livraison,
    COUNT(lp.id) as nb_produits
FROM livraison l
JOIN commande c ON l.commande_id = c.id
JOIN client cl ON c.client_id = cl.id
JOIN livreur lr ON l.livreur_id = lr.id
LEFT JOIN livraison_produit lp ON l.id = lp.livraison_id
GROUP BY l.id, c.id, cl.nom, lr.nom, l.date_livraison, l.zone, l.statut_livraison
ORDER BY l.date_livraison, l.id;

-- ===============================================
-- COMMENTAIRES ET INSTRUCTIONS
-- ===============================================

/*
Ce script de données de test pour le module livraison contient :

1. CLIENTS (10 clients) : Restaurants, hôtels, supermarchés, épiceries
2. PRODUITS (10 produits) : Différents fromages avec catégories
3. COMMANDES (12 commandes) : Diverses commandes avec différents statuts
4. LIGNES DE COMMANDE (33 lignes) : Détail des produits commandés
5. LIVREURS (5 livreurs) : Équipe de livraison
6. LIVRAISONS (10 livraisons) : Avec différents statuts (planifiée, en cours, livrée, échec, annulée)
7. PRODUITS DE LIVRAISON (27 lignes) : Détail des produits à livrer par livraison
8. HISTORIQUE DES STATUTS (14 entrées) : Suivi des changements de statut
9. CONFIRMATIONS DE RÉCEPTION (1 confirmation) : Pour la livraison terminée
10. RETOURS DE LIVRAISON (5 retours) : Produits retournés pour diverses raisons
11. FACTURES (8 factures) : Documents de facturation
12. PAIEMENTS (5 paiements) : Paiements reçus

SCÉNARIOS DE TEST COUVERTS :
- Livraisons planifiées (nouvelles commandes)
- Livraisons en cours (partiellement livrées)
- Livraisons terminées avec succès
- Livraisons échouées (client absent)
- Livraisons annulées
- Retours de marchandises
- Historique des statuts
- Confirmations de réception

UTILISATION :
1. Exécuter ce script après avoir créé la structure de la base de données
2. Utiliser ces données pour tester l'API de livraison
3. Les IDs sont explicites pour faciliter les tests
4. Les dates sont cohérentes (février-mars 2024)
5. Les quantités et montants sont réalistes

ZONES DE LIVRAISON COUVERTES :
- Centre-ville, Ankorondrano, Analakely, Tsaralalana
- Isotry, 67 Ha, Andravoahangy
- Fianarantsoa (livraison longue distance)
*/
