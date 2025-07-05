
INSERT INTO categorie_produit (id, nom) VALUES
(1, 'Biscuits'),
(2, 'Chocolats'),
(3, 'Confitures'),
(4, 'Patisseries'),
(5, 'Viennoiseries');


INSERT INTO produit (nom, poids, prix_vente, prix_revient, ingredients, allergenes, date_peremption, categorie_id) VALUES 
('Biscuit amande', 150.00, 3.50, 2.00, 'Farine, sucre, amandes, beurre, oeufs', 'Gluten, fruits a coque, lait, oeufs', '2023-12-31', 1),
('Chocolat noir 70%', 100.00, 4.20, 2.80, 'Cacao, sucre, beurre de cacao', 'Lait (traces)', '2024-06-30', 2),
('Confiture fraise', 250.00, 2.80, 1.50, 'Fraises, sucre, pectine', 'Aucun', '2024-03-15', 3),
('Tarte aux pommes', 500.00, 8.50, 5.00, 'Pommes, farine, beurre, sucre, oeufs', 'Gluten, lait, oeufs', '2023-11-25', 4),
('Croissant', 80.00, 1.20, 0.60, 'Farine, beurre, eau, sucre, levure, sel', 'Gluten, lait', '2023-11-20', 5);


INSERT INTO client (nom, telephone, adresse) VALUES 
('Boulangerie du Centre', '0123456789', '10 Rue Principale, 75000 Paris'),
('epicerie Fine', '0987654321', '25 Avenue des Champs, 69000 Lyon'),
('Hôtel Bellevue', '0567891234', '5 Boulevard de la Mer, 13000 Marseille');

INSERT INTO commande (client_id, date_commande, statut) VALUES 
(1, '2023-11-01', 'Livree'),
(2, '2023-11-05', 'En preparation'),
(3, '2023-11-10', 'En attente');


INSERT INTO ligne_commande (commande_id, produit_id, quantite) VALUES 
(1, 1, 50),
(1, 2, 20),
(2, 3, 30),
(2, 4, 100),
(3, 5, 10);


INSERT INTO livreur (nom, telephone) VALUES 
('Jean Dupont', '0612345678'),
('Marie Martin', '0698765432');

INSERT INTO livraison (commande_id, livreur_id, date_livraison, zone) VALUES 
(1, 1, '2023-11-03', 'Paris centre'),
(2, 2, '2023-11-08', 'Lyon peripherie');


INSERT INTO statut_livraison (livraison_id, statut, date_statut) VALUES 
(1, 'Livree', '2023-11-03'),
(7, 'En cours', '2025-06-09');

-- Desactiver temporairement les contraintes de cle etrangère
-- Désactiver temporairement les contraintes de clé étrangère
ALTER TABLE statut_livraison DROP CONSTRAINT IF EXISTS statut_livraison_livraison_id_fkey;
ALTER TABLE livraison DROP CONSTRAINT IF EXISTS livraison_commande_id_fkey;
ALTER TABLE ligne_commande DROP CONSTRAINT IF EXISTS ligne_commande_commande_id_fkey;
ALTER TABLE ligne_commande DROP CONSTRAINT IF EXISTS ligne_commande_produit_id_fkey;

-- Supprimer les données des tables et réinitialiser les IDs
TRUNCATE TABLE statut_livraison RESTART IDENTITY CASCADE;
TRUNCATE TABLE livraison RESTART IDENTITY CASCADE;
TRUNCATE TABLE ligne_commande RESTART IDENTITY CASCADE;
TRUNCATE TABLE commande RESTART IDENTITY CASCADE;
TRUNCATE TABLE client RESTART IDENTITY CASCADE;
TRUNCATE TABLE produit RESTART IDENTITY CASCADE;
TRUNCATE TABLE categorie_produit RESTART IDENTITY CASCADE;
TRUNCATE TABLE livreur RESTART IDENTITY CASCADE;

-- Réactiver les contraintes de clé étrangère
ALTER TABLE statut_livraison ADD CONSTRAINT statut_livraison_livraison_id_fkey FOREIGN KEY (livraison_id) REFERENCES livraison (id);
ALTER TABLE livraison ADD CONSTRAINT livraison_commande_id_fkey FOREIGN KEY (commande_id) REFERENCES commande (id);
ALTER TABLE ligne_commande ADD CONSTRAINT ligne_commande_commande_id_fkey FOREIGN KEY (commande_id) REFERENCES commande (id);
ALTER TABLE ligne_commande ADD CONSTRAINT ligne_commande_produit_id_fkey FOREIGN KEY (produit_id) REFERENCES produit (id);





--STRING_AGG est une fonction d'agrégation qui concatène les valeurs d'une colonne en 
--une seule chaîne, séparées par un délimiteur spécifié.

--LISTE COMMANDE LAST INSERT ID PAR STATUT LIVRAISON

        SELECT
            l.id AS livraisonId,
            sl.statut AS statutLivraison,
            l.zone AS zone,
            SUM(lc.quantite * p.prix_vente) AS montantTotal,
            liv.id AS livreurId,
            liv.nom AS livreurNom,
            cl.nom AS clientNom,
            cl.telephone AS clientTelephone,
            STRING_AGG(p.nom || ' (x' || lc.quantite || ')', ', ') AS produitsCommandes
        FROM livraison l
        LEFT JOIN commande c ON l.commande_id = c.id
        LEFT JOIN client cl ON c.client_id = cl.id
        LEFT JOIN ligne_commande lc ON lc.commande_id = c.id
        LEFT JOIN produit p ON p.id = lc.produit_id
        LEFT JOIN livreur liv ON liv.id = l.livreur_id
        LEFT JOIN statut_livraison sl ON sl.id = (
            SELECT sl2.id
            FROM statut_livraison sl2
            WHERE sl2.livraison_id = l.id
            ORDER BY sl2.date_statut DESC
            LIMIT 1
        )
        GROUP BY l.id, sl.statut, l.zone, liv.id, liv.nom, cl.nom, cl.telephone
        ORDER BY l.id DESC


