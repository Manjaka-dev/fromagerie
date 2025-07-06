-- Initialisation des rôles
INSERT INTO role (nom) VALUES 
('ADMIN'),
('MANAGER'),
('EMPLOYE'),
('LIVREUR')
ON CONFLICT DO NOTHING;

-- Initialisation des utilisateurs de test
-- Note: En production, les mots de passe doivent être hashés avec BCrypt
INSERT INTO utilisateur (nom, email, mot_de_passe, role_id) VALUES 
('Administrateur', 'admin@fromagerie.com', 'admin123', (SELECT id FROM role WHERE nom = 'ADMIN')),
('Manager Production', 'manager@fromagerie.com', 'manager123', (SELECT id FROM role WHERE nom = 'MANAGER')),
('Employé Stock', 'employe@fromagerie.com', 'employe123', (SELECT id FROM role WHERE nom = 'EMPLOYE')),
('Livreur Jean', 'livreur@fromagerie.com', 'livreur123', (SELECT id FROM role WHERE nom = 'LIVREUR'))
ON CONFLICT DO NOTHING;

-- Ajouter la colonne montant_total à la table commande si elle n'existe pas
ALTER TABLE commande ADD COLUMN IF NOT EXISTS montant_total DECIMAL(10,2) DEFAULT 0.00;

-- Données de test pour les clients
INSERT INTO client (nom, telephone, adresse) VALUES 
('Fromagerie Martin', '0341234567', '123 Rue de la Fromagerie, Antananarivo'),
('Épicerie Bio Lyon', '0342345678', '456 Avenue des Épices, Lyon'),
('Restaurant Le Gourmet', '0343456789', '789 Boulevard Gastronomique, Paris'),
('Marché de Belleville', '0344567890', '321 Place du Marché, Belleville'),
('Hôtel Luxury', '0345678901', '654 Rue du Luxe, Antananarivo')
ON DUPLICATE KEY UPDATE nom = VALUES(nom);

-- Données de test pour les commandes
INSERT INTO commande (client_id, date_commande, statut, montant_total) VALUES 
(1, '2025-01-15', 'confirmée', 150000.00),
(2, '2025-01-16', 'en_attente', 85000.00),
(3, '2025-01-17', 'livrée', 200000.00),
(4, '2025-01-18', 'annulée', 0.00),
(5, '2025-01-19', 'confirmée', 125000.00),
(1, '2025-01-20', 'en_attente', 95000.00),
(2, '2025-01-21', 'livrée', 175000.00)
ON DUPLICATE KEY UPDATE statut = VALUES(statut); 