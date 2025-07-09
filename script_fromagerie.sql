
-- ===========================
-- SCRIPT SQL FROMAGERIE 
-- Synchronisé avec les entités Java
-- ===========================

-- ===========================
-- 1. PRODUITS & CATÉGORIES
-- ===========================
CREATE TABLE categorie_produit (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE produit (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    poids DECIMAL(5,2),
    prix_vente DECIMAL(10,2),
    prix_revient DECIMAL(10,2),
    ingredients TEXT,
    allergenes TEXT,
    date_peremption DATE,
    categorie_id BIGINT REFERENCES categorie_produit(id)
);

CREATE TABLE lot_produit (
    id BIGSERIAL PRIMARY KEY,
    produit_id BIGINT REFERENCES produit(id),
    numero_lot VARCHAR(50),
    date_fabrication DATE,
    date_peremption DATE,
    quantite INTEGER
);

-- =============================
-- 2. STOCK PRODUITS FINIS
-- =============================
CREATE TABLE stock_produit_fini (
    id BIGSERIAL PRIMARY KEY,
    lot_id BIGINT REFERENCES lot_produit(id),
    quantite INTEGER
);

CREATE TABLE mouvement_stock_produit (
    id BIGSERIAL PRIMARY KEY,
    lot_id BIGINT REFERENCES lot_produit(id),
    type_mouvement VARCHAR(20), -- entree, sortie, ajustement
    quantite INTEGER,
    date_mouvement TIMESTAMP DEFAULT NOW(),
    commentaire TEXT
);

CREATE TABLE simulation_production (
    id BIGSERIAL PRIMARY KEY,
    produit_id BIGINT REFERENCES produit(id),
    quantite_suggeree INTEGER,
    date_simulation TIMESTAMP DEFAULT NOW()
);

-- =============================
-- 3. STOCK MATIÈRES PREMIÈRES
-- =============================
CREATE TABLE matiere_premiere (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100),
    unite VARCHAR(20),
    duree_conservation INTEGER -- en jours
);

CREATE TABLE stock_matiere (
    id BIGSERIAL PRIMARY KEY,
    matiere_id BIGINT REFERENCES matiere_premiere(id),
    quantite DECIMAL(10,2)
);

CREATE TABLE mouvement_stock_matiere (
    id BIGSERIAL PRIMARY KEY,
    matiere_id BIGINT REFERENCES matiere_premiere(id),
    type_mouvement VARCHAR(20),
    quantite DECIMAL(10,2),
    date_mouvement TIMESTAMP DEFAULT NOW(),
    commentaire TEXT
);

CREATE TABLE alerte_peremption (
    id BIGSERIAL PRIMARY KEY,
    matiere_id BIGINT REFERENCES matiere_premiere(id),
    date_peremption DATE,
    seuil_alerte INTEGER
);

-- =============================
-- 4. PRODUCTION
-- =============================
CREATE TABLE fiche_production (
    id BIGSERIAL PRIMARY KEY,
    produit_id BIGINT REFERENCES produit(id),
    matiere_id BIGINT REFERENCES matiere_premiere(id),
    quantite_necessaire DECIMAL(10,2)
);

CREATE TABLE production_effectuee (
    id BIGSERIAL PRIMARY KEY,
    produit_id BIGINT REFERENCES produit(id),
    date_production DATE,
    quantite_produite INTEGER
);

CREATE TABLE perte_production (
    id BIGSERIAL PRIMARY KEY,
    production_id BIGINT REFERENCES production_effectuee(id),
    taux_perte DECIMAL(5,2),
    raison TEXT
);

CREATE TABLE transformation_dechet (
    id BIGSERIAL PRIMARY KEY,
    matiere_id BIGINT REFERENCES matiere_premiere(id),
    produit_final VARCHAR(100),
    quantite_transforme DECIMAL(10,2),
    date_transformation DATE
);

-- =============================
-- 5. VENTES & COMMANDES
-- =============================
CREATE TABLE client (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100),
    telephone VARCHAR(20),
    adresse TEXT
);

CREATE TABLE commande (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT REFERENCES client(id),
    date_commande DATE,
    statut VARCHAR(30), -- en_attente, confirmée, annulée, livrée
    montant_total DECIMAL(10,2)
);

CREATE TABLE ligne_commande (
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT REFERENCES commande(id),
    produit_id BIGINT REFERENCES produit(id),
    quantite INTEGER,
    prix_unitaire DECIMAL(10,2)
);

CREATE TABLE facture (
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT REFERENCES commande(id),
    montant_total DECIMAL(10,2),
    date_facture DATE
);

CREATE TABLE paiement (
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT REFERENCES commande(id),
    date_paiement DATE,
    montant DECIMAL(10,2),
    methode VARCHAR(50)
);

