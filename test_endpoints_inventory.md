# Inventaire des Endpoints REST - Fromagerie API

## 1. AuthController - /api/auth
- POST /api/auth/register - Inscription
- POST /api/auth/login - Connexion
- POST /api/auth/logout - Déconnexion
- GET /api/auth/verify - Vérifier token
- GET /api/auth/me - Infos utilisateur connecté
- POST /api/auth/forgot-password - Demande réinitialisation
- POST /api/auth/reset-password - Réinitialiser mot de passe
- POST /api/auth/change-password - Changer mot de passe
- POST /api/auth/refresh-token - Rafraîchir token
- GET /api/auth/permissions - Permissions utilisateur

## 2. ClientController - /api/clients
- GET /api/clients - Tous les clients
- GET /api/clients/{id} - Client par ID
- POST /api/clients - Créer client
- PUT /api/clients/{id} - Modifier client
- DELETE /api/clients/{id} - Supprimer client

## 3. CommandeController - /api/commandes
- GET /api/commandes - Toutes les commandes
- GET /api/commandes/{id} - Commande par ID
- POST /api/commandes/create - Créer commande
- POST /api/commandes/{clientId} - Créer commande pour client
- POST /api/commandes/save - Sauvegarder commande
- PUT /api/commandes/{id} - Modifier commande
- DELETE /api/commandes/{id} - Supprimer commande
- GET /api/commandes/livreurs - Liste livreurs
- POST /api/commandes/{commandeId}/livraison - Créer livraison

## 4. ProduitController - /api/produits
- GET /api/produits - Tous les produits
- GET /api/produits/{id} - Produit par ID
- POST /api/produits - Créer produit
- PUT /api/produits/{id} - Modifier produit
- DELETE /api/produits/{id} - Supprimer produit
- GET /api/produits/search - Recherche par nom
- GET /api/produits/categories - Toutes les catégories
- GET /api/produits/stats - Statistiques produits
- GET /api/produits/stock-faible - Produits stock faible
- POST /api/produits/{id}/ajuster-stock - Ajuster stock

## 5. LivraisonController - /api/livraisons
- GET /api/livraisons/commandes - Commandes pour livraison
- GET /api/livraisons/commandes/{id}/form - Formulaire livraison
- POST /api/livraisons/commandes/{commandeId} - Commande par ID
- GET /api/livraisons/livraison - Formulaire livraison
- POST /api/livraisons/livraison - Créer livraison
- POST /api/livraisons/assign-livreur - Assigner livreur
- GET /api/livraisons - Liste livraisons
- POST /api/livraisons/{livraisonId}/statut - Ajouter statut
- GET /api/livraisons/{livraisonId}/confirmation-paiement - Confirmation paiement
- POST /api/livraisons/{livraisonId}/confirmer-livraison-paiement - Confirmer livraison et paiement
- POST /api/livraisons/retour-livraison - Retour livraison
- GET /api/livraisons/commandes/{id}/export-pdf - Export PDF
- PUT /api/livraisons/{id} - Modifier livraison
- DELETE /api/livraisons/{id} - Annuler livraison
- GET /api/livraisons/zones - Zones de livraison
- GET /api/livraisons/{id} - Livraison par ID
- POST /api/livraisons/debug/fix-sequence - Debug séquence

## 6. LivreurController - /api/livreurs
- GET /api/livreurs - Tous les livreurs
- GET /api/livreurs/{id} - Livreur par ID
- POST /api/livreurs - Créer livreur
- PUT /api/livreurs/{id} - Modifier livreur
- DELETE /api/livreurs/{id} - Supprimer livreur

## 7. LivraisonProduitController - /api/livraisons-produits
- GET /api/livraisons-produits/livraison/{livraisonId} - Produits d'une livraison
- PUT /api/livraisons-produits/{id}/quantite-livree - Modifier quantité livrée

## 8. PaiementController - /api/paiements
- POST /api/paiements - Créer paiement
- GET /api/paiements/commande/{commandeId} - Paiements par commande
- GET /api/paiements - Tous les paiements

## 9. RetourLivraisonController - /api/retours-livraison
- GET /api/retours-livraison - Tous les retours
- GET /api/retours-livraison/{id} - Retour par ID
- POST /api/retours-livraison - Créer retour
- PUT /api/retours-livraison/{id} - Modifier retour
- DELETE /api/retours-livraison/{id} - Supprimer retour

## 10. StockMatiereController - /api/stock
- GET /api/stock/matieres-premieres - Toutes les matières premières
- POST /api/stock/matieres-premieres - Créer matière première
- PUT /api/stock/matieres-premieres/{id} - Modifier matière première
- DELETE /api/stock/matieres-premieres/{id} - Supprimer matière première
- POST /api/stock/mouvements - Créer mouvement stock
- GET /api/stock/mouvements - Mouvements stock
- GET /api/stock/alertes-stock - Alertes stock
- POST /api/stock/dechets - Déclarer déchet
- GET /api/stock/dechets - Liste déchets
- POST /api/stock/alertes-peremption - Créer alerte péremption
- GET /api/stock/alertes-peremption - Liste alertes péremption
- GET /api/stock/statuts - Statuts stock
- GET /api/stock/types-mouvement - Types mouvement

## 11. UtilisateurController - /api/utilisateurs
- GET /api/utilisateurs - Tous les utilisateurs
- GET /api/utilisateurs/{id} - Utilisateur par ID
- POST /api/utilisateurs - Créer utilisateur
- PUT /api/utilisateurs/{id} - Modifier utilisateur
- DELETE /api/utilisateurs/{id} - Supprimer utilisateur
- POST /api/utilisateurs/{id}/change-password - Changer mot de passe
- GET /api/utilisateurs/roles - Tous les rôles

## 12. RevenuController - /api/revenus
- GET /api/revenus/commande/{commandeId} - Revenu par commande
- GET /api/revenus/date-range - Revenus par période
- GET /api/revenus/total - Total revenus période
- GET /api/revenus/total/date - Total revenus date
- POST /api/revenus - Créer revenu
- GET /api/revenus/{id} - Revenu par ID

## 13. DepenseController - /api/depenses
- POST /api/depenses - Créer dépense
- GET /api/depenses/{id} - Dépense par ID

## 14. TransformationDechetController - /api/transformations
- GET /api/transformations/matiere/{matiereId} - Transformations par matière
- GET /api/transformations/date-range - Transformations par période
- GET /api/transformations/produit-final - Transformations par produit final
- GET /api/transformations/total/{matiereId} - Total transformé par matière
- POST /api/transformations - Créer transformation

## 15. HelloController (non-REST)
- Contrôleur de test/debug
