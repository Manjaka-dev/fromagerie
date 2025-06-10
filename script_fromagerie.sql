
-- ===========================
-- 1. PRODUITS & CATÉGORIES
-- ===========================
CREATE TABLE categorie_produit (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE produit (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    poids DECIMAL(5,2),
    prix_vente DECIMAL(10,2),
    prix_revient DECIMAL(10,2),
    ingredients TEXT,
    allergenes TEXT,
    date_peremption DATE,
    categorie_id INT REFERENCES categorie_produit(id)
);

CREATE TABLE lot_produit (
    id SERIAL PRIMARY KEY,
    produit_id INT REFERENCES produit(id),
    numero_lot VARCHAR(50),
    date_fabrication DATE,
    date_peremption DATE,
    quantite INT
);

-- =============================
-- 2. STOCK PRODUITS FINIS
-- =============================
CREATE TABLE stock_produit_fini (
    id SERIAL PRIMARY KEY,
    lot_id INT REFERENCES lot_produit(id),
    quantite INT
);

CREATE TABLE mouvement_stock_produit (
    id SERIAL PRIMARY KEY,
    lot_id INT REFERENCES lot_produit(id),
    type_mouvement VARCHAR(20), -- entree, sortie, ajustement
    quantite INT,
    date_mouvement TIMESTAMP DEFAULT NOW(),
    commentaire TEXT
);

CREATE TABLE simulation_production (
    id SERIAL PRIMARY KEY,
    produit_id INT REFERENCES produit(id),
    quantite_suggeree INT,
    date_simulation TIMESTAMP DEFAULT NOW()
);

-- =============================
-- 3. STOCK MATIÈRES PREMIÈRES
-- =============================
CREATE TABLE matiere_premiere (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    unite VARCHAR(20),
    duree_conservation INT -- en jours
);

CREATE TABLE stock_matiere (
    id SERIAL PRIMARY KEY,
    matiere_id INT REFERENCES matiere_premiere(id),
    quantite DECIMAL(10,2)
);

CREATE TABLE mouvement_stock_matiere (
    id SERIAL PRIMARY KEY,
    matiere_id INT REFERENCES matiere_premiere(id),
    type_mouvement VARCHAR(20),
    quantite DECIMAL(10,2),
    date_mouvement TIMESTAMP DEFAULT NOW(),
    commentaire TEXT
);

CREATE TABLE alerte_peremption (
    id SERIAL PRIMARY KEY,
    matiere_id INT REFERENCES matiere_premiere(id),
    date_peremption DATE,
    seuil_alerte INT
);

-- =============================
-- 4. PRODUCTION
-- =============================
CREATE TABLE fiche_production (
    id SERIAL PRIMARY KEY,
    produit_id INT REFERENCES produit(id),
    matiere_id INT REFERENCES matiere_premiere(id),
    quantite_necessaire DECIMAL(10,2)
);

CREATE TABLE production_effectuee (
    id SERIAL PRIMARY KEY,
    produit_id INT REFERENCES produit(id),
    date_production DATE,
    quantite_produite INT
);

CREATE TABLE perte_production (
    id SERIAL PRIMARY KEY,
    production_id INT REFERENCES production_effectuee(id),
    taux_perte DECIMAL(5,2),
    raison TEXT
);

CREATE TABLE transformation_dechet (
    id SERIAL PRIMARY KEY,
    matiere_id INT REFERENCES matiere_premiere(id),
    produit_final VARCHAR(100),
    quantite_transforme DECIMAL(10,2),
    date_transformation DATE
);

-- =============================
-- 5. VENTES & COMMANDES
-- =============================
CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    telephone VARCHAR(20),
    adresse TEXT
);

CREATE TABLE commande (
    id SERIAL PRIMARY KEY,
    client_id INT REFERENCES client(id),
    date_commande DATE,
    statut VARCHAR(30) -- en_attente, confirmée, annulée, livrée
);

CREATE TABLE ligne_commande (
    id SERIAL PRIMARY KEY,
    commande_id INT REFERENCES commande(id),
    produit_id INT REFERENCES produit(id),
    quantite INT,
    prix_unitaire DECIMAL(10,2)
);

CREATE TABLE facture (
    id SERIAL PRIMARY KEY,
    commande_id INT REFERENCES commande(id),
    montant_total DECIMAL(10,2),
    date_facture DATE
);

CREATE TABLE paiement (
    id SERIAL PRIMARY KEY,
    commande_id INT REFERENCES commande(id),
    date_paiement DATE,
    montant DECIMAL(10,2),
    methode VARCHAR(50)
);

CREATE TABLE promotion (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    description TEXT,
    reduction_pourcentage DECIMAL(5,2),
    date_debut DATE,
    date_fin DATE
);

-- =============================
-- 6. LIVRAISON
-- =============================
CREATE TABLE livreur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    telephone VARCHAR(20)
);

CREATE TABLE livraison (
    id SERIAL PRIMARY KEY,
    commande_id INT REFERENCES commande(id),
    livreur_id INT REFERENCES livreur(id),
    date_livraison DATE,
    zone VARCHAR(100)
);

CREATE TABLE statut_livraison (
    id SERIAL PRIMARY KEY,
    livraison_id INT REFERENCES livraison(id),
    statut VARCHAR(30),
    date_statut TIMESTAMP DEFAULT NOW()
);

CREATE TABLE confirmation_reception (
    id SERIAL PRIMARY KEY,
    livraison_id INT REFERENCES livraison(id),
    signature TEXT,
    photo_reception TEXT
);

CREATE TABLE retour_livraison (
    id SERIAL PRIMARY KEY,
    livraison_id INT REFERENCES livraison(id),
    produit_id INT REFERENCES produit(id),
    quantite_retour INT,
    raison TEXT
);

-- =============================
-- 7. UTILISATEURS & RÔLES
-- =============================
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50)
);

CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    mot_de_passe TEXT,
    role_id INT REFERENCES role(id)
);

CREATE TABLE journal_connexion (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT REFERENCES utilisateur(id),
    date_connexion TIMESTAMP DEFAULT NOW(),
    action TEXT
);

-- =============================
-- 8. STATISTIQUES & RAPPORTS
-- =============================
CREATE TABLE statistique_vente (
    id SERIAL PRIMARY KEY,
    produit_id INT REFERENCES produit(id),
    periode DATE,
    quantite_vendue INT,
    revenu_total DECIMAL(10,2)
);

CREATE TABLE statistique_production (
    id SERIAL PRIMARY KEY,
    produit_id INT REFERENCES produit(id),
    periode DATE,
    quantite_produite INT
);

CREATE TABLE statistique_stock (
    id SERIAL PRIMARY KEY,
    produit_id INT REFERENCES produit(id),
    date_enregistrement DATE,
    quantite INT
);

CREATE TABLE rapport_personnalise (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    contenu TEXT,
    date_creation TIMESTAMP DEFAULT NOW()
);

-- =============================
-- 9. COMPTABILITÉ
-- =============================
CREATE TABLE depense (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100),
    montant DECIMAL(10,2),
    date_depense DATE,
    categorie VARCHAR(100)
);

CREATE TABLE revenu (
    id SERIAL PRIMARY KEY,
    commande_id INT REFERENCES commande(id),
    montant DECIMAL(10,2),
    date_revenu DATE
);

CREATE TABLE bilan_financier (
    id SERIAL PRIMARY KEY,
    periode DATE,
    total_depenses DECIMAL(10,2),
    total_revenus DECIMAL(10,2),
    profit DECIMAL(10,2)
);
