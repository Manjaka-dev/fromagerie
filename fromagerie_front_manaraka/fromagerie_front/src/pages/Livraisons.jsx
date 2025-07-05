import React, { useState } from 'react';
import '../assets/styles/Livraison.css';
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
  AlertTriangle
} from 'lucide-react';
import { NavLink } from 'react-router-dom';
const Livraison = () => {

  const menuItems = [
    { name: 'Tableau de Bord', icon: BarChart3, active: true, path: '/TableauDeBord' },
    { name: 'Stock', icon: Package, path: '/stock' },
    { name: 'Statistiques', icon: TrendingUp, path: '/statistiques' },
    { name: 'Comptabilité', icon: Calculator, path: '/comptabilite' },
    { name: 'Ventes', icon: ShoppingCart, path: '/ventes' },
    { name: 'Commandes', icon: ShoppingCart, path: '/commandes' },
    { name: 'Livraisons', icon: Truck, path: '/livraisons' },
    { name: 'Administration', icon: Settings, path: '/administration' },
    { name: 'Production', icon: Factory, path: '/production' }
  ];

  // -----------------RECHERCHE-------------------------------
  const [searchQuery, setSearchQuery] = useState('');

  // Données des livraisons
  const livraisons = [
    {
      id: 1,
      fromagerie: "Fromagerie Martin",
      client: "Anosibe.PIZZAMANIA",
      montant: "200 000 Ar",
      produits: "Couda affiné : 50 kg",
      livreur: "Jean Dupont",
      horaire: "10:30 - 11:45"
    },
    {
      id: 2,
      fromagerie: "Fromagerie Martin",
      client: "Anosibe.PIZZAMANIA",
      montant: "200 000 Ar",
      produits: "Couda affiné : 50 kg",
      livreur: "Jean Dupont",
      horaire: "10:30 - 11:45"
    },
    {
      id: 3,
      fromagerie: "Fromagerie Martin",
      client: "Anosibe.PIZZAMANIA",
      montant: "200 000 Ar",
      produits: "Couda affiné : 50 kg",
      livreur: "Jean Dupont",
      horaire: "10:30 - 11:45"
    }
  ];

  // Statistiques
  const stats = {
    livraisonsDuJour: 8,
    livreursActifs: 2,
    distanceOptimisee: "45.2 km",
    tempsEstime: "2h 30min"
  };

  return (
    <div className="dashboard-container">

      {/* Sidebar */}
      <div className="sidebar">
        <div className="sidebar-header">
          <h1 className="app-title">CheeseFlow</h1>
          <p className="app-subtitle">Production</p>
          <p className="app-subtitle">Gouda Artisanale</p>
        </div>

        <nav className="sidebar-nav">
          {menuItems.map((item) => (
            <NavLink
              key={item.name}
              to={item.path}
              className={({ isActive }) =>
                `menu-item ${isActive ? 'menu-item-active' : ''}`
              }
            >
              <item.icon className="menu-icon" />
              <span className="menu-text">{item.name}</span>
            </NavLink>
          ))}
        </nav>
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
            {livraisons.map(livraison => (
              <div key={livraison.id} className="livraison-card">
                <div className="livraison-header">
                  <h3>{livraison.fromagerie}</h3>
                  <span className="client-name">{livraison.client}</span>
                </div>
                <div className="livraison-details">
                  <p><strong>Montant :</strong> {livraison.montant}</p>
                  <p><strong>Produits :</strong> {livraison.produits}</p>
                  <p><strong>Livreur :</strong> {livraison.livreur}</p>
                  <p><strong>Prévu :</strong> {livraison.horaire}</p>
                </div>
                <div className="livraison-actions">
                  <button className="track-btn">Suivre</button>
                  <button className="contact-btn">Contacter</button>
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