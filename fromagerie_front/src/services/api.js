import { useState, useEffect } from 'react';

// Configuration de l'API pour communiquer avec le backend Spring Boot
const API_BASE_URL = 'http://localhost:8080/api';

// Configuration axios-like pour les requêtes
const apiRequest = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;
  const config = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  };

  if (config.body && typeof config.body === 'object') {
    config.body = JSON.stringify(config.body);
  }

  try {
    const response = await fetch(url, config);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error(`API Error on ${endpoint}:`, error);
    throw error;
  }
};

// Services API

// ==================== COMMANDES ====================
export const commandeAPI = {
  // Récupérer toutes les commandes
  getAllCommandes: () =>
    apiRequest('/commandes'),

  // Récupérer une commande par ID
  getCommandeById: (id) =>
    apiRequest(`/commandes/${id}`),

  // Créer une nouvelle commande
  createCommande: (commandeData) =>
    apiRequest('/commandes', { method: 'POST', body: commandeData }),

  // Mettre à jour une commande
  updateCommande: (id, commandeData) =>
    apiRequest(`/commandes/${id}`, { method: 'PUT', body: commandeData }),

  // Supprimer une commande
  deleteCommande: (id) =>
    apiRequest(`/commandes/${id}`, { method: 'DELETE' }),

  // Filtrer les commandes par statut
  getCommandesByStatut: (statut) =>
    apiRequest(`/commandes/statut/${statut}`),
};

// ==================== CLIENTS ====================
export const clientAPI = {
  // Récupérer tous les clients
  getAllClients: () =>
    apiRequest('/clients'),

  // Récupérer un client par ID
  getClientById: (id) =>
    apiRequest(`/clients/${id}`),

  // Créer un nouveau client
  createClient: (clientData) =>
    apiRequest('/clients', { method: 'POST', body: clientData }),

  // Mettre à jour un client
  updateClient: (id, clientData) =>
    apiRequest(`/clients/${id}`, { method: 'PUT', body: clientData }),

  // Supprimer un client
  deleteClient: (id) =>
    apiRequest(`/clients/${id}`, { method: 'DELETE' }),

  // Rechercher des clients par nom
  searchClients: (nom) =>
    apiRequest(`/clients/search?nom=${nom}`),
};

// ==================== PRODUITS ====================
export const produitAPI = {
  // Récupérer tous les produits
  getAllProduits: () =>
    apiRequest('/produits'),

  // Récupérer un produit par ID
  getProduitById: (id) =>
    apiRequest(`/produits/${id}`),

  // Créer un nouveau produit
  createProduit: (produitData) =>
    apiRequest('/produits', { method: 'POST', body: produitData }),

  // Mettre à jour un produit
  updateProduit: (id, produitData) =>
    apiRequest(`/produits/${id}`, { method: 'PUT', body: produitData }),

  // Supprimer un produit
  deleteProduit: (id) =>
    apiRequest(`/produits/${id}`, { method: 'DELETE' }),

  // Rechercher des produits par nom
  searchProduits: (nom) =>
    apiRequest(`/produits/search?nom=${nom}`),
};

// ==================== LIVREURS ====================
export const livreurAPI = {
  // Récupérer tous les livreurs
  getAllLivreurs: () =>
    apiRequest('/commandes/livreurs'),

  // Créer un nouveau livreur
  createLivreur: (livreurData) =>
    apiRequest('/livreurs', { method: 'POST', body: livreurData }),

  // Rechercher des livreurs par nom
  searchLivreurs: (nom) =>
    apiRequest(`/livreurs/search?nom=${nom}`),
};

// ==================== COMPTABILITÉ ====================
export const comptabiliteAPI = {
  // Revenus
  getRevenus: (dateDebut, dateFin) =>
    apiRequest(`/revenus/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`),

  getTotalRevenus: (dateDebut, dateFin) =>
    apiRequest(`/revenus/total?dateDebut=${dateDebut}&dateFin=${dateFin}`),

  createRevenu: (revenuData) =>
    apiRequest('/revenus', { method: 'POST', body: revenuData }),

  // Dépenses
  getDepenses: (dateDebut, dateFin) =>
    apiRequest(`/depenses/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`),

  getDepensesByCategorie: (categorie) =>
    apiRequest(`/depenses/categorie?categorie=${categorie}`),

  getTotalDepenses: (dateDebut, dateFin) =>
    apiRequest(`/depenses/total?dateDebut=${dateDebut}&dateFin=${dateFin}`),

  createDepense: (depenseData) =>
    apiRequest('/depenses', { method: 'POST', body: depenseData }),

  // Bilans
  getBilans: () =>
    apiRequest('/bilans'),

  getBilanByPeriode: (periode) =>
    apiRequest(`/bilans/periode?periode=${periode}`),
};

