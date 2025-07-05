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

CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    telephone VARCHAR(20),
    adresse TEXT
);

CREATE TABLE paiement (
    id SERIAL PRIMARY KEY,
    commande_id INT REFERENCES commande(id),
    date_paiement DATE,
    montant DECIMAL(10,2),
    methode VARCHAR(50)
);

select SUM(lc.quantite * pr.prix_vente) prix_total 
from ligne_commande lc 
join produit pr on lc.produit_id = pr.id
group by pr.id;

select pr.nom, t.prix_total, lc.commande_id
from produit pr 
join ligne_commande lc on lc.produit_id = pr.id
join (select pr.id as id, SUM(lc.quantite * pr.prix_vente) prix_total 
from ligne_commande lc 
join produit pr on lc.produit_id = pr.id
group by pr.id) t on t.id = pr.id;


select SUM(t.prix_total) total, lc.commande_id
from produit pr 
join ligne_commande lc on lc.produit_id = pr.id
join (select pr.id as id, SUM(lc.quantite * pr.prix_vente) prix_total 
from ligne_commande lc 
join produit pr on lc.produit_id = pr.id
group by pr.id) t on t.id = pr.id
group by lc.commande_id


-- liste commande avec produit commande sy prix total produit commandee
select pr.nom, t.prix_total, lc.commande_id, zb.total
from produit pr 
join ligne_commande lc on lc.produit_id = pr.id
join (select pr.id as id, SUM(lc.quantite * pr.prix_vente) prix_total 
from ligne_commande lc 
join produit pr on lc.produit_id = pr.id
group by pr.id) t on t.id = pr.id
join (select SUM(t.prix_total) total, lc.commande_id id
from produit pr 
join ligne_commande lc on lc.produit_id = pr.id
join (select pr.id as id, SUM(lc.quantite * pr.prix_vente) prix_total 
from ligne_commande lc 
join produit pr on lc.produit_id = pr.id
group by pr.id) t on t.id = pr.id
group by lc.commande_id) zb on zb.id = lc.commande_id;


SELECT 
    pr.nom, 
    SUM(lc.quantite * pr.prix_vente) AS prix_total_produit,
    lc.commande_id,
    SUM(SUM(lc.quantite * pr.prix_vente)) OVER (PARTITION BY lc.commande_id) AS total_commande
FROM 
    produit pr 
JOIN 
    ligne_commande lc ON lc.produit_id = pr.id
GROUP BY 
    pr.nom, lc.commande_id;

-- 
select SUM(t.prix_total) total, lc.commande_id id_commande
from produit pr 
join ligne_commande lc on lc.produit_id = pr.id
join (select pr.id as id, SUM(lc.quantite * pr.prix_vente) prix_total 
from ligne_commande lc 
join produit pr on lc.produit_id = pr.id
group by pr.id) t on t.id = pr.id
group by lc.commande_id;


