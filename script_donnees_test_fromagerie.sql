-- =====================================================================
-- SCRIPT DE DONNÉES DE TEST POUR LA FROMAGERIE
-- =====================================================================
-- Ce script génère des données de test concrètes pour toutes les entités
-- du projet fromagerie. Il respecte l'ordre des dépendances FK.
-- Basé sur les entités JPA réelles du projet.
-- =====================================================================

-- Désactiver les contraintes temporairement (équivalent PostgreSQL)
SET session_replication_role = 'replica';

-- =====================================================================
-- NETTOYAGE DES DONNÉES EXISTANTES
-- =====================================================================

-- Vider toutes les tables dans l'ordre inverse des dépendances
TRUNCATE TABLE confirmation_reception CASCADE;
TRUNCATE TABLE retour_livraison CASCADE;
TRUNCATE TABLE livraison_produit CASCADE;
TRUNCATE TABLE statut_livraison CASCADE;
TRUNCATE TABLE paiement CASCADE;
TRUNCATE TABLE facture CASCADE;
TRUNCATE TABLE ligne_commande CASCADE;
TRUNCATE TABLE commande CASCADE;
TRUNCATE TABLE livraison CASCADE;
TRUNCATE TABLE livreur CASCADE;
TRUNCATE TABLE client CASCADE;

TRUNCATE TABLE revenu CASCADE;
TRUNCATE TABLE depense CASCADE;
TRUNCATE TABLE bilan_financier CASCADE;

TRUNCATE TABLE statistique_vente CASCADE;
TRUNCATE TABLE statistique_production CASCADE;
TRUNCATE TABLE statistique_stock CASCADE;
TRUNCATE TABLE rapport_personnalise CASCADE;

TRUNCATE TABLE stock_produit_fini CASCADE;
TRUNCATE TABLE mouvement_stock_produit CASCADE;
TRUNCATE TABLE mouvement_stock_matiere CASCADE;
TRUNCATE TABLE stock_matiere CASCADE;
TRUNCATE TABLE alerte_peremption CASCADE;
TRUNCATE TABLE simulation_production CASCADE;

TRUNCATE TABLE perte_production CASCADE;
TRUNCATE TABLE production_effectuee CASCADE;
TRUNCATE TABLE transformation_dechet CASCADE;
TRUNCATE TABLE fiche_production CASCADE;

TRUNCATE TABLE lot_produit CASCADE;
TRUNCATE TABLE produit CASCADE;
TRUNCATE TABLE categorie_produit CASCADE;
TRUNCATE TABLE matiere_premiere CASCADE;

TRUNCATE TABLE promotion CASCADE;

TRUNCATE TABLE journal_connexion CASCADE;
-- Supprimer token_reset qui n'existe pas dans toutes les bases
-- TRUNCATE TABLE token_reset CASCADE;
TRUNCATE TABLE utilisateur CASCADE;
TRUNCATE TABLE role CASCADE;

-- =====================================================================
-- DONNÉES DE BASE - UTILISATEURS ET RÔLES
-- =====================================================================

-- Rôles
INSERT INTO role (id, nom) VALUES
(1, 'ADMIN'),
(2, 'GESTIONNAIRE'),
(3, 'VENDEUR'),
(4, 'LIVREUR'),
(5, 'PRODUCTION');

-- Utilisateurs
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role_id) VALUES
(1, 'Jean Rakoto', 'jean.rakoto@fromagerie.mg', '$2a$10$xGHhE7qF8zqF8zqF8zqF8O', 1),
(2, 'Marie Rasoanirina', 'marie.raso@fromagerie.mg', '$2a$10$yH8iF9rG9rG9rG9rG9rG9P', 2),
(3, 'Paul Andriamana', 'paul.andria@fromagerie.mg', '$2a$10$zI9jG0sH0sH0sH0sH0sH0Q', 3),
(4, 'Sophie Randria', 'sophie.randria@fromagerie.mg', '$2a$10$aJ0kH1tI1tI1tI1tI1tI1R', 4),
(5, 'Michel Rabemanana', 'michel.rabe@fromagerie.mg', '$2a$10$bK1lI2uJ2uJ2uJ2uJ2uJ2S', 5);

-- Journal de connexion
INSERT INTO journal_connexion (id, utilisateur_id, date_connexion, action) VALUES
(1, 1, '2024-07-01T08:00:00', 'Connexion admin'),
(2, 2, '2024-07-01T08:30:00', 'Connexion gestionnaire'),
(3, 3, '2024-07-01T09:00:00', 'Connexion vendeur'),
(4, 4, '2024-07-01T09:30:00', 'Connexion livreur'),
(5, 5, '2024-07-01T10:00:00', 'Connexion production');

