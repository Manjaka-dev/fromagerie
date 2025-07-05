INSERT INTO categorie_produit (nom) VALUES 
('Biscuits'), ('Chocolats'), ('Confitures'), ('Patisseries'), ('Viennoiseries');

INSERT INTO produit (nom, poids, prix_vente, prix_revient, ingredients, allergenes, date_peremption, categorie_id) VALUES 
('Biscuit amande', 150.00, 3.50, 2.00, 'Farine, sucre, amandes, beurre, œufs', 'Gluten, fruits a coque, lait, œufs', '2023-12-31', 1),
('Chocolat noir 70%', 100.00, 4.20, 2.80, 'Cacao, sucre, beurre de cacao', 'Lait (traces)', '2024-06-30', 2),
('Confiture fraise', 250.00, 2.80, 1.50, 'Fraises, sucre, pectine', 'Aucun', '2024-03-15', 3),
('Croissant', 80.00, 1.20, 0.60, 'Farine, beurre, eau, sucre, levure, sel', 'Gluten, lait', '2023-11-20', 5),
('Tarte aux pommes', 500.00, 8.50, 5.00, 'Pommes, farine, beurre, sucre, œufs', 'Gluten, lait, œufs', '2023-11-25', 4);

INSERT INTO lot_produit (produit_id, numero_lot, date_fabrication, date_peremption, quantite) VALUES 
(5, 'PAT-2023-005', '2023-11-18', '2023-11-25', 100);
(1, 'BIS-2023-001', '2023-10-01', '2023-12-31', 500),
(2, 'CHO-2023-002', '2023-09-15', '2024-06-30', 300),
(3, 'CON-2023-003', '2023-10-10', '2024-03-15', 200),
(4, 'VIE-2023-004', '2023-11-15', '2023-11-20', 150),


INSERT INTO client (nom, telephone, adresse) VALUES 
('Boulangerie du Centre', '0123456789', '10 Rue Principale, 75000 Paris'),
('epicerie Fine', '0987654321', '25 Avenue des Champs, 69000 Lyon'),
('Hôtel Bellevue', '0567891234', '5 Boulevard de la Mer, 13000 Marseille');

INSERT INTO commande (client_id, date_commande, statut) VALUES 
(1, '2023-11-01', 'Livree'),
(2, '2023-11-05', 'En preparation'),
(3, '2023-11-10', 'En attente');

INSERT INTO ligne_commande (commande_id, produit_id, quantite, prix_unitaire) VALUES 
(1, 1, 50, 3.50),
(1, 2, 20, 4.20),
(2, 3, 30, 2.80),
(2, 4, 100, 1.20),
(3, 5, 10, 8.50);

INSERT INTO facture (commande_id, montant_total, date_facture) VALUES 
(1, 259.00, '2023-11-02'),
(2, 186.00, '2023-11-06');

INSERT INTO paiement (commande_id, date_paiement, montant, methode) VALUES 
(1, '2023-11-03', 259.00, 'Virement bancaire'),
(2, '2023-11-07', 186.00, 'Carte bancaire');

INSERT INTO livreur (nom, telephone) VALUES 
('Jean Dupont', '0612345678'),
('Marie Martin', '0698765432');

INSERT INTO livraison (commande_id, livreur_id, date_livraison, zone) VALUES 
(1, 1, '2023-11-03', 'Paris centre'),
(2, 2, '2023-11-08', 'Lyon peripherie');

INSERT INTO statut_livraison (livraison_id, statut) VALUES 
(1, 'Livre'),
(2, 'En cours');