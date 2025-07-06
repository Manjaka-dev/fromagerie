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
                • Mise à jour: {new Date().toLocaleString('fr-FR')}
              </div>
            </div>
            <input
              type="text"
              placeholder="Rechercher des livraisons..."
              className="searchInput"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
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
          <h1>Gestion des Livraisons</h1>

          <div className="header-section">
            <h2>Suivi des livraisons</h2>
            <span className="optimisation-badge">En temps réel</span>
          </div>

          <div className="divider"></div>

          {/* Statistiques principales */}
          <div className="main-stats">
            <div className="livraison-stat">
              <h3>Total Livraisons</h3>
              <p className="stat-value">{stats.totalLivraisons}</p>
            </div>
            <div className="livraison-stat">
              <h3>Planifiées</h3>
              <p className="stat-value" style={{ color: '#3b82f6' }}>{stats.livraisonsPlanifiees}</p>
            </div>
            <div className="livraison-stat">
              <h3>En cours</h3>
              <p className="stat-value" style={{ color: '#f59e0b' }}>{stats.livraisonsEnCours}</p>
            </div>
            <div className="livraison-stat">
              <h3>Livrées</h3>
              <p className="stat-value" style={{ color: '#10b981' }}>{stats.livraisonsLivrees}</p>
            </div>
          </div>

          {/* Section recherche et filtres */}
          <div className="search-section">
            <div className="search-bar">
              <Search className="search-icon" size={18} />
              <input 
                type="text" 
                placeholder="Rechercher par client, zone ou livreur..." 
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
            
            {/* Filtres */}
            <div className="filters-section">
              <div className="filter-group">
                <label>Date de début</label>
                <input
                  type="date"
                  value={filters.dateDebut}
                  onChange={(e) => setFilters({ ...filters, dateDebut: e.target.value })}
                />
              </div>
              
              <div className="filter-group">
                <label>Date de fin</label>
                <input
                  type="date"
                  value={filters.dateFin}
                  onChange={(e) => setFilters({ ...filters, dateFin: e.target.value })}
                />
              </div>
              
              <div className="filter-group">
                <label>Statut</label>
                <select
                  value={filters.statut}
                  onChange={(e) => setFilters({ ...filters, statut: e.target.value })}
                >
                  <option value="tous">Tous les statuts</option>
                  <option value="planifié">Planifiées</option>
                  <option value="en cours">En cours</option>
                  <option value="livré">Livrées</option>
                </select>
              </div>
              
              <button className="clear-filters-btn" onClick={clearFilters}>
                Effacer les filtres
              </button>
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
            ) : (
              filteredLivraisons.map(livraison => (
                <div key={livraison.livraisonId} className="livraison-card">
                  <div className="livraison-header">
                    <div>
                      <h3>{livraison.clientNom || 'Client inconnu'}</h3>
                      <span className="client-name">Zone: {livraison.zone || 'Non définie'}</span>
                    </div>
                    <div style={{ 
                      padding: '4px 12px', 
                      borderRadius: '20px', 
                      backgroundColor: getStatusColor(livraison.statutLivraison) + '20',
                      color: getStatusColor(livraison.statutLivraison),
                      fontWeight: 'bold',
                      fontSize: '14px'
                    }}>
                      {livraison.statutLivraison || 'Statut inconnu'}
                    </div>
                  </div>
                  
                  <div className="livraison-details">
                    <p><strong>Montant :</strong> {formatAmount(livraison.montantTotal)}</p>
                    <p><strong>Produits :</strong> {livraison.produitsCommandes || 'Non spécifiés'}</p>
                    <p><strong>Livreur :</strong> {livraison.livreurNom || 'Non assigné'}</p>
                    <p><strong>Contact :</strong> {livraison.clientTelephone || 'Non disponible'}</p>
                  </div>
                  
                  <div className="livraison-actions">
                    {livraison.statutLivraison?.toLowerCase().includes('planifié') && (
                      <button 
                        className="track-btn"
                        onClick={() => handleUpdateStatut(livraison)}
                        style={{ backgroundColor: '#f59e0b' }}
                      >
                        <Play size={16} style={{ marginRight: '5px' }} />
                        Démarrer
                      </button>
                    )}
                    
                    {livraison.statutLivraison?.toLowerCase().includes('en cours') && (
                      <button 
                        className="track-btn"
                        onClick={() => handleUpdateStatut(livraison)}
                        style={{ backgroundColor: '#10b981' }}
                      >
                        <CheckCircle size={16} style={{ marginRight: '5px' }} />
                        Livrer
                      </button>
                    )}
                    
                    {livraison.statutLivraison?.toLowerCase().includes('livré') && (
                      <button className="contact-btn" disabled style={{ backgroundColor: '#6b7280' }}>
                        <CheckCircle size={16} style={{ marginRight: '5px' }} />
                        Terminée
                      </button>
                    )}
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Modal de paiement et confirmation */}
        {showPaiementModal && selectedLivraison && (
          <div className="modal-overlay">
            <div className="modal-content">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <h2>
                  <DollarSign size={24} style={{ marginRight: '10px' }} />
                  Confirmation de livraison et paiement
                </h2>
                <button 
                  onClick={() => setShowPaiementModal(false)}
                  style={{ background: 'none', border: 'none', cursor: 'pointer' }}
                >
                  <X size={24} />
                </button>
              </div>

              <div className="info-box">
                <h3>Informations de la livraison</h3>
                <p><strong>Client:</strong> {selectedLivraison.clientNom}</p>
                <p><strong>Zone:</strong> {selectedLivraison.zone}</p>
                <p><strong>Livreur:</strong> {selectedLivraison.livreurNom}</p>
                <p><strong>Montant total:</strong> {formatAmount(selectedLivraison.montantTotal)}</p>
              </div>

              <div style={{ marginBottom: '20px' }}>
                <label>
                  Montant du paiement
                </label>
                <input
                  type="number"
                  value={paiementForm.montantPaiement}
                  onChange={(e) => setPaiementForm({ ...paiementForm, montantPaiement: e.target.value })}
                  placeholder="Entrez le montant"
                />
              </div>

              <div style={{ marginBottom: '20px' }}>
                <label>
                  Méthode de paiement
                </label>
                <select
                  value={paiementForm.methodePaiement}
                  onChange={(e) => setPaiementForm({ ...paiementForm, methodePaiement: e.target.value })}
                >
                  <option value="espèces">Espèces</option>
                  <option value="mobile money">Mobile Money</option>
                  <option value="virement bancaire">Virement bancaire</option>
                  <option value="carte bancaire">Carte bancaire</option>
                </select>
              </div>

              <div style={{ marginBottom: '20px' }}>
                <label>
                  Date de paiement
                </label>
                <input
                  type="date"
                  value={paiementForm.datePaiement}
                  onChange={(e) => setPaiementForm({ ...paiementForm, datePaiement: e.target.value })}
                />
              </div>

              <div style={{ display: 'flex', gap: '10px', justifyContent: 'flex-end' }}>
                <button onClick={() => setShowPaiementModal(false)}>
                  Annuler
                </button>
                <button onClick={handleConfirmerLivraison}>
                  <CheckCircle size={16} style={{ marginRight: '5px' }} />
                  Confirmer livraison et paiement
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Livraison;