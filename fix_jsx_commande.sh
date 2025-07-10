#!/bin/bash

# Script pour corriger l'erreur JSX dans Commande.jsx
# Cette approche réécrit le fichier entier pour éviter les problèmes de caractères invisibles

# Chemin vers le fichier
FILE_PATH="/Users/mac/Documents/L2/Gestion_proj/fromagerie_front/src/pages/commande/Commande.jsx"
BACKUP_PATH="/Users/mac/Documents/L2/Gestion_proj/fromagerie_front/src/pages/commande/Commande.jsx.bak"

# Créer une sauvegarde
cp "$FILE_PATH" "$BACKUP_PATH"
echo "Sauvegarde créée: $BACKUP_PATH"

# Extraire le contenu du fichier
content=$(cat "$FILE_PATH")

# Trouver la partie problématique (autour de la ligne 950-960)
# Cette approche recherche le bloc contenant la date de livraison et le réécrit

# Créer un fichier temporaire pour la correction
TMP_FILE=$(mktemp)

# Écrire le contenu modifié dans le fichier temporaire
cat > "$TMP_FILE" << 'EOF'
import React, { useState, useEffect } from 'react';
import { Link, NavLink } from 'react-router-dom';
import {
  ShoppingCart,
  Filter,
  X,
  Search,
  Calendar,
  ChevronDown,
  Clock,
  Bell,
  User,
  Check,
  Package,
  Truck,
  AlertTriangle,
  MoreHorizontal,
  BarChart3,
  TrendingUp,
  Calculator,
  Factory,
  Settings,
  CheckCircle, Plus, Wallet, ListChecks, ArrowDown, Loader, Trash2
} from 'lucide-react';

import styles from './../../assets/styles/commande/Commande.module.css';
import SidebarMenu from "../../components/SidebarMenu";
import { FaTag } from 'react-icons/fa';
import { commandeAPI, clientAPI, produitAPI, livreurAPI, livraisonAPI, formatCurrency } from '../../services/api';

