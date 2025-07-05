-- ========================
-- Tables de référence
-- ========================
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

CREATE TABLE stock_produit_fini (
    id SERIAL PRIMARY KEY,
    lot_id INT REFERENCES lot_produit(id),
    quantite INT
);

CREATE TABLE mouvement_stock_produit (
    id SERIAL PRIMARY KEY,
    lot_id INT REFERENCES lot_produit(id),
    type_mouvement VARCHAR(20),
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

-- ========================
-- Matières premières
-- ========================
CREATE TABLE matiere_premiere (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    unite VARCHAR(20),
    duree_conservation INT
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

-- ========================
-- Production
-- ========================
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

-- ========================
-- Commande & client
-- ========================
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
    statut VARCHAR(30)
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


-- ========================
-- Livraison
-- ========================
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