-- =====================================================================
-- DONNÉES PRODUITS ET STOCK
-- =====================================================================

-- Matières premières
INSERT INTO matiere_premiere (id, nom, unite, duree_conservation) VALUES
(1, 'Lait de vache frais', 'litres', 3),
(2, 'Lait de zébu', 'litres', 3),
(3, 'Ferments lactiques', 'grammes', 30),
(4, 'Présure naturelle', 'millilitres', 180),
(5, 'Sel fin', 'kilogrammes', 365),
(6, 'Herbes de Provence', 'grammes', 90),
(7, 'Épices malgaches', 'grammes', 120),
(8, 'Emballage plastique', 'unités', 730),
(9, 'Étiquettes', 'unités', 365),
(10, 'Cartons d''emballage', 'unités', 365);

-- Stock matières premières
INSERT INTO stock_matiere (id, matiere_id, quantite) VALUES
(1, 1, 500.00),  -- 500L lait de vache
(2, 2, 200.00),  -- 200L lait de zébu
(3, 3, 2500.00), -- 2.5kg ferments
(4, 4, 1500.00), -- 1.5L présure
(5, 5, 50.00),   -- 50kg sel
(6, 6, 5000.00), -- 5kg herbes
(7, 7, 3000.00), -- 3kg épices
(8, 8, 10000.00), -- 10000 emballages
(9, 9, 15000.00), -- 15000 étiquettes
(10, 10, 2000.00); -- 2000 cartons

-- Catégories de produits
INSERT INTO categorie_produit (id, nom) VALUES
(1, 'Fromages à pâte pressée'),
(2, 'Fromages frais'),
(3, 'Fromages de chèvre'),
(4, 'Spécialités malgaches'),
(5, 'Produits laitiers');

-- Produits
INSERT INTO produit (id, nom, poids, prix_vente, prix_revient, ingredients, allergenes, date_peremption, categorie_id) VALUES
(1, 'Gouda Artisanal 250g', 0.25, 15000.00, 8000.00, 'Lait de vache, ferments lactiques, présure, sel', 'Lait', '2024-12-31', 1),
(2, 'Gouda Artisanal 500g', 0.50, 28000.00, 15000.00, 'Lait de vache, ferments lactiques, présure, sel', 'Lait', '2024-12-31', 1),
(3, 'Gouda Artisanal 1kg', 1.00, 52000.00, 28000.00, 'Lait de vache, ferments lactiques, présure, sel', 'Lait', '2024-12-31', 1),
(4, 'Fromage Frais Nature 200g', 0.20, 8000.00, 4000.00, 'Lait de vache, ferments lactiques', 'Lait', '2024-08-15', 2),
(5, 'Fromage Frais aux Herbes 200g', 0.20, 9500.00, 5000.00, 'Lait de vache, ferments lactiques, herbes de Provence', 'Lait', '2024-08-15', 2),
(6, 'Chèvre Frais 150g', 0.15, 12000.00, 6500.00, 'Lait de chèvre, ferments lactiques, sel', 'Lait', '2024-08-20', 3),
(7, 'Crottin de Chèvre 80g', 0.08, 8500.00, 4500.00, 'Lait de chèvre, ferments lactiques, sel', 'Lait', '2024-09-30', 3),
(8, 'Tome de Montagne 400g', 0.40, 22000.00, 12000.00, 'Lait de vache, ferments lactiques, présure, sel, herbes', 'Lait', '2024-11-30', 1),
(9, 'Spécialité Malgache 300g', 0.30, 18000.00, 9500.00, 'Lait de zébu, ferments traditionnels, épices locales', 'Lait', '2024-10-15', 4),
(10, 'Yaourt Fermier 500ml', 0.50, 6000.00, 3000.00, 'Lait de vache, ferments lactiques', 'Lait', '2024-08-01', 5);

-- Lots de produits
INSERT INTO lot_produit (id, produit_id, numero_lot, date_fabrication, date_peremption, quantite) VALUES
(1, 1, 'GOD250-240701', '2024-07-01', '2024-08-01', 100),
(2, 2, 'GOD500-240701', '2024-07-01', '2024-08-01', 80),
(3, 3, 'GOD1KG-240701', '2024-07-01', '2024-08-01', 50),
(4, 4, 'FRN200-240702', '2024-07-02', '2024-07-12', 120),
(5, 5, 'FRH200-240702', '2024-07-02', '2024-07-12', 100),
(6, 6, 'CHE150-240703', '2024-07-03', '2024-07-18', 80),
(7, 7, 'CRO080-240703', '2024-07-03', '2024-08-03', 150),
(8, 8, 'TOM400-240704', '2024-07-04', '2024-08-04', 60),
(9, 9, 'SMA300-240704', '2024-07-04', '2024-08-04', 70),
(10, 10, 'YAO500-240705', '2024-07-05', '2024-07-15', 200);

