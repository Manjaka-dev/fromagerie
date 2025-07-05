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