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

CREATE TABLE livreur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    telephone VARCHAR(20)
);

CREATE TABLE livraison (
    id SERIAL PRIMARY KEY,
    commande_id INT REFERENCES commande(id),
    livreur_id INT REFERENCES livreur(id),
    date_livraison DATE
);



-- Formulaire planification livraison
select cl.nom, pr.nom, lc.quantite, c.date_commande, liv.date_livraison, liv.zone, liv.livreur_id 
from ligne_commande lc 
join commande c  on lc.commande_id = c.id
join produit pr on pr.id = lc.produit_id
join client cl on cl.id = c.client_id
join livraison liv on liv.commande_id = c.id;

select cl.nom, c.date_commande, liv.date_livraison, liv.zone 
from ligne_commande lc 
join commande c  on lc.commande_id = c.id
join produit pr on pr.id = lc.produit_id
join client cl on cl.id = c.client_id
join livraison liv on liv.commande_id = c.id
group by c.id, cl.nom, liv.date_livraison, liv.zone;



select c.id, cl.nom, c.date_commande
from commande c 
join client cl on cl.id = c.client_id
group by c.id;



-- insert statut_livraison planifier

select liv.id, cl.nom, liv.date_livraison, pr.nom, lc.quantite, liv.zone, st.id
from ligne_commande lc 
join commande c  on lc.commande_id = c.id
join produit pr on pr.id = lc.produit_id
join client cl on cl.id = c.client_id
join livraison liv on liv.commande_id = c.id
join statut_livraison st on st.livraison_id = liv.id
where st.statut = 'Livre';

-- insert statut_livraison en cours

select * from livreur;

-- ticket
