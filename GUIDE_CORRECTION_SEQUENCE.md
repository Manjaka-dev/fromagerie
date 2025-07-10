# Guide pour corriger les problèmes de séquence PostgreSQL

## Introduction
Ce guide vous aide à résoudre les problèmes de séquence PostgreSQL qui causent des erreurs de type "duplicate key value violates unique constraint" lors de la création de nouvelles entités.

## Problème
L'erreur se produit lorsque la séquence PostgreSQL utilisée pour générer automatiquement les IDs n'est pas synchronisée avec les valeurs existantes dans la table. Cela provoque des tentatives d'insertion avec des IDs déjà utilisés.

## Solution 1: Exécution directe via script shell
Le script `fix_commande_sequence_direct.sh` vous permet d'exécuter directement la correction SQL.

### Prérequis
- PostgreSQL installé sur la machine ou accessible via réseau
- Accès à la base de données avec des privilèges suffisants

### Utilisation
1. Modifiez les variables dans le script pour correspondre à votre environnement:
   ```bash
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=fromagerie
   DB_USER=postgres
   DB_PASSWORD=postgres
   ```
   
2. Exécutez le script:
   ```bash
   ./fix_commande_sequence_direct.sh
   ```

## Solution 2: Exécution manuelle via pgAdmin ou psql
Le fichier `fix_commande_sequence.sql` contient les commandes SQL à exécuter.

### Utilisation avec psql
```bash
psql -h localhost -p 5432 -d fromagerie -U postgres -f fix_commande_sequence.sql
```

### Utilisation avec pgAdmin
1. Ouvrez pgAdmin
2. Connectez-vous à votre serveur PostgreSQL
3. Ouvrez l'éditeur de requêtes SQL
4. Chargez le fichier `fix_commande_sequence.sql` ou copiez son contenu
5. Exécutez la requête

## Vérification
Après exécution de l'une des solutions, vérifiez que:
1. La commande SQL a renvoyé un nombre (la valeur maximale actuelle de l'ID)
2. Vous pouvez maintenant créer de nouvelles commandes sans erreur

## Résolution des problèmes courants
- **Erreur d'authentification**: Vérifiez les identifiants de connexion à la base de données
- **Relation non trouvée**: Vérifiez que le nom de la séquence est correct (commande_id_seq)
- **Permission refusée**: Assurez-vous que l'utilisateur a les droits suffisants
