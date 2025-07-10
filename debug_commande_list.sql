-- Cette correction permet de vérifier si les commandes sont retournées par le backend
-- pour résoudre le problème d'affichage de la liste des commandes

-- Création d'un script SQL qui vérifie les commandes
SELECT id, client_id, date_commande, statut, montant_total 
FROM commande;

-- Vérification des commandes sans livraison
SELECT c.id, c.date_commande, c.statut, c.client_id 
FROM commande c 
LEFT JOIN livraison l ON c.id = l.commande_id
WHERE l.id IS NULL;