// ==================== STOCK ====================
export const stockAPI = {
  // Matières premières
  getAllMatieresPremiere: () =>
    apiRequest('/stock/matieres-premieres'),

  createMatierePremiere: (matiereData) =>
    apiRequest('/stock/matieres-premieres', { method: 'POST', body: matiereData }),

  getMatierePremiere: (id) =>
    apiRequest(`/stock/matieres-premieres/${id}`),

  // Mouvements de stock
  getMouvementsStock: () =>
    apiRequest(`/stock/mouvements`),

  getMouvementsProduits: () =>
    apiRequest(`/stock/mouvement`),

  getSimulationProduits: () =>
    apiRequest(`/stock/simulation/all`),

  // getStockProduit: () =>
  //   apiRequest(`/stock/produit-fini`),

  getStockProduit: () =>
    apiRequest(`/stock/mouvement/courant`),

  createSimulationProduits: (id, quantite) =>
    apiRequest(`/stock/simulation-production?produitId=${id}&quantite=${quantite}`, { method: 'POST' }),

  createMouvementStock: (mouvementData) =>
    apiRequest('/stock/mouvements', { method: 'POST', body: mouvementData }),
};

// ==================== PRODUCTION ====================
export const productionAPI = {
  // Productions effectuées
  getProductionsByDate: (date) =>
    apiRequest(`/productions/date?date=${date}`),

  getProductionsByDateRange: (dateDebut, dateFin) =>
    apiRequest(`/productions/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`),

  getProductionsByProduit: (produitId) =>
    apiRequest(`/productions/produit/${produitId}`),

  // Fiches de production
  getFichesByProduit: (produitId) =>
    apiRequest(`/fiches/produit/${produitId}`),

  createFicheProduction: (ficheData) =>
    apiRequest('/fiches', { method: 'POST', body: ficheData }),

  // Pertes de production
  getPertesByProduction: (productionId) =>
    apiRequest(`/pertes/production/${productionId}`),

  getAverageTauxPerte: () =>
    apiRequest('/pertes/average-taux'),
};

// ==================== LIVRAISONS ====================
export const livraisonAPI = {
  // Livraisons
  getAllLivraisons: () =>
    apiRequest('/livraisons'),

  getLivraisonById: (id) =>
    apiRequest(`/livraisons/${id}`),

  createLivraison: (livraisonData) =>
    apiRequest('/livraisons', { method: 'POST', body: livraisonData }),

  // 🆕 MODIFICATION COMPLÈTE D'UNE LIVRAISON
  updateLivraison: (id, updateData) =>
    apiRequest(`/livraisons/${id}`, { method: 'PUT', body: updateData }),

  // 🆕 ANNULATION D'UNE LIVRAISON  
  deleteLivraison: (id) =>
    apiRequest(`/livraisons/${id}`, { method: 'DELETE' }),

  updateStatutLivraison: (id, statut) =>
    apiRequest(`/livraisons/${id}/statut`, { method: 'PUT', body: { statut } }),

  // 🆕 CONFIRMATION PAIEMENT ET LIVRAISON
  confirmerLivraisonPaiement: (id, paiementData) =>
    apiRequest(`/livraisons/${id}/confirmer-livraison-paiement`, {
      method: 'POST',
      body: paiementData
    }),

  // 🆕 LISTE DES ZONES DE LIVRAISON
  getZonesLivraison: () =>
    apiRequest('/livraisons/zones'),

  // Recherches par livreur/zone
  getLivraisonsByLivreur: (livreurId) =>
    apiRequest(`/livraisons/livreur/${livreurId}`),

  getLivraisonsByZone: (zone) =>
    apiRequest(`/livraisons/zone/${zone}`),

  // Autres fonctions existantes  
  exportCommandePdf: (commandeId) =>
    apiRequest(`/livraisons/commandes/${commandeId}/export-pdf`),

  retourLivraison: (retourData) =>
    apiRequest('/livraisons/retour-livraison', { method: 'POST', body: retourData }),
};

// ==================== UTILITAIRES ====================
export const formatCurrency = (amount) => {
  return new Intl.NumberFormat('fr-MG', {
    style: 'currency',
    currency: 'MGA',
    minimumFractionDigits: 0,
  }).format(amount);
};

export const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('fr-FR');
};

export const getCurrentDate = () => {
  return new Date().toISOString().split('T')[0];
};

export const getDateRange = (days = 30) => {
  const end = new Date();
  const start = new Date();
  start.setDate(start.getDate() - days);

  return {
    dateDebut: start.toISOString().split('T')[0],
    dateFin: end.toISOString().split('T')[0],
  };
};

// ==================== HOOKS ET UTILITAIRES ====================
export const useApiData = (apiCall, dependencies = []) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);
      const result = await apiCall();
      setData(result);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, dependencies);

  return { data, loading, error, refetch: fetchData };
};