-- Stock produits finis
INSERT INTO stock_produit_fini (id, lot_id, quantite) VALUES
(1, 1, 85),   -- Gouda 250g
(2, 2, 65),   -- Gouda 500g
(3, 3, 42),   -- Gouda 1kg
(4, 4, 95),   -- Fromage frais nature
(5, 5, 78),   -- Fromage frais herbes
(6, 6, 68),   -- Chèvre frais
(7, 7, 125),  -- Crottin chèvre
(8, 8, 48),   -- Tome montagne
(9, 9, 58),   -- Spécialité malgache
(10, 10, 180); -- Yaourt fermier

-- =====================================================================
-- DONNÉES CLIENTS ET COMMANDES
-- =====================================================================

-- Clients
INSERT INTO client (id, nom, telephone, adresse) VALUES
(1, 'Restaurant Le Gourmet', '0342345678', '15 Avenue de l''Indépendance, Antananarivo'),
(2, 'Hôtel Colbert', '0342456789', '29 Rue Prince Ratsimamanga, Antananarivo'),
(3, 'Supermarché Score', '0342567890', 'Ankorondrano, Antananarivo'),
(4, 'Épicerie Fine de la Capitale', '0342678901', '42 Analakely, Antananarivo'),
(5, 'Restaurant La Table Française', '0342789012', '8 Avenue Gal Gabriel Ramanantsoa, Antananarivo'),
(6, 'Boulangerie Artisanale Martin', '0342890123', '67 Rue Rainibetsimisaraka, Antananarivo'),
(7, 'Café de la Gare', '0343012345', '12 Place de la Gare, Antananarivo'),
(8, 'Traiteur Excellence', '0342901234', 'Isotry, Antananarivo'),
(9, 'Fromagerie Rivière', '0343123456', 'Centre-ville, Fianarantsoa'),
(10, 'Epicerie du Marché', '0343234567', '42 Analakely, Antananarivo');

-- Commandes
INSERT INTO commande (id, client_id, date_commande, statut, montant_total) VALUES
(1, 1, '2024-06-20', 'livrée', 356000.00),
(2, 2, '2024-06-22', 'livrée', 276000.00),
(3, 3, '2024-06-24', 'confirmée', 534000.00),
(4, 4, '2024-06-25', 'confirmée', 219000.00),
(5, 5, '2024-06-26', 'confirmée', 382000.00),
(6, 8, '2024-06-27', 'confirmée', 426000.00),
(7, 6, '2024-06-28', 'en_preparation', 215000.00),
(8, 7, '2024-06-29', 'en_preparation', 184000.00),
(9, 3, '2024-06-30', 'en_preparation', 900000.00),
(10, 10, '2024-07-01', 'en_preparation', 615000.00),
(11, 1, '2024-07-02', 'en_attente', 280000.00),
(12, 9, '2024-07-03', 'confirmée', 505000.00);

-- Lignes de commande
INSERT INTO ligne_commande (id, commande_id, produit_id, quantite, prix_unitaire) VALUES
-- Commande 1 (Restaurant Le Gourmet)
(1, 1, 2, 5, 28000.00),  -- 5 Gouda 500g
(2, 1, 8, 3, 22000.00),  -- 3 Tome montagne
(3, 1, 1, 10, 15000.00), -- 10 Gouda 250g

-- Commande 2 (Hôtel Colbert)
(4, 2, 3, 2, 52000.00),  -- 2 Gouda 1kg
(5, 2, 5, 8, 9500.00),   -- 8 Fromage herbes
(6, 2, 4, 12, 8000.00),  -- 12 Fromage nature

-- Commande 3 (Supermarché Score)
(7, 3, 1, 15, 15000.00), -- 15 Gouda 250g
(8, 3, 2, 10, 28000.00), -- 10 Gouda 500g
(9, 3, 9, 3, 18000.00),  -- 3 Spécialité malgache

-- Commande 4 (Épicerie Fine)
(10, 4, 9, 4, 18000.00), -- 4 Spécialité malgache
(11, 4, 7, 10, 8500.00), -- 10 Crottin chèvre
(12, 4, 6, 6, 12000.00), -- 6 Chèvre frais

-- Commande 5 (Restaurant La Table Française)
(13, 5, 8, 5, 22000.00), -- 5 Tome montagne
(14, 5, 2, 8, 28000.00), -- 8 Gouda 500g
(15, 5, 6, 4, 12000.00), -- 4 Chèvre frais

-- Commande 6 (Traiteur Excellence)
(16, 6, 3, 3, 52000.00), -- 3 Gouda 1kg
(17, 6, 1, 12, 15000.00), -- 12 Gouda 250g
(18, 6, 9, 5, 18000.00), -- 5 Spécialité malgache

