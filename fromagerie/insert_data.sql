-- Insertion des rôles
INSERT INTO role (nom) VALUES 
('ADMIN'),
('MANAGER'),
('EMPLOYE'),
('LIVREUR')
ON CONFLICT DO NOTHING;

-- Insertion des utilisateurs de test
-- Note: En production, les mots de passe doivent être hashés avec BCrypt
INSERT INTO utilisateur (nom, email, mot_de_passe, role_id) VALUES 
('Administrateur Principal', 'admin@fromagerie.com', 'admin123', (SELECT id FROM role WHERE nom = 'ADMIN')),
('Manager Production', 'manager@fromagerie.com', 'manager123', (SELECT id FROM role WHERE nom = 'MANAGER')),
('Employé Stock', 'employe@fromagerie.com', 'employe123', (SELECT id FROM role WHERE nom = 'EMPLOYE')),
('Livreur Jean', 'livreur@fromagerie.com', 'livreur123', (SELECT id FROM role WHERE nom = 'LIVREUR')),
('Marie Dupont', 'marie.dupont@fromagerie.com', 'marie123', (SELECT id FROM role WHERE nom = 'EMPLOYE')),
('Pierre Martin', 'pierre.martin@fromagerie.com', 'pierre123', (SELECT id FROM role WHERE nom = 'MANAGER')),
('Sophie Bernard', 'sophie.bernard@fromagerie.com', 'sophie123', (SELECT id FROM role WHERE nom = 'LIVREUR')),
('Lucas Petit', 'lucas.petit@fromagerie.com', 'lucas123', (SELECT id FROM role WHERE nom = 'EMPLOYE'))
ON CONFLICT DO NOTHING;

-- Affichage des données insérées
SELECT 'Rôles créés:' as info;
SELECT id, nom FROM role ORDER BY id;

SELECT 'Utilisateurs créés:' as info;
SELECT u.id, u.nom, u.email, r.nom as role 
FROM utilisateur u 
LEFT JOIN role r ON u.role_id = r.id 
ORDER BY u.id; 