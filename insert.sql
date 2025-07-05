-- Exemple pour table livreur
INSERT INTO livreur (id, nom) VALUES (1, 'Jean Dupont'), (2, 'Sitraka Andrianina');

-- Exemple pour table commande
INSERT INTO commande (id, date_commande) VALUES (1, '2025-07-01'), (2, '2025-07-03'), (3, '2025-07-04');

-- Livraison 1
INSERT INTO livraison (
  date_livraison,
  statut_livraison,
  zone,
  commande_id,
  livreur_id
)
VALUES (
  '2025-07-05',
  'PLANIFIEE',
  'Ankorondrano',
  1,
  1
);

-- Livraison 2
INSERT INTO livraison (
  date_livraison,
  statut_livraison,
  zone,
  commande_id,
  livreur_id
)
VALUES (
  '2025-07-05',
  'EN_COURS',
  'Andavamamba',
  2,
  2
);

-- Livraison 3
INSERT INTO livraison (
  date_livraison,
  statut_livraison,
  zone,
  commande_id,
  livreur_id
)
VALUES (
  '2025-07-06',
  'LIVREE',
  'Anosibe',
  3,
  1
);