CREATE TABLE promotion (
    id SERIAL PRIMARY KEY,
    produit_id BIGINT REFERENCES produit(id),
    nom VARCHAR(100),
    description TEXT,
    reduction_pourcentage DECIMAL(5,2),
    date_debut DATE,
    date_fin DATE
);

CREATE TABLE promotion_commande(
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT REFERENCES commande(id),
    promotion_id INTEGER REFERENCES promotion(id)
);

-- =============================
-- 6. LIVRAISON
-- =============================
CREATE TABLE livreur (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100),
    telephone VARCHAR(20)
);

-- Énumération pour les statuts de livraison
CREATE TYPE statut_livraison_enum AS ENUM ('PLANIFIEE', 'EN_COURS', 'LIVREE', 'ANNULEE');

CREATE TABLE livraison (
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT REFERENCES commande(id),
    livreur_id BIGINT REFERENCES livreur(id),
    date_livraison DATE,
    zone VARCHAR(100),
    statut_livraison statut_livraison_enum DEFAULT 'PLANIFIEE'
);

CREATE TABLE livraison_produit (
    id BIGSERIAL PRIMARY KEY,
    livraison_id BIGINT REFERENCES livraison(id),
    produit_id BIGINT REFERENCES produit(id),
    quantite_a_livrer INTEGER,
    quantite_livree INTEGER DEFAULT 0
);

CREATE TABLE statut_livraison (
    id BIGSERIAL PRIMARY KEY,
    livraison_id BIGINT REFERENCES livraison(id),
    statut VARCHAR(30),
    date_statut TIMESTAMP DEFAULT NOW()
);

CREATE TABLE confirmation_reception (
    id BIGSERIAL PRIMARY KEY,
    livraison_id BIGINT REFERENCES livraison(id),
    signature TEXT,
    photo_reception TEXT
);

CREATE TABLE retour_livraison (
    id BIGSERIAL PRIMARY KEY,
    livraison_id BIGINT REFERENCES livraison(id),
    produit_id BIGINT REFERENCES produit(id),
    quantite_retour INTEGER,
    raison TEXT
);

-- =============================
-- 7. UTILISATEURS & RÔLES
-- =============================
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(50)
);

CREATE TABLE utilisateur (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    mot_de_passe TEXT,
    role_id BIGINT REFERENCES role(id)
);

CREATE TABLE token_reset (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT REFERENCES utilisateur(id),
    token VARCHAR(255) UNIQUE,
    date_expiration TIMESTAMP,
    utilise BOOLEAN DEFAULT FALSE,
    date_creation TIMESTAMP DEFAULT NOW()
);

CREATE TABLE journal_connexion (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT REFERENCES utilisateur(id),
    date_connexion TIMESTAMP DEFAULT NOW(),
    action TEXT
);

-- =============================
-- 8. STATISTIQUES & RAPPORTS
-- =============================
CREATE TABLE statistique_vente (
    id BIGSERIAL PRIMARY KEY,
    produit_id BIGINT REFERENCES produit(id),
    periode DATE,
    quantite_vendue INTEGER,
    revenu_total DECIMAL(10,2)
);

CREATE TABLE statistique_production (
    id BIGSERIAL PRIMARY KEY,
    produit_id BIGINT REFERENCES produit(id),
    periode DATE,
    quantite_produite INTEGER
);

CREATE TABLE statistique_stock (
    id BIGSERIAL PRIMARY KEY,
    produit_id BIGINT REFERENCES produit(id),
    date_enregistrement DATE,
    quantite INTEGER
);

CREATE TABLE rapport_personnalise (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100),
    contenu TEXT,
    date_creation TIMESTAMP DEFAULT NOW()
);

-- =============================
-- 9. COMPTABILITÉ
-- =============================
CREATE TABLE depense (
    id BIGSERIAL PRIMARY KEY,
    libelle VARCHAR(100),
    montant DECIMAL(10,2),
    date_depense DATE,
    categorie VARCHAR(100)
);

CREATE TABLE revenu (
    id BIGSERIAL PRIMARY KEY,
    commande_id BIGINT REFERENCES commande(id),
    montant DECIMAL(10,2),
    date_revenu DATE
);

CREATE TABLE bilan_financier (
    id BIGSERIAL PRIMARY KEY,
    periode DATE,
    total_depenses DECIMAL(10,2),
    total_revenus DECIMAL(10,2),
    profit DECIMAL(10,2)
);

-- =============================
-- DONNÉES D'EXEMPLE
-- =============================
INSERT INTO promotion (produit_id, nom, description, reduction_pourcentage, date_debut, date_fin) 
VALUES (1, 'Promotion Été', 'Réduction estivale', 10.00, '2025-07-05', '2025-07-15');

INSERT INTO promotion (produit_id, nom, description, reduction_pourcentage, date_debut, date_fin) 
VALUES (2, 'Promotion Rentrée', 'Réduction de rentrée', 10.00, '2025-08-05', '2025-08-15');