-- Commande 7 (Boulangerie Martin)
(19, 7, 4, 15, 8000.00), -- 15 Fromage nature
(20, 7, 5, 10, 9500.00), -- 10 Fromage herbes

-- Commande 8 (Café de la Gare)
(21, 8, 10, 20, 6000.00), -- 20 Yaourt
(22, 8, 4, 8, 8000.00),   -- 8 Fromage nature

-- Commande 9 (Supermarché Score - grosse commande)
(23, 9, 2, 15, 28000.00), -- 15 Gouda 500g
(24, 9, 1, 20, 15000.00), -- 20 Gouda 250g
(25, 9, 10, 30, 6000.00), -- 30 Yaourt

-- Commande 10 (Epicerie du Marché)
(26, 10, 1, 25, 15000.00), -- 25 Gouda 250g
(27, 10, 4, 20, 8000.00),  -- 20 Fromage nature
(28, 10, 10, 40, 6000.00), -- 40 Yaourt

-- Commande 11 (Restaurant Le Gourmet)
(29, 11, 8, 4, 22000.00), -- 4 Tome montagne
(30, 11, 3, 3, 52000.00), -- 3 Gouda 1kg

-- Commande 12 (Fromagerie Rivière)
(31, 12, 1, 15, 15000.00), -- 15 Gouda 250g
(32, 12, 2, 10, 28000.00), -- 10 Gouda 500g
(33, 12, 9, 3, 18000.00);  -- 3 Spécialité malgache

-- =====================================================================
-- DONNÉES LIVREURS ET LIVRAISONS
-- =====================================================================

-- Livreurs
INSERT INTO livreur (id, nom, telephone) VALUES
(1, 'Rakoto Jean', '0341234567'),
(2, 'Rabe Paul', '0341345678'),
(3, 'Andry Michel', '0341456789'),
(4, 'Hery Patrick', '0341567890'),
(5, 'Nivo Daniel', '0341678901');

-- Livraisons
INSERT INTO livraison (id, commande_id, livreur_id, date_livraison, zone, statut_livraison) VALUES
(1, 1, 2, '2024-06-21', 'Antananarivo Centre', 'LIVREE'),
(2, 2, 2, '2024-06-23', 'Test Zone', 'PLANIFIEE'),
(3, 4, 3, '2024-06-26', 'Analakely', 'PLANIFIEE'),
(4, 5, 1, '2024-06-27', 'Tsaralalana', 'PLANIFIEE'),
(5, 6, 4, '2024-06-28', 'Isotry', 'PLANIFIEE'),
(6, 9, 2, '2024-07-01', 'Fianarantsoa', 'EN_COURS'),
(7, 11, 3, '2024-06-21', 'Centre-ville', 'LIVREE'),
(8, 8, 5, '2024-06-30', '67 Ha', 'LIVREE'),
(9, 3, 4, '2024-06-25', 'Ankorondrano', 'ECHEC'),
(10, 7, 1, '2024-06-29', 'Andravoahangy', 'ANNULEE');

-- Statuts de livraison (historique) - Utilisation d'Instant au lieu de LocalDateTime
INSERT INTO statut_livraison (id, livraison_id, statut, date_statut, commentaire) VALUES
(1, 1, 'PLANIFIEE', '2024-06-20T10:00:00Z', 'Livraison programmée'),
(2, 1, 'EN_COURS', '2024-06-21T08:00:00Z', 'Départ pour livraison'),
(3, 1, 'LIVREE', '2024-06-21T10:30:00Z', 'Livraison réussie'),
(4, 2, 'PLANIFIEE', '2024-06-22T15:00:00Z', 'En attente de livraison'),
(5, 6, 'PLANIFIEE', '2024-06-30T16:00:00Z', 'Programmé pour demain'),
(6, 6, 'EN_COURS', '2024-07-01T09:00:00Z', 'En route vers Fianarantsoa'),
(7, 7, 'PLANIFIEE', '2024-06-20T11:00:00Z', 'Livraison programmée'),
(8, 7, 'EN_COURS', '2024-06-21T09:00:00Z', 'En cours de livraison'),
(9, 7, 'LIVREE', '2024-06-21T11:00:00Z', 'Livré avec succès'),
(10, 8, 'PLANIFIEE', '2024-06-29T14:00:00Z', 'Programmé'),
(11, 8, 'EN_COURS', '2024-06-30T08:30:00Z', 'En route'),
(12, 8, 'LIVREE', '2024-06-30T10:15:00Z', 'Livraison terminée'),
(13, 9, 'PLANIFIEE', '2024-06-24T16:00:00Z', 'Programmé'),
(14, 9, 'EN_COURS', '2024-06-25T08:00:00Z', 'Départ livraison'),
(15, 9, 'ECHEC', '2024-06-25T10:00:00Z', 'Client absent, nouvelle tentative nécessaire'),
(16, 10, 'PLANIFIEE', '2024-06-28T17:00:00Z', 'Programmé'),
(17, 10, 'ANNULEE', '2024-06-29T08:00:00Z', 'Annulé par le client');

