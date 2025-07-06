import React, { useState, useEffect } from 'react';
import '../assets/styles/Livraison.css';
import SidebarMenu from "../components/SidebarMenu";
import {
  BarChart3,
  Package,
  TrendingDown,
  TrendingUp,
  Calculator,
  ShoppingCart,
  Truck,
  Settings,
  Factory,
  Calendar,
  Clock,
  User,
  ChevronDown,
  Bell,
  Search,
  Filter,
  X,
  MoreHorizontal,
  AlertTriangle,
  CheckCircle,
  Play,
  Loader,
  DollarSign,
  FileText
} from 'lucide-react';
import { NavLink } from 'react-router-dom';
import { livraisonAPI, formatCurrency, formatDate } from '../services/api';

const Livraison = () => {
  // États pour les données
  const [livraisons, setLivraisons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  
  // États pour les filtres
  const [filters, setFilters] = useState({
    dateDebut: '',
    dateFin: '',
    statut: 'tous'
  });

  // États pour les modales
  const [showPaiementModal, setShowPaiementModal] = useState(false);
  const [selectedLivraison, setSelectedLivraison] = useState(null);
  const [paiementForm, setPaiementForm] = useState({
    montantPaiement: '',
    methodePaiement: 'espèces',
    datePaiement: new Date().toISOString().split('T')[0]
  });

  // États pour les statistiques
  const [stats, setStats] = useState({
    livraisonsDuJour: 0,
    livreursActifs: 0,
    distanceOptimisee: "0 km",
    tempsEstime: "0h 00min"
  });

  // Charger les données au montage
  useEffect(() => {
    loadLivraisons();
  }, []);

  const loadLivraisons = async () => {
    setLoading(true);
    setError(null);
    try {
      const livraisonsData = await livraisonAPI.getAllLivraisons();
      
      // Adapter les données de l'API backend pour l'interface
      const adaptedLivraisons = livraisonsData.map(livraison => ({
        id: livraison.id,
        commande: {
          id: livraison.commandeId || livraison.id,
          clientNom: livraison.clientNom,
          total: livraison.montantTotal || 0
        },
        livreur: {
          nom: livraison.livreurNom || 'Livreur non assigné',
          telephone: livraison.livreurTelephone || ''
        },
        zone: livraison.zone || 'Zone non définie',
        dateLivraison: livraison.dateLivraison || new Date().toISOString().split('T')[0],
        statut: mapStatutFromBackend(livraison.statut),
        adresse: livraison.adresse || 'Adresse du client',
        produits: livraison.produits || []
      }));

      setLivraisons(adaptedLivraisons);
      
      // Calculer les statistiques
      calculateStats(adaptedLivraisons);
      
    } catch (err) {
      console.error('Erreur lors du chargement des livraisons:', err);
      setError('Impossible de charger les livraisons. Veuillez vérifier que le backend est démarré.');
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour mapper les statuts du backend vers le frontend
  const mapStatutFromBackend = (statutBackend) => {
    if (!statutBackend) return 'inconnu';
    
    const statutMap = {
      'Planifiée': 'planifiée',
      'En cours': 'en cours',
      'Livrée': 'livré',
      'Annulée': 'annulée',
      'Échec de livraison': 'échec'
    };
    
    return statutMap[statutBackend] || statutBackend.toLowerCase();
  };

  // Fonction pour parser les produits depuis la chaîne de caractères
  const parseProduits = (produitsString) => {
    if (!produitsString) return [];
    
    try {
      // Essayer de parser comme JSON d'abord
      if (produitsString.startsWith('[') || produitsString.startsWith('{')) {
        return JSON.parse(produitsString);
      }
      
      // Sinon, traiter comme une chaîne de produits séparés par des virgules
      return produitsString.split(',').map((produit, index) => ({
        nom: produit.trim(),
        quantite: 1 // Quantité par défaut
      }));
    } catch (error) {
      console.error('Erreur lors du parsing des produits:', error);
      return [{ nom: produitsString, quantite: 1 }];
    }
  };

  // Fonction pour calculer les statistiques
  const calculateStats = (livraisons) => {
    const today = new Date().toISOString().split('T')[0];
    
    const livraisonsDuJour = livraisons.filter(liv => 
      liv.dateLivraison === today && liv.statut === 'livré'
    ).length;
    
    const livreursActifs = new Set(
      livraisons
        .filter(liv => liv.statut === 'en cours' || liv.statut === 'planifiée')
        .map(liv => liv.livreur.nom)
    ).size;
    
    setStats({
      livraisonsDuJour,
      livreursActifs,
      distanceOptimisee: `${Math.round(livraisons.length * 2.5)} km`,
      tempsEstime: `${Math.floor(livraisons.length * 0.5)}h ${Math.round((livraisons.length * 0.5 % 1) * 60)}min`
    });
  };

  // Fonction pour mettre à jour le statut d'une livraison
  const updateStatutLivraison = async (id, nouveauStatut) => {
    try {
      // Utiliser l'endpoint backend pour mettre à jour le statut
      const response = await livraisonAPI.updateStatutLivraison(id);
      
      if (response.action === 'confirmation_paiement') {
        // Si une confirmation de paiement est nécessaire, ouvrir la modal
        setSelectedLivraison(response.livraison);
        setPaiementForm({
          ...paiementForm,
          montantPaiement: response.livraison.montantTotal || 0
        });
        setShowPaiementModal(true);
      } else {
        // Recharger les données après mise à jour
        await loadLivraisons();
      }
    } catch (err) {
      console.error('Erreur lors de la mise à jour du statut:', err);
      setError('Impossible de mettre à jour le statut de la livraison.');
    }
  };

  // Fonction pour traiter le paiement
  const handlePaiement = async () => {
    try {
      await livraisonAPI.confirmerLivraisonEtPaiement(selectedLivraison.livraisonId, paiementForm);
      
      // Fermer la modal et réinitialiser
      setShowPaiementModal(false);
      setPaiementForm({
        montantPaiement: '',
        methodePaiement: 'espèces',
        datePaiement: new Date().toISOString().split('T')[0]
      });
      setSelectedLivraison(null);
      
      // Recharger les données
      await loadLivraisons();
    } catch (err) {
      console.error('Erreur lors du traitement du paiement:', err);
      setError('Erreur lors de la confirmation du paiement.');
    }
  };

  // -----------------RECHERCHE-------------------------------

  return (
    <div className="dashboard-container">

      {/* Sidebar */}
      <div className="sidebar">
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
        <header className="header">
          <div className="header-content">
            <div className="header-left">
              <div className="update-info">
                • Mise à jour: 11 Juin 2025, 14:30
              </div>
            </div>
            {/* Barre de recherche centrée avec Lucide Search icon */}
            {/* <Search className="search-icon" size={18} /> */}
            <input
              type="text"
              placeholder="Rechercher des produits, références..."
              className="searchInput"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            {/* {searchQuery && (
                          <X
                            className="clear-search-icon"
                            size={18}
                            onClick={() => setSearchQuery('')}
                          />
                        )} */}

            <div className="header-right">
              <div className="time-selector">
                <Clock className="time-icon" />
                <span className="time-text">7 jours</span>
                <ChevronDown className="time-chevron" />
              </div>

              <Bell className="notification-icon" />

              <div className="user-avatar">
                <User className="user-icon" />
              </div>
              <ChevronDown className="user-chevron" />
            </div>
          </div>
        </header>

        <div className="livraison-container">
          <h1>Tournée du jour</h1>

          <div className="header-section">
            <h2>Suivi livreurs</h2>
            <span className="optimisation-badge">Optimisation</span>
          </div>

          <div className="divider"></div>

          {/* Statistiques principales */}
          <div className="main-stats">
            <div className="livraison-stat">
              <h3>Livraison du jour</h3>
              <p className="stat-value">{stats.livraisonsDuJour}</p>
              <p className="stat-label">Livrés</p>
            </div>
          </div>

          {/* Section recherche et livreurs */}
          <div className="search-section">
            <div className="search-bar">
              <input type="text" placeholder="Rechercher..." />
            </div>
            <div className="livreurs-stat">
              <p className="livreurs-count">{stats.livreursActifs}</p>
              <p className="livreurs-label">Livreurs actifs</p>
            </div>
          </div>

          {/* Liste des livraisons */}
          <div className="livraison-list">
            {loading && (
              <div className="loading-message">
                <p>Chargement des livraisons...</p>
              </div>
            )}
            
            {error && (
              <div className="error-message">
                <p>Erreur : {error}</p>
                <button onClick={loadLivraisons}>Réessayer</button>
              </div>
            )}
            
            {!loading && !error && livraisons.length === 0 && (
              <div className="empty-message">
                <p>Aucune livraison trouvée</p>
              </div>
            )}
            
            {!loading && !error && livraisons.map(livraison => (
              <div key={livraison.id} className="livraison-card">
                <div className="livraison-header">
                  <h3>CheeseFlow</h3>
                  <span className="client-name">{livraison.commande.clientNom}</span>
                </div>
                <div className="livraison-details">
                  <p><strong>Montant :</strong> {formatCurrency(livraison.commande.total)}</p>
                  <p><strong>Produits :</strong> {livraison.produits.map(p => `${p.nom} (${p.quantite})`).join(', ')}</p>
                  <p><strong>Livreur :</strong> {livraison.livreur.nom}</p>
                  <p><strong>Zone :</strong> {livraison.zone}</p>
                  <p><strong>Statut :</strong> <span className={`statut-${livraison.statut.replace(' ', '-')}`}>{livraison.statut}</span></p>
                  <p><strong>Prévu :</strong> {formatDate(livraison.dateLivraison)}</p>
                </div>
                <div className="livraison-actions">
                  <button 
                    className="track-btn"
                    onClick={() => updateStatutLivraison(livraison.id, 'en cours')}
                    disabled={livraison.statut === 'livré' || livraison.statut === 'en cours'}
                  >
                    {livraison.statut === 'planifiée' ? 'Démarrer' : 'Suivre'}
                  </button>
                  <button 
                    className="contact-btn"
                    onClick={() => updateStatutLivraison(livraison.id, 'livré')}
                    disabled={livraison.statut === 'livré' || livraison.statut === 'planifiée'}
                  >
                    {livraison.statut === 'en cours' ? 'Terminer' : 'Contacter'}
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* Section carte et optimisation */}
          <div className="map-section">
            <h3>Carte interactive</h3>
            <div className="map-placeholder">
              <p>Tournée Optimisée</p>
            </div>
          </div>

          {/* Stats d'optimisation */}
          <div className="optimisation-stats">
            <h3>Tournée Optimisée</h3>
            <div className="optimisation-details">
              <p><strong>Distance:</strong> {stats.distanceOptimisee}</p>
              <p><strong>Temps estimé:</strong> {stats.tempsEstime}</p>
            </div>
          </div>
        </div>

      </div>


    </div>
  );
};

export default Livraison;