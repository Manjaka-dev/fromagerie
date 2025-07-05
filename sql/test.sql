Table "livraison" {
  "id" SERIAL [pk, increment]
  "commande_id" INT
  "livreur_id" INT
  "date_livraison" DATE
  "zone" VARCHAR(100)
}

Table "statut_livraison" {
  "id" SERIAL [pk, increment]
  "livraison_id" INT
  "statut" VARCHAR(30)
  "date_statut" TIMESTAMP [default: `NOW()`]
}

Table "confirmation_reception" {
  "id" SERIAL [pk, increment]
  "livraison_id" INT
}

Table "retour_livraison" {
  "id" SERIAL [pk, increment]
  "livraison_id" INT
  "produit_id" INT
  "quantite_retour" INT
  "raison" TEXT
}