-- Produits de livraison
INSERT INTO livraison_produit (id, livraison_id, produit_id, quantite_a_livrer, quantite_livree) VALUES
-- Livraison 1 (Livrée)
(1, 1, 2, 5, 5),   -- Gouda 500g
(2, 1, 8, 3, 3),   -- Tome montagne
(3, 1, 1, 10, 10), -- Gouda 250g

-- Livraison 2 (Planifiée)
(4, 2, 3, 2, 0),   -- Gouda 1kg
(5, 2, 5, 8, 0),   -- Fromage herbes
(6, 2, 4, 12, 0),  -- Fromage nature

-- Livraison 6 (En cours)
(7, 6, 2, 15, 0),  -- Gouda 500g
(8, 6, 1, 20, 0),  -- Gouda 250g
(9, 6, 10, 30, 0), -- Yaourt

-- Livraison 8 (Livrée)
(10, 8, 10, 20, 20), -- Yaourt
(11, 8, 4, 8, 8);    -- Fromage nature

-- Confirmations de réception
INSERT INTO confirmation_reception (id, livraison_id, signature, photo_reception) VALUES
(1, 1, 'Signature_Restaurant_Le_Gourmet_20240621', 'photo_reception_1.jpg'),
(2, 7, 'Signature_Restaurant_Le_Gourmet_20240621_2', 'photo_reception_7.jpg'),
(3, 8, 'Signature_Cafe_Gare_20240630', 'photo_reception_8.jpg');

-- Retours de livraison
INSERT INTO retour_livraison (id, livraison_id, produit_id, quantite_retour, raison) VALUES
(1, 9, 2, 2, 'Date de péremption proche'),
(2, 9, 1, 3, 'Emballage endommagé');

-- =====================================================================
-- DONNÉES PRODUCTION
-- =====================================================================

-- Fiches de production
INSERT INTO fiche_production (id, produit_id, matiere_id, quantite_necessaire) VALUES
-- Gouda 250g
(1, 1, 1, 2.50),   -- 2.5L lait pour 250g Gouda
(2, 1, 3, 5.00),   -- 5g ferments
(3, 1, 4, 2.00),   -- 2ml présure
(4, 1, 5, 3.00),   -- 3g sel

-- Gouda 500g
(5, 2, 1, 5.00),   -- 5L lait pour 500g Gouda
(6, 2, 3, 10.00),  -- 10g ferments
(7, 2, 4, 4.00),   -- 4ml présure
(8, 2, 5, 6.00),   -- 6g sel

-- Fromage frais nature
(9, 4, 1, 1.80),   -- 1.8L lait pour 200g fromage frais
(10, 4, 3, 3.00),  -- 3g ferments

-- Fromage frais herbes
(11, 5, 1, 1.80),  -- 1.8L lait
(12, 5, 3, 3.00),  -- 3g ferments
(13, 5, 6, 2.00),  -- 2g herbes

-- Spécialité malgache
(14, 9, 2, 2.70),  -- 2.7L lait de zébu
(15, 9, 3, 8.00),  -- 8g ferments
(16, 9, 7, 5.00);  -- 5g épices malgaches

-- Productions effectuées
INSERT INTO production_effectuee (id, produit_id, date_production, quantite_produite) VALUES
(1, 1, '2024-07-01', 100), -- Gouda 250g
(2, 2, '2024-07-01', 80),  -- Gouda 500g
(3, 3, '2024-07-01', 50),  -- Gouda 1kg
(4, 4, '2024-07-02', 120), -- Fromage nature
(5, 5, '2024-07-02', 100), -- Fromage herbes
(6, 6, '2024-07-03', 80),  -- Chèvre frais
(7, 7, '2024-07-03', 150), -- Crottin
(8, 8, '2024-07-04', 60),  -- Tome montagne
(9, 9, '2024-07-04', 70),  -- Spécialité malgache
(10, 10, '2024-07-05', 200); -- Yaourt

-- Pertes de production
INSERT INTO perte_production (id, production_id, taux_perte, raison) VALUES
(1, 1, 5.00, 'Fromage trop humide, texture défaillante'),
(2, 2, 3.50, 'Problème de température durant l''affinage'),
(3, 4, 2.00, 'Caillage incomplet'),
(4, 8, 8.00, 'Moisissure non désirée'),
(5, 9, 4.50, 'Goût trop prononcé des épices'),
(6, 10, 1.50, 'Fermentation légèrement excessive');

