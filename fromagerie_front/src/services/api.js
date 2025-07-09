const API_BASE_URL = 'http://localhost:8080/api';

// Service pour les commandes
export const commandeAPI = {
  // Récupérer toutes les commandes
  getAllCommandes: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la récupération des commandes:', error);
      throw error;
    }
  },

  // Récupérer une commande par ID
  getCommandeById: async (id) => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes/${id}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la récupération de la commande:', error);
      throw error;
    }
  },

  // Créer une nouvelle commande avec produits
  createCommande: async (clientId, dateCommande, produits) => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes/create`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          clientId,
          dateCommande,
          produits
        }),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la création de la commande:', error);
      throw error;
    }
  },

  // Créer une commande (ancienne méthode - gardée pour compatibilité)
  createCommandeLegacy: async (clientId, formData) => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes/${clientId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la création de la commande:', error);
      throw error;
    }
  },

  // Sauvegarder une commande
  saveCommande: async (clientId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes/save`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ clientId }),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la sauvegarde de la commande:', error);
      throw error;
    }
  },

  // Mettre à jour une commande
  updateCommande: async (id, commande) => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(commande),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la mise à jour de la commande:', error);
      throw error;
    }
  },

  // Supprimer une commande
  deleteCommande: async (id) => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes/${id}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la suppression de la commande:', error);
      throw error;
    }
  },
};

// Service pour les clients
export const clientAPI = {
  // Récupérer tous les clients
  getAllClients: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/clients`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la récupération des clients:', error);
      throw error;
    }
  },

  // Créer un nouveau client
  createClient: async (client) => {
    try {
      const response = await fetch(`${API_BASE_URL}/clients`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(client),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la création du client:', error);
      throw error;
    }
  },
};

// Service pour les produits
export const produitAPI = {
  // Récupérer tous les produits
  getAllProduits: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/produits`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la récupération des produits:', error);
      throw error;
    }
  },
};

// Service pour les livreurs
export const livreurAPI = {
  // Récupérer tous les livreurs
  getAllLivreurs: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/commandes/livreurs`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la récupération des livreurs:', error);
      throw error;
    }
  },
};

// Service pour les livraisons
export const livraisonAPI = {
  // Récupérer toutes les livraisons
  getAllLivraisons: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/livraisons`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la récupération des livraisons:', error);
      throw error;
    }
  },

  // Créer une livraison
  createLivraison: async ({ commandeId, livreurId, zone, dateLivraison }) => {
    try {
      const response = await fetch(`${API_BASE_URL}/livraisons/livraison`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          commandeId: parseInt(commandeId),
          livreur: parseInt(livreurId),
          dateLivraison,
          zone
        }),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la création de la livraison:', error);
      throw error;
    }
  },

  // Mettre à jour le statut d'une livraison
  updateStatutLivraison: async (livraisonId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/livraisons/${livraisonId}/statut`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la mise à jour du statut:', error);
      throw error;
    }
  },

  // Confirmer livraison et paiement
  confirmerLivraisonEtPaiement: async (livraisonId, paiementData) => {
    try {
      const response = await fetch(`${API_BASE_URL}/livraisons/${livraisonId}/confirmer-livraison-paiement`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(paiementData),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la confirmation:', error);
      throw error;
    }
  },
}; 