const CommandesPage = () => {
  const [searchClient, setSearchClient] = useState('');
  
  // États pour les données du backend
  const [commandes, setCommandes] = useState([]);
  const [clients, setClients] = useState([]);
  const [produits, setProduits] = useState([]);
  const [livreurs, setLivreurs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // États pour les filtres
  const [filters, setFilters] = useState({
    dateDebut: '',
    dateFin: '',
    statut: 'tous',
    client: 'tous'
  });

  // État pour la modale de création d'une commande
  const [showAddModal, setShowAddModal] = useState(false);
  const [selectedProduits, setSelectedProduits] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [showLivraisonModal, setShowLivraisonModal] = useState(false);
  
  // État pour le formulaire de livraison
  const [livraisonForm, setLivraisonForm] = useState({
    livreurId: '',
    zone: '',
    dateLivraison: ''
  });

  // État pour la nouvelle commande
  const [newOrder, setNewOrder] = useState({
    clientId: '',
    date: new Date().toISOString().split('T')[0],
    produits: []
  });
  
  useEffect(() => {
    loadData();
  }, []);

  // Fonction pour formater les dates
  const formatDate = (dateString) => {
    if (!dateString) return 'Non spécifiée';
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR');
  };

  // Fonction pour calculer le total de la nouvelle commande
  const calculateOrderTotal = () => {
    let total = 0;
    newOrder.produits.forEach(produitData => {
      const produit = produits.find(p => p.id === produitData.produitId);
      if (produit) {
        total += (produit.prixVente || 0) * produitData.quantite;
      }
    });
    return total;
  };

  // Fonction pour charger les données
  const loadData = async () => {
    setLoading(true);
    try {
      // Chargement des commandes
      const commandesData = await commandeAPI.getAllCommandes();
      
      // Log pour le débogage
      console.log("Données brutes des commandes:", commandesData);
      
      // Gérer différents formats de réponse potentiels
      let processedCommandes = [];
      if (Array.isArray(commandesData)) {
        processedCommandes = commandesData;
      } else if (commandesData && Array.isArray(commandesData.commandes)) {
        processedCommandes = commandesData.commandes;
      } else if (commandesData && typeof commandesData === 'object') {
        processedCommandes = [commandesData];
      }
      
      console.log("Commandes traitées:", processedCommandes);
      setCommandes(processedCommandes || []);
      
      // Chargement des clients
      const clientsData = await clientAPI.getAllClients();
      console.log("Clients:", clientsData);
      setClients(clientsData || []);
      
      // Chargement des produits
      const produitsData = await produitAPI.getAllProduits();
      console.log("Produits:", produitsData);
      setProduits(produitsData || []);
      
      // Chargement des livreurs
      const livreursData = await livreurAPI.getAllLivreurs();
      console.log("Livreurs:", livreursData);
      setLivreurs(livreursData || []);
      
    } catch (error) {
      console.error("Erreur lors du chargement des données:", error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour filtrer les commandes
  const getFilteredCommandes = () => {
    return commandes.filter(commande => {
      // Filtre par client
      if (filters.client !== 'tous' && commande.client?.id !== parseInt(filters.client)) {
        return false;
      }
      
      // Filtre par statut
      if (filters.statut !== 'tous') {
        switch (filters.statut) {
          case 'en_attente':
            if (commande.statutLivraison !== 'EN_ATTENTE') return false;
            break;
          case 'en_cours':
            if (commande.statutLivraison !== 'EN_COURS') return false;
            break;
          case 'livree':
            if (commande.statutLivraison !== 'LIVREE') return false;
            break;
          default:
            break;
        }
      }
      
      // Filtre par date
      if (filters.dateDebut && new Date(commande.dateCommande) < new Date(filters.dateDebut)) {
        return false;
      }
      if (filters.dateFin && new Date(commande.dateCommande) > new Date(filters.dateFin)) {
        return false;
      }
      
      // Filtre par recherche client
      if (searchClient) {
        const clientName = commande.client?.nom?.toLowerCase() || '';
        if (!clientName.includes(searchClient.toLowerCase())) {
          return false;
        }
      }
      
      return true;
    });
  };

  // Fonction pour ouvrir le modal de détails
  const handleShowDetails = (commande) => {
    setSelectedOrder(commande);
    setShowDetailsModal(true);
  };
  
  // Fonction pour ajouter un produit à la commande
  const addProduitToOrder = () => {
    if (selectedProduits.length === 0) {
      alert('Veuillez sélectionner au moins un produit');
      return;
    }
    
    const newProduits = [...newOrder.produits];
    
    selectedProduits.forEach(produit => {
      // Vérifier si le produit est déjà dans la commande
      const existingIndex = newProduits.findIndex(p => p.produitId === produit.id);
      
      if (existingIndex >= 0) {
        // Mettre à jour la quantité si le produit existe déjà
        newProduits[existingIndex].quantite += parseInt(produit.quantite) || 1;
      } else {
        // Ajouter le nouveau produit à la commande
        newProduits.push({
          produitId: parseInt(produit.id),
          quantite: parseInt(produit.quantite) || 1
        });
      }
    });
    
    setNewOrder({ ...newOrder, produits: newProduits });
    setSelectedProduits([]);
  };
  
  // Fonction pour retirer un produit de la commande
  const removeProduitFromOrder = (index) => {
    const newProduits = [...newOrder.produits];
    newProduits.splice(index, 1);
    setNewOrder({ ...newOrder, produits: newProduits });
  };

  // Fonction pour créer une nouvelle commande
  const handleAddOrder = async () => {
    try {
      // Vérifier que tous les champs requis sont remplis
      if (!newOrder.clientId) {
        alert('Veuillez sélectionner un client');
        return;
      }
      
      if (newOrder.produits.length === 0) {
        alert('Veuillez ajouter au moins un produit à la commande');
        return;
      }
      
      // Préparation des données à envoyer
      const commandeData = {
        clientId: parseInt(newOrder.clientId),
        dateCommande: newOrder.date,
        ligneCommandes: newOrder.produits.map(p => ({
          produitId: parseInt(p.produitId),
          quantite: parseInt(p.quantite)
        }))
      };
      
      console.log("Données de la commande à envoyer:", commandeData);
      
      // Envoi de la requête
      const result = await commandeAPI.createCommande(commandeData);
      console.log("Résultat de la création:", result);
      
      // Recharger les données
      await loadData();
      
      // Réinitialiser le formulaire
      setShowAddModal(false);
      setNewOrder({
        clientId: '',
        date: new Date().toISOString().split('T')[0],
        produits: []
      });
      setSelectedProduits([]);
    } catch (error) {
      console.error('Erreur lors de la création de la commande:', error);
      
      // Afficher des détails supplémentaires pour le débogage
      let errorMessage = error.message;
      if (error.response) {
        try {
          errorMessage += ' - Détails: ' + JSON.stringify(error.response.data);
        } catch (e) {
          errorMessage += ' - Impossible de lire les détails de l\'erreur';
        }
      }
      
      alert('Erreur lors de la création de la commande: ' + errorMessage);
    }
  };

  const handleCancelOrder = async (orderId) => {
    if (window.confirm('Êtes-vous sûr de vouloir annuler cette commande ?')) {
      try {
        await commandeAPI.deleteCommande(orderId);
        await loadData();
      } catch (error) {
        console.error('Erreur lors de l\'annulation:', error);
        alert('Erreur lors de l\'annulation de la commande');
      }
    }
  };

  const handleDeleteOrder = async (orderId) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette commande ?')) {
      try {
        await commandeAPI.deleteCommande(orderId);
        await loadData();
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        alert('Erreur lors de la suppression de la commande');
      }
    }
  };

  // Fonction pour ouvrir le formulaire de livraison
  const handleOpenLivraisonModal = (commande) => {
    setSelectedOrder(commande);
    setLivraisonForm({
      livreurId: '',
      zone: '',
      dateLivraison: ''
    });
    setShowLivraisonModal(true);
  };

  // Fonction pour créer une livraison
  const handleCreateLivraison = async () => {
    try {
      if (!livraisonForm.livreurId || !livraisonForm.zone || !livraisonForm.dateLivraison) {
        alert('Veuillez remplir tous les champs');
        return;
      }

      await livraisonAPI.createLivraison(
        selectedOrder.id,
        livraisonForm.livreurId,
        livraisonForm.zone,
        livraisonForm.dateLivraison
      );
      
      alert('Livraison configurée avec succès !');
      
      // Recharger les données pour mettre à jour la liste des commandes
      await loadData();
      
      setShowLivraisonModal(false);
      setSelectedOrder(null);
      setLivraisonForm({
        livreurId: '',
        zone: '',
        dateLivraison: ''
      });
    } catch (error) {
      console.error('Erreur lors de la création de la livraison:', error);
      if (error.message.includes('400')) {
        alert('Erreur: Impossible de créer la livraison. Veuillez réessayer.');
      } else {
        alert('Erreur lors de la création de la livraison: ' + error.message);
      }
    }
  };

  if (loading) {
    return (
      <div className="dashboard-container">
        <div className={styles.sidebar}>
          <div className="sidebar-header">
            <h1 className="app-title">CheeseFlow</h1>
            <p className="app-subtitle">Production</p>
            <p className="app-subtitle">Gouda Artisanale</p>
          </div>
          <SidebarMenu />
        </div>
        <div className="main-content">
          <div className={styles.loaderContainer}>
            <Loader size={48} className={styles.loader} />
            <p>Chargement des commandes...</p>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard-container">
        <div className={styles.sidebar}>
          <div className="sidebar-header">
            <h1 className="app-title">CheeseFlow</h1>
            <p className="app-subtitle">Production</p>
            <p className="app-subtitle">Gouda Artisanale</p>
          </div>
          <SidebarMenu />
        </div>
        <div className="main-content">
          <div className={styles.errorContainer}>
            <AlertTriangle size={48} className={styles.errorIcon} />
            <p>Erreur de chargement: {error}</p>
            <button onClick={loadData} className={styles.retryButton}>
              Réessayer
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-container">
      <div className={styles.sidebar}>
        <div className="sidebar-header">
          <h1 className="app-title">CheeseFlow</h1>
          <p className="app-subtitle">Production</p>
          <p className="app-subtitle">Gouda Artisanale</p>
        </div>
          <SidebarMenu />
      </div>

      {/* Main Content */}
      <div className="main-content">
        {/* Header */}
        <header className={styles.header}>
          <div className="header-content">
            <div className="header-left">
              <div className="update-info">
                • Mise à jour: {new Date().toLocaleString('fr-FR')}
              </div>
            </div>

            <input
              type="text"
              placeholder="Rechercher un client..."
              value={searchClient}
              onChange={(e) => setSearchClient(e.target.value)}
              className={styles.searchInput}
            />

            <div className="header-right">
              <div className={styles.userMenuContainer}>
                <div className={styles.userMenu}>
                  <User size={24} className={styles.userIcon} />
                  <span className={styles.username}>Admin</span>
                  <ChevronDown size={20} className={styles.chevronIcon} />
                </div>
              </div>
            </div>
          </div>
        </header>

        {/* Main Content */}
        <div className={styles.contentContainer}>
          <div className={styles.pageHeader}>
            <div className={styles.pageTitle}>
              <ShoppingCart className={styles.pageTitleIcon} />
              <h1>Commandes</h1>
            </div>

            <button onClick={() => setShowAddModal(true)} className={styles.addButton}>
              <Plus size={20} />
              Nouvelle commande
            </button>
          </div>

          {/* Filtres */}
          <div className={styles.filterSection}>
            <div className={styles.filterHeader}>
              <Filter size={20} />
              <h2>Filtres</h2>
            </div>

            <div className={styles.filterControls}>
              <div className={styles.filterGroup}>
                <label>Date début</label>
                <input
                  type="date"
                  value={filters.dateDebut}
                  onChange={(e) => setFilters({ ...filters, dateDebut: e.target.value })}
                />
              </div>

              <div className={styles.filterGroup}>
                <label>Date fin</label>
                <input
                  type="date"
                  value={filters.dateFin}
                  onChange={(e) => setFilters({ ...filters, dateFin: e.target.value })}
                />
              </div>

              <div className={styles.filterGroup}>
                <label>Statut</label>
                <select
                  value={filters.statut}
                  onChange={(e) => setFilters({ ...filters, statut: e.target.value })}
                >
                  <option value="tous">Tous</option>
                  <option value="en_attente">En attente</option>
                  <option value="en_cours">En cours</option>
                  <option value="livree">Livrée</option>
                </select>
              </div>

              <div className={styles.filterGroup}>
                <label>Client</label>
                <select
                  value={filters.client}
                  onChange={(e) => setFilters({ ...filters, client: e.target.value })}
                >
                  <option value="tous">Tous</option>
                  {clients.map(client => (
                    <option key={client.id} value={client.id}>
                      {client.nom}
                    </option>
                  ))}
                </select>
              </div>

              <button
                onClick={() => setFilters({
                  dateDebut: '',
                  dateFin: '',
                  statut: 'tous',
                  client: 'tous'
                })}
                className={styles.resetButton}
              >
                Réinitialiser
              </button>
            </div>
          </div>

          {/* Liste des commandes */}
          <div className={styles.commandesContainer}>
            {getFilteredCommandes().length === 0 ? (
              <div className={styles.noCommandes}>
                <p>Aucune commande ne correspond aux critères de recherche.</p>
              </div>
            ) : (
              <div className={styles.commandesList}>
                {getFilteredCommandes().map((commande) => (
                  <div key={commande.id} className={styles.commandeCard}>
                    <div className={styles.commandeHeader}>
                      <h3>Commande #{commande.id}</h3>
                      <div className={`${styles.commandeStatut} ${
                        commande.statutLivraison === 'LIVREE' ? styles.statutLivree : 
                        commande.statutLivraison === 'EN_COURS' ? styles.statutEnCours : styles.statutEnAttente
                      }`}>
                        <span>{commande.statutLivraison?.replace('_', ' ') || 'EN ATTENTE'}</span>
                      </div>
                    </div>

                    <div className={styles.commandeContent}>
                      <p>
                        <strong>Client:</strong> {commande.client?.nom || 'Client inconnu'}
                      </p>
                      <p>
                        <strong>Date:</strong> {formatDate(commande.dateCommande)}
                      </p>
                      <p>
                        <strong>Montant:</strong> {formatCurrency(commande.montantTotal || 0)}
                      </p>
                    </div>

                    <div className={styles.commandeActions}>
                      <button onClick={() => handleShowDetails(commande)} className={styles.detailsButton}>
                        <Search size={16} />
                        Détails
                      </button>

                      {commande.statutLivraison === 'EN_ATTENTE' && (
                        <>
                          <button onClick={() => handleOpenLivraisonModal(commande)} className={styles.livraisonButton}>
                            <Truck size={16} />
                            Livraison
                          </button>
                          
                          <button onClick={() => handleDeleteOrder(commande.id)} className={styles.deleteButton}>
                            <Trash2 size={16} />
                            Supprimer
                          </button>
                        </>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}

            {/* Modale de détails d'une commande */}
            {showDetailsModal && selectedOrder && (
              <div className={styles.modalOverlay}>
                <div className={styles.orderModal}>
                  <button onClick={() => setShowDetailsModal(false)}>
                    <X size={15} className={styles.closeButton} />
                  </button>

                  <div className={styles.modalHeader}>
                    <h2>Détails de la commande #{selectedOrder.id}</h2>
                    <div className={`${styles.commandeStatut} ${
                      selectedOrder.statutLivraison === 'LIVREE' ? styles.statutLivree : 
                      selectedOrder.statutLivraison === 'EN_COURS' ? styles.statutEnCours : styles.statutEnAttente
                    }`}>
                      <span>{selectedOrder.statutLivraison?.replace('_', ' ') || 'EN ATTENTE'}</span>
                    </div>
                  </div>

                  <div className={styles.modalContent}>
                    <div className={styles.orderDetails}>
                      <div className={styles.detailSection}>
                        <h3>Informations générales</h3>
                        <p><strong>Client:</strong> {selectedOrder.client?.nom || 'Client inconnu'}</p>
                        <p><strong>Date de commande:</strong> {formatDate(selectedOrder.dateCommande)}</p>
                        <p><strong>Montant total:</strong> {formatCurrency(selectedOrder.montantTotal || 0)}</p>
                      </div>

                      <div className={styles.detailSection}>
                        <h3>Produits commandés</h3>
                        {selectedOrder.ligneCommandes && selectedOrder.ligneCommandes.length > 0 ? (
                          <div className={styles.produitsTable}>
                            <div className={styles.produitsHeader}>
                              <span>Produit</span>
                              <span>Quantité</span>
                              <span>Prix unitaire</span>
                              <span>Total</span>
                            </div>
                            {selectedOrder.ligneCommandes.map((ligne, index) => {
                              const produit = produits.find(p => p.id === ligne.produit?.id);
                              return (
                                <div key={index} className={styles.produitItem}>
                                  <span>{ligne.produit?.nom || produit?.nom || 'Produit inconnu'}</span>
                                  <span>{ligne.quantite}</span>
                                  <span>{formatCurrency(ligne.produit?.prixVente || produit?.prixVente || 0)}</span>
                                  <span>{formatCurrency((ligne.produit?.prixVente || produit?.prixVente || 0) * ligne.quantite)}</span>
                                </div>
                              );
                            })}
                          </div>
                        ) : (
                          <p>Aucun produit dans cette commande</p>
                        )}
                      </div>

                      {selectedOrder.livraison && (
                        <div className={styles.detailSection}>
                          <h3>Informations de livraison</h3>
                          <p><strong>Livreur:</strong> {selectedOrder.livraison.livreur?.nom || 'Livreur non spécifié'}</p>
                          <p><strong>Zone:</strong> {selectedOrder.livraison.zone || 'Non spécifiée'}</p>
                          <p><strong>Date prévue:</strong> {formatDate(selectedOrder.livraison.dateLivraison)}</p>
                          <p><strong>Statut:</strong> {selectedOrder.statutLivraison?.replace('_', ' ') || 'EN ATTENTE'}</p>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            )}

            {/* Modale d'ajout d'une commande */}
            {showAddModal && (
              <div className={styles.modalOverlayNewCommande}>
                <div className={styles.orderModalNewCommande}>
                  <button onClick={() => setShowAddModal(false)}>
                    <X size={15} className={styles.closeButton} />
                  </button>

                  <div className={styles.modalHeaderNewCommande}>
                    <h2 className={styles.modalTitleNewCommande}>Nouvelle commande</h2>
                  </div>

                  <div className={styles.modalContentNewCommande}>
                    {/* Sélection du client */}
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Client</label>
                      <select
                        className={styles.formSelect}
                        value={newOrder.clientId}
                        onChange={(e) => setNewOrder({ ...newOrder, clientId: e.target.value })}
                      >
                        <option value="">Sélectionner un client</option>
                        {clients.map(client => (
                          <option key={client.id} value={client.id}>
                            {client.nom} - {client.adresse}
                          </option>
                        ))}
                      </select>
                    </div>

                    {/* Date de commande */}
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Date</label>
                      <input
                        type="date"
                        className={styles.formInputDate}
                        value={newOrder.date}
                        onChange={(e) => setNewOrder({ ...newOrder, date: e.target.value })}
                      />
                    </div>

                    {/* Sélection des produits */}
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Produits disponibles</label>
                      <div className={styles.produitsCheckboxes}>
                        {produits.map(produit => (
                          <div key={produit.id} className={styles.produitCheckboxItem}>
                            <input
                              type="checkbox"
                              id={`produit-${produit.id}`}
                              checked={selectedProduits.some(p => p.id === produit.id)}
                              onChange={(e) => {
                                if (e.target.checked) {
                                  setSelectedProduits([...selectedProduits, { 
                                    id: produit.id, 
                                    nom: produit.nom, 
                                    prix: produit.prixVente || 0,
                                    quantite: 1 
                                  }]);
                                } else {
                                  setSelectedProduits(selectedProduits.filter(p => p.id !== produit.id));
                                }
                              }}
                            />
                            <label htmlFor={`produit-${produit.id}`}>
                              {produit.nom} - {formatCurrency(produit.prixVente || 0)}
                            </label>
                          </div>
                        ))}
                      </div>
                    </div>

                    {/* Quantités pour les produits sélectionnés */}
                    {selectedProduits.length > 0 && (
                      <div className={styles.formGroup}>
                        <label className={styles.formLabel}>Quantités</label>
                        {selectedProduits.map((produit, index) => (
                          <div key={produit.id} className={styles.quantityInput}>
                            <label>{produit.nom}:</label>
                            <input
                              type="number"
                              min="1"
                              value={produit.quantite}
                              onChange={(e) => {
                                const newSelectedProduits = [...selectedProduits];
                                newSelectedProduits[index].quantite = parseInt(e.target.value) || 1;
                                setSelectedProduits(newSelectedProduits);
                              }}
                              className={styles.formInput}
                            />
                          </div>
                        ))}
                        <button 
                          type="button" 
                          onClick={addProduitToOrder}
                          className={styles.addProductButton}
                        >
                          <Plus size={16} /> Ajouter à la commande
                        </button>
                      </div>
                    )}

                    {/* Produits ajoutés à la commande */}
                    {newOrder.produits.length > 0 && (
                      <div className={styles.formGroup}>
                        <label className={styles.formLabel}>Produits dans la commande</label>
                        <div className={styles.selectedProducts}>
                          {newOrder.produits.map((produitData, index) => {
                            const produit = produits.find(p => p.id === produitData.produitId);
                            return (
                              <div key={index} className={styles.selectedProductItem}>
                                <span>{produit?.nom || 'Produit inconnu'}</span>
                                <span>Quantité: {produitData.quantite}</span>
                                <span>Prix: {formatCurrency((produit?.prixVente || 0) * produitData.quantite)}</span>
                                <button
                                  type="button"
                                  onClick={() => removeProduitFromOrder(index)}
                                  className={styles.removeProductButton}
                                >
                                  <Trash2 size={14} />
                                </button>
                              </div>
                            );
                          })}
                        </div>
                        <div className={styles.orderTotal}>
                          <strong>Total: {formatCurrency(calculateOrderTotal())}</strong>
                        </div>
                      </div>
                    )}
                  </div>

                  <div className={styles.modalActionsNewCommande}>
                    <button
                      className={`${styles.actionButtonNewCommande} ${styles.cancelButton}`}
                      onClick={() => setShowAddModal(false)}
                    >
                      Annuler
                    </button>
                    <button
                      className={`${styles.actionButtonNewCommande} ${styles.confirmButton}`}
                      onClick={handleAddOrder}
                      disabled={newOrder.produits.length === 0}
                    >
                      Enregistrer
                    </button>
                  </div>
                </div>
              </div>
            )}

            {/* Modale de livraison */}
            {showLivraisonModal && selectedOrder && (
              <div className={styles.modalOverlayNewCommande}>
                <div className={styles.orderModalNewCommande}>
                  <button onClick={() => setShowLivraisonModal(false)}>
                    <X size={15} className={styles.closeButton} />
                  </button>

                  <div className={styles.modalHeaderNewCommande}>
                    <h2 className={styles.modalTitleNewCommande}>Créer une livraison</h2>
                  </div>

                  <div className={styles.modalContentNewCommande}>
                    {/* Informations de la commande */}
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Commande</label>
                      <div className={styles.commandeInfo}>
                        <p><strong>Client:</strong> {selectedOrder.client?.nom || 'Client inconnu'}</p>
                        <p><strong>Montant:</strong> {formatCurrency(selectedOrder.montantTotal || 0)}</p>
                        <p><strong>Date:</strong> {formatDate(selectedOrder.dateCommande)}</p>
                      </div>
                    </div>

                    {/* Sélection du livreur */}
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Livreur</label>
                      <select
                        className={styles.formSelect}
                        value={livraisonForm.livreurId}
                        onChange={(e) => setLivraisonForm({ ...livraisonForm, livreurId: e.target.value })}
                      >
                        <option value="">Sélectionner un livreur</option>
                        {livreurs.map(livreur => (
                          <option key={livreur.id} value={livreur.id}>
                            {livreur.nom} - {livreur.telephone}
                          </option>
                        ))}
                      </select>
                    </div>

                    {/* Zone de livraison */}
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Zone de livraison</label>
                      <input
                        type="text"
                        className={styles.formInput}
                        value={livraisonForm.zone}
                        onChange={(e) => setLivraisonForm({ ...livraisonForm, zone: e.target.value })}
                        placeholder="Ex: Antananarivo Centre, Ivato, etc."
                      />
                    </div>

                    {/* Date de livraison */}
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Date de livraison</label>
                      <input
                        type="date"
                        className={styles.formInputDate}
                        value={livraisonForm.dateLivraison}
                        onChange={(e) => setLivraisonForm({ ...livraisonForm, dateLivraison: e.target.value })}
                        min={new Date().toISOString().split('T')[0]}
                      />
                    </div>
                  </div>

                  <div className={styles.modalActionsNewCommande}>
                    <button
                      className={`${styles.actionButtonNewCommande} ${styles.cancelButton}`}
                      onClick={() => setShowLivraisonModal(false)}
                    >
                      Annuler
                    </button>
                    <button
                      className={`${styles.actionButtonNewCommande} ${styles.confirmButton}`}
                      onClick={handleCreateLivraison}
                      disabled={!livraisonForm.livreurId || !livraisonForm.zone || !livraisonForm.dateLivraison}
                    >
                      Créer la livraison
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export { CommandesPage };
EOF

# Remplacer l'ancien fichier par le nouveau
cat "$TMP_FILE" > "$FILE_PATH"

echo "Fichier réécrit avec succès pour corriger l'erreur JSX à la ligne 984."

# Supprimer le fichier temporaire
rm "$TMP_FILE"

echo "Correction terminée."