-- Transformations de déchets
INSERT INTO transformation_dechet (id, matiere_id, produit_final, quantite_transforme, date_transformation) VALUES
(1, 1, 'Petit-lait pour alimentation animale', 15.50, '2024-07-01'),
(2, 1, 'Compost organique', 8.20, '2024-07-02'),
(3, 2, 'Petit-lait de zébu pour porcs', 12.30, '2024-07-04'),
(4, 5, 'Eau salée de nettoyage', 2.50, '2024-07-03'),
(5, 6, 'Tisane aux herbes', 0.85, '2024-07-02');

-- =====================================================================
-- DONNÉES MOUVEMENTS DE STOCK
-- =====================================================================

-- Mouvements stock matières
INSERT INTO mouvement_stock_matiere (id, matiere_id, type_mouvement, quantite, date_mouvement, commentaire) VALUES
(1, 1, 'entree', 300.00, '2024-06-30T08:00:00', 'Livraison fournisseur laiterie'),
(2, 1, 'sortie', 250.00, '2024-07-01T09:00:00', 'Production Gouda'),
(3, 2, 'entree', 150.00, '2024-07-01T10:00:00', 'Livraison lait de zébu'),
(4, 2, 'sortie', 18.90, '2024-07-04T11:00:00', 'Production spécialité malgache'),
(5, 3, 'entree', 1000.00, '2024-06-29T14:00:00', 'Commande ferments lactiques'),
(6, 3, 'sortie', 180.00, '2024-07-01T09:30:00', 'Productions diverses'),
(7, 4, 'sortie', 120.00, '2024-07-01T10:00:00', 'Production fromages pressés'),
(8, 5, 'sortie', 45.00, '2024-07-01T11:00:00', 'Salage fromages'),
(9, 6, 'sortie', 200.00, '2024-07-02T14:00:00', 'Fromage aux herbes'),
(10, 7, 'sortie', 350.00, '2024-07-04T15:00:00', 'Spécialité malgache');

-- Mouvements stock produits
INSERT INTO mouvement_stock_produit (id, lot_id, type_mouvement, quantite, date_mouvement, commentaire) VALUES
(1, 1, 'entree', 100, '2024-07-01T15:00:00', 'Production terminée'),
(2, 1, 'sortie', 15, '2024-07-02T10:00:00', 'Vente Restaurant Le Gourmet'),
(3, 2, 'entree', 80, '2024-07-01T16:00:00', 'Production terminée'),
(4, 2, 'sortie', 15, '2024-07-02T11:00:00', 'Vente Supermarché Score'),
(5, 4, 'entree', 120, '2024-07-02T17:00:00', 'Production terminée'),
(6, 4, 'sortie', 25, '2024-07-03T09:00:00', 'Vente multiple'),
(7, 10, 'entree', 200, '2024-07-05T18:00:00', 'Production terminée'),
(8, 10, 'sortie', 20, '2024-07-06T08:00:00', 'Livraison Café de la Gare'),
(9, 7, 'entree', 150, '2024-07-03T17:30:00', 'Production terminée'),
(10, 7, 'sortie', 25, '2024-07-04T10:30:00', 'Commandes diverses');

-- Alertes de péremption
INSERT INTO alerte_peremption (id, matiere_id, date_peremption, seuil_alerte) VALUES
(1, 1, '2024-07-08', 2),   -- Lait de vache expire dans 2 jours
(2, 2, '2024-07-09', 2),   -- Lait de zébu expire dans 3 jours
(3, 3, '2024-08-15', 7),   -- Ferments expire dans 1 mois
(4, 6, '2024-09-30', 15),  -- Herbes expirent dans 2 mois
(5, 7, '2024-10-15', 30);  -- Épices expirent dans 3 mois

-- Simulations de production
INSERT INTO simulation_production (id, produit_id, quantite_suggeree, date_simulation) VALUES
(1, 1, 150, '2024-07-06T09:00:00'),  -- Suggestion Gouda 250g
(2, 2, 120, '2024-07-06T09:15:00'),  -- Suggestion Gouda 500g
(3, 4, 200, '2024-07-06T09:30:00'),  -- Suggestion Fromage nature
(4, 10, 300, '2024-07-06T09:45:00'), -- Suggestion Yaourt
(5, 9, 100, '2024-07-06T10:00:00');  -- Suggestion Spécialité malgache

-- =====================================================================
-- DONNÉES COMPTABILITÉ
-- =====================================================================

