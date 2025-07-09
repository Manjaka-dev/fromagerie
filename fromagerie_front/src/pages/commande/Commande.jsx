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
import { commandeAPI, clientAPI, produitAPI, livreurAPI, livraisonAPI } from '../../services/api';

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

  // États pour les modales
  const [showAddModal, setShowAddModal] = useState(false);
  const [showOrderModal, setShowOrderModal] = useState(false);
  const [showLivraisonModal, setShowLivraisonModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);
  
  // Nouveau format pour les commandes
  const [newOrder, setNewOrder] = useState({
    clientId: '',
    date: new Date().toISOString().split('T')[0],
    produits: [] // Liste des produits sélectionnés
  });

  // État pour le formulaire de livraison
  const [livraisonForm, setLivraisonForm] = useState({
    livreurId: '',
    zone: '',
    dateLivraison: ''
  });

  // État pour la sélection de produits
  const [selectedProduits, setSelectedProduits] = useState([]);

  // Charger les données au montage du composant
  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      // Charger les commandes
      const commandesResponse = await commandeAPI.getAllCommandes();
      setCommandes(commandesResponse.commandes || []);

      // Charger les clients
      const clientsResponse = await clientAPI.getAllClients();
      setClients(clientsResponse || []);

      // Charger les produits
      const produitsResponse = await produitAPI.getAllProduits();
      setProduits(produitsResponse.produits || []);

      // Charger les livreurs
      const livreursResponse = await livreurAPI.getAllLivreurs();
      setLivreurs(livreursResponse.livreurs || []);
    } catch (err) {
      console.error('Erreur lors du chargement des données:', err);
      setError('Erreur lors du chargement des données');
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour obtenir le nom du client
  const getClientName = (clientId) => {
    if (!clientId) return 'Client inconnu';
    const client = clients.find(c => c.id === clientId);
    return client ? client.nom : 'Client inconnu';
  };

  // Fonction pour formater le montant
  const formatAmount = (amount) => {
    // Gérer les BigDecimal du backend
    let numericAmount = amount;
    if (typeof amount === 'object' && amount !== null) {
      // Si c'est un objet BigDecimal du backend
      numericAmount = parseFloat(amount.toString()) || 0;
    } else {
      numericAmount = parseFloat(amount) || 0;
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

  // Fonction pour effacer tous les filtres
  const clearFilters = () => {
    setFilters({
      dateDebut: '',
      dateFin: '',
      statut: 'tous',
      client: 'tous'
    });
    setSearchClient('');
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

  // Filtrer les commandes selon la recherche et les filtres
  const filteredCommandes = commandes.filter(commande => {
    // Filtre par recherche textuelle
    const matchesSearch = 
      getClientName(commande.clientId)?.toLowerCase().includes(searchClient.toLowerCase());
    
    // Filtre par statut
    const matchesStatus = filters.statut === 'tous' || 
      commande.statut?.toLowerCase().includes(filters.statut.toLowerCase());
    
    // Filtre par client
    const matchesClient = filters.client === 'tous' || 
      commande.clientId?.toString() === filters.client;
    
    // Filtre par date
    const matchesDate = isDateInRange(commande.dateCommande, filters.dateDebut, filters.dateFin);
    
    return matchesSearch && matchesStatus && matchesClient && matchesDate;
  });



  // Ajout de la fonction addProduitToOrder pour ajouter les produits sélectionnés à la commande
  const addProduitToOrder = () => {
    setNewOrder(prevOrder => {
      // Pour chaque produit sélectionné, on met à jour la quantité si déjà présent, sinon on l’ajoute
      const produitsMaj = [...prevOrder.produits];
      selectedProduits.forEach(selProd => {
        const idx = produitsMaj.findIndex(p => p.produitId === selProd.id);
        if (idx !== -1) {
          // Met à jour la quantité
          produitsMaj[idx].quantite = selProd.quantite;
        } else {
          produitsMaj.push({
            produitId: selProd.id,
            quantite: selProd.quantite || 1
          });
        }
      });
      return { ...prevOrder, produits: produitsMaj };
    });
    setSelectedProduits([]);
  };

  // Fonction pour supprimer un produit de la commande
  const removeProduitFromOrder = (index) => {
    setNewOrder(prev => ({
      ...prev,
      produits: prev.produits.filter((_, i) => i !== index)
    }));
  };

  // Fonction pour calculer le montant total de la commande
  const calculateOrderTotal = () => {
    return newOrder.produits.reduce((total, produitData) => {
      const produit = produits.find(p => p.id === produitData.produitId);
      if (produit) {
        // Gérer les BigDecimal du backend
        let prixProduit = produit.prixVente;
        if (typeof prixProduit === 'object' && prixProduit !== null) {
          prixProduit = parseFloat(prixProduit.toString()) || 0;
        } else {
          prixProduit = parseFloat(prixProduit) || 0;
        }
        return total + (prixProduit * produitData.quantite);
      }
      return total;
    }, 0);
  };

  const handleAddOrder = async () => {
    try {
      if (!newOrder.clientId) {
        alert('Veuillez sélectionner un client');
        return;
      }

      if (newOrder.produits.length === 0) {
        alert('Veuillez ajouter au moins un produit à la commande');
        return;
      }

      // Créer la commande avec les produits
      await commandeAPI.createCommande(
        newOrder.clientId,
        newOrder.date,
        newOrder.produits
      );
      
      // Recharger les données
      await loadData();
      
      // Fermer la modale et réinitialiser
      setShowAddModal(false);
      setNewOrder({
        clientId: '',
        date: new Date().toISOString().split('T')[0],
        produits: []
      });
      setSelectedProduits([]);
    } catch (error) {
      console.error('Erreur lors de la création de la commande:', error);
      alert('Erreur lors de la création de la commande: ' + error.message);
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

      // Préparer la liste des produits à livrer à partir de la commande sélectionnée
      const produitsALivrer = (selectedOrder.lignesCommande || []).map(lc => ({
        produitId: lc.produit.id,
        quantiteALivrer: lc.quantite
      }));

      await livraisonAPI.createLivraison({
        commandeId: selectedOrder.id,
        livreurId: livraisonForm.livreurId,
        zone: livraisonForm.zone,
        dateLivraison: livraisonForm.dateLivraison,
        produitsALivrer
      });
      
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
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <Loader className="animate-spin" size={48} />
            <span style={{ marginLeft: '10px' }}>Chargement des commandes...</span>
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
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', flexDirection: 'column' }}>
            <AlertTriangle size={48} color="red" />
            <p style={{ marginTop: '10px', color: 'red' }}>{error}</p>
            <button onClick={loadData} style={{ marginTop: '10px', padding: '10px 20px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '5px' }}>
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
              placeholder="Rechercher des commandes..."
              className={styles.searchInput}
              value={searchClient}
              onChange={(e) => setSearchClient(e.target.value)}
            />

            <Link to="/promotion" className={styles.ventes_promo_btn}>
              <FaTag className={styles.gestion_promotion} /> Gestion promotion
            </Link>

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

        <div className={styles.contenus}>
          <div className={styles.dashboardContainer}>
            {/* Bouton d'ajout de commande */}
            <div className={styles.addOrderSection}>
              <button
                className={styles.addOrderButton}
                onClick={() => setShowAddModal(true)}
              >
                <Plus className={styles.addIcon} />
                Nouvelle Commande
              </button>
            </div>

            {/* Section filtres */}
            <div className={styles.filtersSection}>
              <div className={styles.filterGroup}>
                <label>Date de début</label>
                <input
                  type="date"
                  value={filters.dateDebut}
                  onChange={(e) => setFilters({ ...filters, dateDebut: e.target.value })}
                />
              </div>
              
              <div className={styles.filterGroup}>
                <label>Date de fin</label>
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
                  <option value="tous">Tous les statuts</option>
                  <option value="en attente">En attente</option>
                  <option value="confirmée">Confirmée</option>
                  <option value="annulée">Annulée</option>
                </select>
              </div>
              
              <div className={styles.filterGroup}>
                <label>Client</label>
                <select
                  value={filters.client}
                  onChange={(e) => setFilters({ ...filters, client: e.target.value })}
                >
                  <option value="tous">Tous les clients</option>
                  {clients.map(client => (
                    <option key={client.id} value={client.id}>
                      {client.nom}
                    </option>
                  ))}
                </select>
              </div>
              
              <button className={styles.clearFiltersBtn} onClick={clearFilters}>
                Effacer les filtres
              </button>
            </div>

            {/* Liste des commandes */}
            <div className={styles.commandesList}>
              <h2 className={styles.listTitle}>Liste des Commandes</h2>
              
              {filteredCommandes.length === 0 ? (
                <div className={styles.emptyState}>
                  <Package size={48} color="#9ca3af" />
                  <p>Aucune commande trouvée</p>
                  <button
                    className={styles.addOrderButton}
                    onClick={() => setShowAddModal(true)}
                  >
                    <Plus className={styles.addIcon} />
                    Créer la première commande
                  </button>
                </div>
              ) : (
                <div className={styles.commandesGrid}>
                  {filteredCommandes.map((commande, index) => (
                    <div 
                      key={index} 
                      className={styles.commandeCard}
                      onClick={() => handleOpenLivraisonModal(commande)}
                      style={{ cursor: 'pointer' }}
                    >
                                          <div className={styles.commandeHeader}>
                                              <div className={styles.clientInfo}>
                          <h3 className={styles.clientName}>{commande.client?.nom || 'Client inconnu'}</h3>
                        </div>
                        <div className={styles.commandeActions}>
                          <button
                            className={styles.cancelButton}
                            onClick={() => handleCancelOrder(commande.id)}
                          >
                            Annuler
                          </button>
                          <button
                            className={styles.deleteButton}
                            onClick={() => handleDeleteOrder(commande.id)}
                          >
                            <Trash2 size={16} />
                          </button>
                        </div>
                      </div>

                      <div className={styles.commandeDetails}>
                        <div className={styles.detailRow}>
                          <Wallet className={styles.detailIcon} />
                          <span className={styles.detailLabel}>Prix total:</span>
                          <span className={styles.detailValue}>{formatAmount(commande.montantTotal || 0)}</span>
                        </div>

                        <div className={styles.detailRow}>
                          <Calendar className={styles.detailIcon} />
                          <span className={styles.detailLabel}>Date:</span>
                          <span className={styles.detailValue}>{formatDate(commande.dateCommande)}</span>
                        </div>

                        <div className={styles.detailRow}>
                          <User className={styles.detailIcon} />
                          <span className={styles.detailLabel}>Contact:</span>
                          <span className={styles.detailValue}>{commande.client?.telephone || 'Non défini'}</span>
                        </div>
                      </div>

                      {/* Liste des produits */}
                      <div className={styles.produitsSection}>
                        <h4 className={styles.produitsTitle}>Produits commandés:</h4>
                        {commande.lignesCommande && commande.lignesCommande.length > 0 ? (
                          <div className={styles.produitsList}>
                            {commande.lignesCommande.map((ligne, ligneIndex) => (
                              <div key={ligneIndex} className={styles.produitItem}>
                                <span className={styles.produitName}>
                                  {ligne.produit?.nom || 'Produit inconnu'}
                                </span>
                                <span className={styles.produitQuantity}>
                                  Quantité: {ligne.quantite}
                                </span>
                                <span className={styles.produitPrice}>
                                  {formatAmount(ligne.prixUnitaire || 0)}
                                </span>
                              </div>
                            ))}
                          </div>
                        ) : (
                          <p className={styles.noProduits}>Aucun produit dans cette commande</p>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Modale d'ajout de commande */}
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
                    {/* Informations de base */}
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
                            {client.nom} - {client.telephone}
                          </option>
                        ))}
                      </select>
                    </div>



                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Date</label>
                      <input
                        type="date"
                        className={styles.formInputDate}
                        value={newOrder.date}
                        onChange={(e) => setNewOrder({ ...newOrder, date: e.target.value })}
                      />
                    </div>

                    {/* Section sélection de produits */}
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Sélectionner des produits</label>
                      <div className={styles.productSelection}>
                        {produits.map(produit => (
                          <div key={produit.id} className={styles.productOption}>
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
                              {produit.nom} - {formatAmount(produit.prixVente || 0)}
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
                                <span>Prix: {formatAmount((produit?.prixVente || 0) * produitData.quantite)}</span>
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
                          <strong>Total: {formatAmount(calculateOrderTotal())}</strong>
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
                        <p><strong>Montant:</strong> {formatAmount(selectedOrder.montantTotal || 0)}</p>
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