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
import { livraisonAPI } from '../services/api';

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

  // Charger les données au montage
  useEffect(() => {
    loadLivraisons();
  }, []);

  const loadLivraisons = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await livraisonAPI.getAllLivraisons();
      setLivraisons(response.livraisons || []);
    } catch (err) {
      console.error('Erreur lors du chargement des livraisons:', err);
      setError('Erreur lors du chargement des livraisons');
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour formater le montant
  const formatAmount = (amount) => {
    let numericAmount = 0;
    if (amount !== null && amount !== undefined) {
      if (typeof amount === 'object') {
        // Si c'est un objet BigDecimal du backend
        numericAmount = parseFloat(amount.toString()) || 0;
      } else {
        numericAmount = parseFloat(amount) || 0;
      }
    }
    
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'MGA',
      minimumFractionDigits: 0
    }).format(numericAmount).replace('MGA', 'Ar');
  };

  // Fonction pour formater la date
  const formatDate = (dateString) => {
    if (!dateString) return 'Date non définie';
    return new Date(dateString).toLocaleDateString('fr-FR');
  };

  // Fonction pour obtenir la couleur du statut
  const getStatusColor = (status) => {
    switch (status?.toLowerCase()) {
      case 'planifiée':
      case 'planifié':
        return '#3b82f6'; // bleu
      case 'en cours':
        return '#f59e0b'; // orange
      case 'livrée':
      case 'livre':
        return '#10b981'; // vert
      default:
        return '#6b7280'; // gris
    }
  };

  // Fonction pour mettre à jour le statut
  const handleUpdateStatut = async (livraison) => {
    try {
      const response = await livraisonAPI.updateStatutLivraison(livraison.livraisonId);
      
      // Si le statut est "En cours" et qu'on veut passer à "Livrée", ouvrir le modal de paiement
      if (response.action === 'confirmation_paiement') {
        // Débogage pour voir les valeurs exactes
        console.log('=== DÉBOGAGE MONTANT ===');
        console.log('Livraison montantTotal:', livraison.montantTotal);
        console.log('Type de montantTotal:', typeof livraison.montantTotal);
        console.log('MontantTotal stringifié:', JSON.stringify(livraison.montantTotal));
        
        // S'assurer que le montant est correctement converti
        let montantPaiement = 0;
        if (livraison.montantTotal !== null && livraison.montantTotal !== undefined) {
          if (typeof livraison.montantTotal === 'object') {
            // Si c'est un objet BigDecimal du backend
            montantPaiement = parseFloat(livraison.montantTotal.toString()) || 0;
          } else {
            montantPaiement = parseFloat(livraison.montantTotal) || 0;
          }
        }
        
        console.log('Montant final pour paiement:', montantPaiement);
        console.log('=== FIN DÉBOGAGE ===');
        
        setSelectedLivraison(livraison);
        setPaiementForm({
          montantPaiement: montantPaiement,
          methodePaiement: 'espèces',
          datePaiement: new Date().toISOString().split('T')[0]
        });
        setShowPaiementModal(true);
      } else {
        // Recharger les données
        await loadLivraisons();
        alert('Statut mis à jour avec succès !');
      }
    } catch (error) {
      console.error('Erreur lors de la mise à jour du statut:', error);
      alert('Erreur lors de la mise à jour du statut');
    }
  };

  // Fonction pour confirmer la livraison et le paiement
  const handleConfirmerLivraison = async () => {
    try {
      if (!paiementForm.montantPaiement || !paiementForm.methodePaiement || !paiementForm.datePaiement) {
        alert('Veuillez remplir tous les champs');
        return;
      }

      await livraisonAPI.confirmerLivraisonEtPaiement(selectedLivraison.livraisonId, {
        montantPaiement: parseFloat(paiementForm.montantPaiement),
        methodePaiement: paiementForm.methodePaiement,
        datePaiement: paiementForm.datePaiement
      });

      alert('Livraison confirmée et paiement enregistré avec succès !');
      setShowPaiementModal(false);
      setSelectedLivraison(null);
      await loadLivraisons();
    } catch (error) {
      console.error('Erreur lors de la confirmation:', error);
      alert('Erreur lors de la confirmation');
    }
  };

  // Fonction pour effacer tous les filtres
  const clearFilters = () => {
    setFilters({
      dateDebut: '',
      dateFin: '',
      statut: 'tous'
    });
    setSearchQuery('');
  };

  // Fonction pour vérifier si une date est dans la plage
  const isDateInRange = (dateString, dateDebut, dateFin) => {
    if (!dateString) return true;
    
    const date = new Date(dateString);
    const start = dateDebut ? new Date(dateDebut) : null;
    const end = dateFin ? new Date(dateFin) : null;
    
    if (start && date < start) return false;
    if (end && date > end) return false;
    
    return true;
  };

  // Filtrer les livraisons selon la recherche et les filtres
  const filteredLivraisons = livraisons.filter(livraison => {
    // Filtre par recherche textuelle
    const matchesSearch = 
      livraison.clientNom?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      livraison.zone?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      livraison.livreurNom?.toLowerCase().includes(searchQuery.toLowerCase());
    
    // Filtre par statut
    const matchesStatus = filters.statut === 'tous' || 
      livraison.statutLivraison?.toLowerCase().includes(filters.statut.toLowerCase());
    
    // Filtre par date (on utilise la date de livraison si disponible)
    const matchesDate = isDateInRange(livraison.dateLivraison, filters.dateDebut, filters.dateFin);
    
    return matchesSearch && matchesStatus && matchesDate;
  });

  // Calculer les statistiques
  const stats = {
    totalLivraisons: livraisons.length,
    livraisonsPlanifiees: livraisons.filter(l => l.statutLivraison?.toLowerCase().includes('planifié')).length,
    livraisonsEnCours: livraisons.filter(l => l.statutLivraison?.toLowerCase().includes('en cours')).length,
    livraisonsLivrees: livraisons.filter(l => l.statutLivraison?.toLowerCase().includes('livré')).length
  };

  if (loading) {
    return (
      <div className="dashboard-container">
        <div className="sidebar">
          <div className="sidebar-header">
            <h1 className="app-title">CheeseFlow</h1>
            <p className="app-subtitle">Production</p>
            <p className="app-subtitle">Gouda Artisanale</p>
          </div>
          <SidebarMenu />
        </div>
        <div className="main-content">
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <Loader className="animate-spin" size={48} />
            <span style={{ marginLeft: '10px' }}>Chargement des livraisons...</span>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard-container">
        <div className="sidebar">
          <div className="sidebar-header">
            <h1 className="app-title">CheeseFlow</h1>
            <p className="app-subtitle">Production</p>
            <p className="app-subtitle">Gouda Artisanale</p>
          </div>
          <SidebarMenu />
        </div>
        <div className="main-content">
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', flexDirection: 'column' }}>
            <AlertTriangle size={48} color="red" />
            <p style={{ marginTop: '10px', color: 'red' }}>{error}</p>
            <button onClick={loadLivraisons} style={{ marginTop: '10px', padding: '10px 20px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '5px' }}>
              Réessayer
            </button>
          </div>
        </div>
      </div>
    );
  }

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
            {filteredLivraisons.length === 0 ? (
              <div style={{ textAlign: 'center', padding: '40px', color: '#6b7280' }}>
                <Truck size={48} />
                <p>Aucune livraison trouvée</p>
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
                {/* Correction : afficher le montant total de la commande si disponible */}
                <p><strong>Montant total:</strong> {formatAmount(selectedLivraison.montantTotalCommande || selectedLivraison.montantTotal)}</p>
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