-- Paiements
INSERT INTO paiement (id, commande_id, date_paiement, montant, methode) VALUES
(1, 1, '2024-06-21', 356000.00, 'virement bancaire'),
(2, 2, '2024-06-23', 276000.00, 'espèces'),
(3, 8, '2024-06-30', 184000.00, 'mobile money'),
(4, 3, '2024-06-25', 267000.00, 'virement bancaire'), -- Paiement partiel
(5, 7, '2024-06-29', 215000.00, 'carte bancaire'),
(6, 12, '2024-07-03', 505000.00, 'virement bancaire');

-- Factures
INSERT INTO facture (id, commande_id, montant_total, date_facture) VALUES
(1, 1, 356000.00, '2024-06-21'),
(2, 2, 276000.00, '2024-06-23'),
(3, 3, 534000.00, '2024-06-24'),
(4, 4, 219000.00, '2024-06-25'),
(5, 5, 382000.00, '2024-06-26'),
(6, 6, 426000.00, '2024-06-27'),
(7, 7, 215000.00, '2024-06-28'),
(8, 8, 184000.00, '2024-06-29'),
(9, 12, 505000.00, '2024-07-03');

-- Revenus
INSERT INTO revenu (id, commande_id, montant, date_revenu) VALUES
(1, 1, 356000.00, '2024-06-21'),
(2, 2, 276000.00, '2024-06-23'),
(3, 8, 184000.00, '2024-06-30'),
(4, 3, 267000.00, '2024-06-25'), -- Paiement partiel
(5, 7, 215000.00, '2024-06-29'),
(6, 12, 505000.00, '2024-07-03');

-- Dépenses
INSERT INTO depense (id, libelle, montant, date_depense, categorie) VALUES
(1, 'Achat lait fournisseur Laiterie Tana', 180000.00, '2024-06-30', 'Matières premières'),
(2, 'Électricité chambre froide', 45000.00, '2024-07-01', 'Énergie'),
(3, 'Salaire équipe production', 320000.00, '2024-07-01', 'Personnel'),
(4, 'Carburant véhicules livraison', 85000.00, '2024-07-01', 'Transport'),
(5, 'Maintenance équipement fromagerie', 125000.00, '2024-07-02', 'Maintenance'),
(6, 'Emballages et étiquettes', 35000.00, '2024-07-03', 'Conditionnement'),
(7, 'Achat ferments lactiques', 75000.00, '2024-06-29', 'Matières premières'),
(8, 'Assurance véhicules', 28000.00, '2024-07-01', 'Assurances'),
(9, 'Nettoyage et désinfection', 22000.00, '2024-07-04', 'Hygiène'),
(10, 'Frais vétérinaire contrôle', 40000.00, '2024-07-05', 'Contrôle qualité');

-- Bilan financier
INSERT INTO bilan_financier (id, periode, total_depenses, total_revenus, profit) VALUES
(1, '2024-06-01', 852000.00, 1803000.00, 951000.00),
(2, '2024-07-01', 955000.00, 1298000.00, 343000.00);

-- =====================================================================
-- DONNÉES PROMOTIONS
-- =====================================================================

-- Promotions
INSERT INTO promotion (id, nom, description, reduction_pourcentage, date_debut, date_fin) VALUES
(1, 'Promotion d''été', 'Réduction sur les fromages frais pour l''été', 15.00, '2024-07-01', '2024-08-31'),
(2, 'Dégustation Gouda', 'Promotion spéciale sur la gamme Gouda artisanal', 10.00, '2024-07-15', '2024-07-31'),
(3, 'Spécialités malgaches', 'Mise en avant des produits locaux', 20.00, '2024-06-26', '2024-07-26'),
(4, 'Fromages de chèvre', 'Promotion sur les fromages de chèvre', 12.50, '2024-08-01', '2024-08-15'),
(5, 'Rentrée fromagère', 'Offre spéciale pour la rentrée', 8.00, '2024-09-01', '2024-09-30');

-- =====================================================================
-- DONNÉES STATISTIQUES
-- =====================================================================

-- Statistiques production
INSERT INTO statistique_production (id, produit_id, periode, quantite_produite) VALUES
(1, 1, '2024-06-01', 280), -- Gouda 250g en juin
(2, 2, '2024-06-01', 220), -- Gouda 500g en juin
(3, 3, '2024-06-01', 120), -- Gouda 1kg en juin
(4, 4, '2024-06-01', 350), -- Fromage nature en juin
(5, 5, '2024-06-01', 280), -- Fromage herbes en juin
(6, 10, '2024-06-01', 450), -- Yaourt en juin
(7, 1, '2024-07-01', 100), -- Gouda 250g en juillet (début)
(8, 2, '2024-07-01', 80),  -- Gouda 500g en juillet (début)
(9, 4, '2024-07-01', 120), -- Fromage nature en juillet (début)
(10, 10, '2024-07-01', 200); -- Yaourt en juillet (début)

