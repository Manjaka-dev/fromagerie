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
    statut: 'en attente',
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
      // Charger les commandes depuis l'API
      const commandesResponse = await commandeAPI.getAllCommandes();
      setCommandes(commandesResponse.commandes || []);

      // Charger les clients depuis l'API
      const clientsResponse = await clientAPI.getAllClients();
      setClients(clientsResponse || []);

      // Charger les produits depuis l'API
      const produitsResponse = await produitAPI.getAllProduits();
      setProduits(produitsResponse.produits || []);

      // Charger les livreurs depuis l'API
      const livreursResponse = await livreurAPI.getAllLivreurs();
      setLivreurs(livreursResponse || []);

    } catch (err) {
      console.error('Erreur lors du chargement des données:', err);
      setError('Impossible de charger les données. Veuillez vérifier que le backend est démarré.');
    } finally {
      setLoading(false);
    }
  };



  // Fonction pour filtrer les commandes
  const filteredCommandes = commandes.filter(commande => {
    const matchSearch = commande.clientNom?.toLowerCase().includes(searchClient.toLowerCase()) || false;
    const matchDateDebut = !filters.dateDebut || commande.date >= filters.dateDebut;
    const matchDateFin = !filters.dateFin || commande.date <= filters.dateFin;
    const matchStatut = filters.statut === 'tous' || commande.statut === filters.statut;
    const matchClient = filters.client === 'tous' || commande.clientId.toString() === filters.client;

    return matchSearch && matchDateDebut && matchDateFin && matchStatut && matchClient;
  });

  // Fonction pour créer une nouvelle commande
  const handleCreateCommande = async () => {
    try {
      const commandeData = {
        ...newOrder,
        produits: selectedProduits
      };

      try {
        await commandeAPI.createCommande(commandeData);
        await loadData(); // Recharger les données
        
        // Réinitialiser le formulaire
        setNewOrder({
          clientId: '',
          date: new Date().toISOString().split('T')[0],
          statut: 'en attente',
          produits: []
        });
        setSelectedProduits([]);
        setShowAddModal(false);
      } catch (err) {
        throw err; // Propager l'erreur pour la gestion globale
      }
    } catch (err) {
      console.error('Erreur lors de la création de la commande:', err);
      setError('Erreur lors de la création de la commande');
    }
  };

  // Fonction pour créer une livraison
  const handleCreateLivraison = async () => {
    try {
      const livraisonData = {
        commandeId: selectedOrder.id,
        livreurId: livraisonForm.livreurId,
        zone: livraisonForm.zone,
        dateLivraison: livraisonForm.dateLivraison,
        statut: 'planifiée'
      };

      try {
        await livraisonAPI.createLivraison(livraisonData);
        
        // Réinitialiser le formulaire
        setLivraisonForm({
          livreurId: '',
          zone: '',
          dateLivraison: ''
        });
        setShowLivraisonModal(false);
        setSelectedOrder(null);
      } catch (err) {
        throw err; // Propager l'erreur pour la gestion globale
      }
    } catch (err) {
      console.error('Erreur lors de la création de la livraison:', err);
    }
  };

  const [showFilters, setShowFilters] = useState(false);
  const [searchDate, setSearchDate] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');

  const statusCards = [
    {
      title: 'CA en cours',
      amount: '$4,156.45',
      icon: Clock,
      className: 'statusCardOrange'
    },
    {
      title: 'En attente',
      amount: '$4,156.45',
      icon: Truck,
      className: 'statusCardRed'
    },
    {
      title: 'En préparation',
      amount: '$3,146.45',
      icon: Package,
      className: 'statusCardBlue'
    },
    {
      title: 'Livrés ce mois',
      amount: '$1,146.45',
      icon: CheckCircle,
      className: 'statusCardGreen'
    }
  ];


  const orderSections = [
    {
      title: 'Confirmé',
      count: 2,
      orders: [
        {
          client: 'Fromagerie Martin',
          status: 'haute',
          amount: '200 000 Ar',
          contact: 'Contact: M.Martin',
          details: 'Gouda offre - 50 kg',
          date: '2025-06-05'
        },
        {
          client: 'Fromagerie Martin',
          status: 'basse', // 'normale', 'haute', 'basse'
          amount: '200 000 Ar',
          contact: 'Contact: M.Martin',
          details: 'Gouda offre - 50 kg',
          date: '2025-06-05'
        }
      ]
    },
    {
      title: 'Préparation',
      count: 2,
      orders: [
        {
          client: 'Fromagerie Martin',
          status: 'normale', // 'normale', 'haute', 'basse'
          amount: '200 000 Ar',
          contact: 'Contact: M.Martin',
          details: 'Gouda offre - 50 kg',
          date: '2025-06-05'
        },
        {
          client: 'Fromagerie Martin',
          status: 'haute', // 'normale', 'haute', 'basse'
          amount: '200 000 Ar',
          contact: 'Contact: M.Martin',
          details: 'Gouda offre - 50 kg',
          date: '2025-06-05'
        }
      ]
    },
    {
      title: 'Livré',
      count: 2,
      orders: [
        {
          client: 'Fromagerie Martin',
          status: 'haute', // 'normale', 'haute', 'basse'
          amount: '200 000 Ar',
          contact: 'Contact: M.Martin',
          details: 'Gouda offre - 50 kg',
          date: '2025-06-05'
        },
        {
          client: 'Fromagerie Martin',
          status: 'haute', // 'normale', 'haute', 'basse'
          amount: '200 000 Ar',
          contact: 'Contact: M.Martin',
          details: 'Gouda offre - 50 kg',
          date: '2025-06-05'
        }
      ]
    },
    {
      title: 'Devis',
      count: 2,
      orders: [
        {
          client: 'Fromagerie Martin',
          status: 'basse', // 'normale', 'haute', 'basse'
          amount: '200 000 Ar',
          contact: 'Contact: M.Martin',
          details: 'Gouda offre - 50 kg',
          date: '2025-06-05'
        },
        {
          client: 'Fromagerie Martin',
          status: 'basse', // 'normale', 'haute', 'basse'
          amount: '200 000 Ar',
          contact: 'Contact: M.Martin',
          details: 'Gouda offre - 50 kg',
          date: '2025-06-05'
        }
      ]
    }
  ];

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
                • Mise à jour: 20 Juin 2025, 09:15
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
            {/* Status Cards - 4 cartes en haut */}
            <div className={styles.statusCardsGrid}>
              {statusCards.map((card, index) => {
                const IconComponent = card.icon;
                return (
                  <div key={index} className={`${styles.statusCard} ${styles[card.className]}`}>
                    <div className={styles.statusCardHeader}>
                      <div className={`${styles.statusIconContainer} ${styles[`${card.className}StatusIconContainer`]}`}>
                        <IconComponent className={`${styles.statusIconHaute} ${styles[`${card.className}StatusIcon`]}`} />
                      </div>
                    </div>
                    <div className={styles.contenusLeft}>
                      <h3 className={styles.statusCardTitle}>{card.title}</h3>
                      <p className={styles.statusCardAmount}>{card.amount}</p>
                    </div>
                  </div>
                );
              })}
            </div>
            <div className={styles.ordersGrid}>
              {orderSections.map((section, sectionIndex) => (
                <div key={sectionIndex} className={styles.orderSection}>
                  {/* En-tête de section avec titre et compteur */}
                  <div className={styles.sectionHeader}>
                    <h2 className={styles.sectionTitle}>{section.title}</h2>
                    <span className={styles.sectionCount}>{section.count}</span>
                  </div>

                  {/* Liste des commandes */}
                  <div className={styles.ordersList}>
                    {section.orders.map((order, orderIndex) => (

                      <div key={orderIndex} className={styles.orderCard} onClick={() => handleOrderClick(order)}>
                        <div className={styles.orderHeader}>
                          <div className={styles.orderClientInfo}>
                            <h3 className={styles.orderClient}>{order.client}</h3>
                            <span className={`${styles.statusBadge} ${styles[`status${order.status.toLowerCase()}`]}`}>
                              {order.status === 'haute' && <AlertTriangle size={2} className={styles.statusIcon} />}
                              {order.status === 'normale' && <CheckCircle size={10} className={styles.statusIcon} />}
                              {order.status === 'basse' && <ArrowDown size={10} className={styles.statusIcon} />}
                              {order.status}
                            </span>
                          </div>
                        </div>

                        <div className={styles.orderDetails}>
                          <div className={styles.detailItem}>
                            <Wallet className={styles.detailIcon} />
                            <p className={styles.orderAmount}>{order.amount}</p>
                          </div>

                          <div className={styles.detailItem}>
                            <User className={styles.detailIcon} />
                            <p className={styles.orderContact}>{order.contact}</p>
                          </div>

                          <div className={styles.detailItem}>
                            <ListChecks className={styles.detailIcon} />
                            <p className={styles.orderDescription}>{order.details}</p>
                          </div>
                        </div>

                        <div className={styles.orderDate}>
                          <svg className={styles.calendarIcon} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                            <line x1="16" y1="2" x2="16" y2="6"></line>
                            <line x1="8" y1="2" x2="8" y2="6"></line>
                            <line x1="3" y1="10" x2="21" y2="10"></line>
                          </svg>
                          {order.date}
                        </div>
                      </div>
                    ))}
                  </div>


                  <div className={styles.sectionFooter}>
                    <button
                      className={styles.addButton}
                      onClick={() => setShowAddModal(true)}
                    >
                      <Plus className={styles.addIcon} />
                      Ajouter
                    </button>
                  </div>

                </div>
              ))}
            </div>

            {/* Details commandes */}
            {showOrderModal && selectedOrder && (
              <div className={styles.modalOverlay}>
                <div className={styles.orderModal}>
                  <button onClick={closeModal}>
                    <X size={15} className={styles.closeButton} />
                  </button>

                  <div className={styles.modalHeader}>
                    <h2 className={styles.modalTitle}>Détails de la commande</h2>
                    <span className={`${styles.orderBadge} ${styles[`badge${selectedOrder.status.toLowerCase()}`]}`}>
                      {selectedOrder.status}
                    </span>
                  </div>

                  <div className={styles.modalContent}>
                    <div className={styles.modalSection}>
                      <h3 className={styles.sectionTitle}>Client</h3>
                      <p className={styles.sectionText}>{selectedOrder.client}</p>
                      <p className={styles.sectionText}>{selectedOrder.contact}</p>
                    </div>

                    <div className={styles.modalSection}>
                      <h3 className={styles.sectionTitle}>Détails</h3>
                      <p className={styles.sectionText}>{selectedOrder.details}</p>
                    </div>

                    <div className={styles.modalSection}>
                      <h3 className={styles.sectionTitle}>Montant</h3>
                      <p className={styles.sectionText}>{selectedOrder.amount}</p>
                    </div>

                    <div className={styles.modalSection}>
                      <h3 className={styles.sectionTitle}>Date</h3>
                      <p className={styles.sectionText}>{selectedOrder.date}</p>
                    </div>
                  </div>

                  <div className={styles.modalActions}>
                    <button className={`${styles.actionButton} ${styles.cancelButton}`}>
                      Annuler
                    </button>
                    <button className={`${styles.actionButton} ${styles.editButton}`}>
                      Modifier
                    </button>
                    <button className={`${styles.actionButton} ${styles.confirmButton}`}>
                      Confirmer
                    </button>
                  </div>
                </div>
              </div>
            )}

            {/* Ajouter commande  */}
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
                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Nom du client</label>
                      <input
                        type="text"
                        className={styles.formInput}
                        value={newOrder.client}
                        onChange={(e) => setNewOrder({ ...newOrder, client: e.target.value })}
                      />
                    </div>

                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Contact</label>
                      <input
                        type="text"
                        className={styles.formInput}
                        value={newOrder.contact}
                        onChange={(e) => setNewOrder({ ...newOrder, contact: e.target.value })}
                      />
                    </div>

                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Détails</label>
                      <input
                        className={styles.formInput}
                        value={newOrder.details}
                        onChange={(e) => setNewOrder({ ...newOrder, details: e.target.value })}
                      />
                    </div>

                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Montant</label>
                      <input
                        type="text"
                        className={styles.formInput}
                        value={newOrder.amount}
                        onChange={(e) => setNewOrder({ ...newOrder, amount: e.target.value })}
                      />
                    </div>

                    <div className={styles.formGroup}>
                      <label className={styles.formLabel}>Statut</label>
                      <select
                        className={styles.formSelect}
                        value={newOrder.status}
                        onChange={(e) => setNewOrder({ ...newOrder, status: e.target.value })}
                      >
                        <option value="basse">Basse</option>
                        <option value="normale">Normale</option>
                        <option value="haute">Haute</option>
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
                    >
                      Enregistrer
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