CREATE TABLE IF NOT EXISTS public.livraison
(
    id integer NOT NULL DEFAULT nextval('livraison_id_seq'::regclass),
    commande_id integer,
    livreur_id integer,
    date_livraison date,
    zone character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT livraison_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.ligne_commande
(
    id integer NOT NULL DEFAULT nextval('ligne_commande_id_seq'::regclass),
    commande_id integer,
    produit_id integer,
    quantite integer,
    prix_unitaire numeric(10, 2),
    CONSTRAINT ligne_commande_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.commande
(
    id integer NOT NULL DEFAULT nextval('commande_id_seq'::regclass),
    client_id integer,
    date_commande date,
    statut character varying(30) COLLATE pg_catalog."default",
    CONSTRAINT commande_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.client
(
    id integer NOT NULL DEFAULT nextval('client_id_seq'::regclass),
    nom character varying(100) COLLATE pg_catalog."default",
    telephone character varying(20) COLLATE pg_catalog."default",
    adresse text COLLATE pg_catalog."default",
    CONSTRAINT client_pkey PRIMARY KEY (id)
    );


SELECT quantite FROM lot_produit WHERE produit_id = ? AND date_peremption > CURRENT_DATE;