-- Statistiques ventes
INSERT INTO statistique_vente (id, produit_id, periode, quantite_vendue, revenu_total) VALUES
(1, 1, '2024-06-01', 65, 975000.00),   -- Gouda 250g juin
(2, 2, '2024-06-01', 48, 1344000.00),  -- Gouda 500g juin
(3, 3, '2024-06-01', 15, 780000.00),   -- Gouda 1kg juin
(4, 4, '2024-06-01', 85, 680000.00),   -- Fromage nature juin
(5, 5, '2024-06-01', 42, 399000.00),   -- Fromage herbes juin
(6, 10, '2024-06-01', 120, 720000.00), -- Yaourt juin
(7, 1, '2024-07-01', 50, 750000.00),   -- Gouda 250g juillet (début)
(8, 2, '2024-07-01', 33, 924000.00),   -- Gouda 500g juillet (début)
(9, 8, '2024-07-01', 17, 374000.00),   -- Tome montagne juillet (début)
(10, 10, '2024-07-01', 70, 420000.00); -- Yaourt juillet (début)

-- Statistiques stock
INSERT INTO statistique_stock (id, produit_id, date_enregistrement, quantite) VALUES
(1, 1, '2024-07-01', 100), -- Stock Gouda 250g au 1er juillet
(2, 2, '2024-07-01', 80),  -- Stock Gouda 500g au 1er juillet
(3, 3, '2024-07-01', 50),  -- Stock Gouda 1kg au 1er juillet
(4, 4, '2024-07-02', 120), -- Stock Fromage nature au 2 juillet
(5, 5, '2024-07-02', 100), -- Stock Fromage herbes au 2 juillet
(6, 10, '2024-07-05', 200), -- Stock Yaourt au 5 juillet
(7, 1, '2024-07-06', 85),  -- Stock actuel Gouda 250g
(8, 2, '2024-07-06', 65),  -- Stock actuel Gouda 500g
(9, 4, '2024-07-06', 95),  -- Stock actuel Fromage nature
(10, 10, '2024-07-06', 180); -- Stock actuel Yaourt

-- Rapports personnalisés
INSERT INTO rapport_personnalise (id, nom, contenu, date_creation) VALUES
(1, 'Rapport mensuel juin 2024', 'Synthèse des ventes et production du mois de juin 2024. Production totale: 1700 unités. Chiffre d''affaires: 4.898.000 Ar. Taux de perte: 3.8%.', '2024-07-01T16:00:00'),
(2, 'Analyse produits phares', 'Les Gouda artisanaux représentent 45% du CA. Le yaourt fermier connaît une forte demande. Recommandation: augmenter production Gouda 500g.', '2024-07-03T10:30:00'),
(3, 'Suivi qualité semaine 27', 'Contrôle qualité satisfaisant. 2 lots avec défauts mineurs. pH et texture conformes aux standards. Prochaine formation équipe prévue.', '2024-07-05T14:45:00'),
(4, 'Performance livraisons', 'Taux de réussite livraisons: 85%. 2 échecs dus à l''absence client. Zone Antananarivo la plus desservie. Délai moyen: 1.2 jour.', '2024-07-06T09:15:00'),
(5, 'Prévisions production août', 'Estimation besoins août: +20% Gouda, +15% fromages frais, +30% yaourt. Stock matières premières suffisant pour 3 semaines.', '2024-07-06T11:00:00');

-- Réactiver les contraintes
SET session_replication_role = 'origin';

-- =====================================================================
-- VÉRIFICATION DES DONNÉES INSÉRÉES
-- =====================================================================

-- Requêtes de vérification
SELECT 'Nombre de clients:' as info, COUNT(*)::text as total FROM client
UNION ALL
SELECT 'Nombre de produits:', COUNT(*)::text FROM produit
UNION ALL
SELECT 'Nombre de commandes:', COUNT(*)::text FROM commande
UNION ALL
SELECT 'Nombre de livraisons:', COUNT(*)::text FROM livraison
UNION ALL
SELECT 'Nombre de livreurs:', COUNT(*)::text FROM livreur
UNION ALL
SELECT 'Chiffre d''affaires total:', COALESCE(SUM(montant_total), 0)::text || ' Ar' FROM commande WHERE statut IN ('livrée', 'confirmée')
UNION ALL
SELECT 'Stock total produits:', COALESCE(SUM(quantite), 0)::text || ' unités' FROM stock_produit_fini
UNION ALL
SELECT 'Productions effectuées:', COALESCE(SUM(quantite_produite), 0)::text || ' unités' FROM production_effectuee;

-- =====================================================================
-- SCRIPT TERMINÉ
-- =====================================================================
-- Toutes les données de test ont été insérées avec succès.
-- Le script respecte les contraintes de clés étrangères et les relations.
-- Données cohérentes pour un environnement de test complet.
-- =====================================================================
