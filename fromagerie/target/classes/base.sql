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
    quantite INT
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

CREATE TABLE promotion (
    id serial PRIMARY KEY ,
    id_produit INT REFERENCES produit(id),
    poorcentage INTEGER,
    raison TEXT

);




