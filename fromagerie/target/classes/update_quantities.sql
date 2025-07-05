-- Script pour mettre à jour les quantités des produits
-- Mise à jour des dates de péremption pour qu'elles soient dans le futur
UPDATE lot_produit SET date_peremption = '2025-12-31' WHERE produit_id = 1;
UPDATE lot_produit SET date_peremption = '2025-12-31' WHERE produit_id = 2;
UPDATE lot_produit SET date_peremption = '2025-12-31' WHERE produit_id = 3;
UPDATE lot_produit SET date_peremption = '2025-12-31' WHERE produit_id = 4;
UPDATE lot_produit SET date_peremption = '2025-12-31' WHERE produit_id = 5;

-- Mise à jour des quantités (si elles sont nulles ou 0)
UPDATE lot_produit SET quantite = 500 WHERE produit_id = 1 AND (quantite IS NULL OR quantite = 0);
UPDATE lot_produit SET quantite = 300 WHERE produit_id = 2 AND (quantite IS NULL OR quantite = 0);
UPDATE lot_produit SET quantite = 200 WHERE produit_id = 3 AND (quantite IS NULL OR quantite = 0);
UPDATE lot_produit SET quantite = 150 WHERE produit_id = 4 AND (quantite IS NULL OR quantite = 0);
UPDATE lot_produit SET quantite = 100 WHERE produit_id = 5 AND (quantite IS NULL OR quantite = 0);

-- Insérer de nouveaux lots si aucun n'existe
INSERT INTO lot_produit (produit_id, numero_lot, date_fabrication, date_peremption, quantite)
SELECT 1, 'BIS-2025-001', '2025-01-01', '2025-12-31', 500
WHERE NOT EXISTS (SELECT 1 FROM lot_produit WHERE produit_id = 1);

INSERT INTO lot_produit (produit_id, numero_lot, date_fabrication, date_peremption, quantite)
SELECT 2, 'CHO-2025-001', '2025-01-01', '2025-12-31', 300
WHERE NOT EXISTS (SELECT 1 FROM lot_produit WHERE produit_id = 2);

INSERT INTO lot_produit (produit_id, numero_lot, date_fabrication, date_peremption, quantite)
SELECT 3, 'CON-2025-001', '2025-01-01', '2025-12-31', 200
WHERE NOT EXISTS (SELECT 1 FROM lot_produit WHERE produit_id = 3);

INSERT INTO lot_produit (produit_id, numero_lot, date_fabrication, date_peremption, quantite)
SELECT 4, 'VIE-2025-001', '2025-01-01', '2025-12-31', 150
WHERE NOT EXISTS (SELECT 1 FROM lot_produit WHERE produit_id = 4);

INSERT INTO lot_produit (produit_id, numero_lot, date_fabrication, date_peremption, quantite)
SELECT 5, 'PAT-2025-001', '2025-01-01', '2025-12-31', 100
WHERE NOT EXISTS (SELECT 1 FROM lot_produit WHERE produit_id = 5); 