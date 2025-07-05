import React, { useState } from 'react';
import '../assets/styles/Promotions.css';
import { NavLink , Link} from 'react-router-dom';
import { FaTag } from 'react-icons/fa';
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
  AlertTriangle
} from 'lucide-react';

const Promotions = () => {
  // Données des promotions
  const promotions = [
    {
      id: 1,
      nom: "Pack Famille",
      description: "Réduction sur l'achat de 3 fromages Couda ou plus",
      reduction: "25 %",
      periode: "Du : 01/06/2025 au 01/07/2025"
    },
    {
      id: 2,
      nom: "Clients fidèl0es",
      description: "Remise exclusive pour nos clients réguliers",
      reduction: "25 %",
      periode: "Du : 01/06/2025 au 01/07/2025"
    },
    {
      id: 3,
      nom: "Offres été Couda",
      description: "Promotion spéciale pour les fromages Couda artisanaux pendant la saison estivale",
      reduction: "25 %",
      periode: "Du : 01/06/2025 au 01/07/2025"
    }
  ];


  // -----------------RECHERCHE-------------------------------
  const [searchQuery, setSearchQuery] = useState('');

  // Statistiques
  const stats = {
    promotionsActives: 2,
    reductionMoyenne: "15 %",
    economiesClients: "2 500 Ar",
    promotionsExpirees: 1
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
        <SidebarMenu />
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
            <Link to="/commandes" className="ventes_promo_btn">
                <FaTag className="gestion_promotion"/> Gestion Commandes
            </Link>
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

        <div className="promotion-container">
          <h1>GESTION PROMOTION</h1>

          {/* Section Statistiques */}
          <div className="stats-grid">
            <div className="stat-card">
              <h3>Promotions Actives</h3>
              <p className="stat-value">{stats.promotionsActives}</p>
            </div>

            <div className="stat-card">
              <h3>Réduction Moyenne</h3>
              <p className="stat-value">{stats.reductionMoyenne}</p>
            </div>

            <div className="stat-card">
              <h3>Economies Clients</h3>
              <p className="stat-value">{stats.economiesClients}</p>
            </div>

            <div className="stat-card">
              <h3>Promotions exprimées</h3>
              <p className="stat-value">{stats.promotionsExpirees}</p>
            </div>
          </div>

          <div className="divider"></div>

          {/* Liste des promotions */}
          <h2>Listes des promotions :</h2>

          <div className="promotion-list">
            {promotions.map(promo => (
              <div key={promo.id} className="promotion-card">
                <div className="promotion-header">
                  <h3>{promo.nom}</h3>
                  <span className="reduction-badge">{promo.reduction}</span>
                </div>
                <p className="promotion-description">{promo.description}</p>
                <p className="promotion-period">{promo.periode}</p>
                <div className="promotion-actions">
                  <button className="edit-btn">Modifier</button>
                  <button className="delete-btn">Supprimer</button>
                </div>
              </div>
            ))}
          </div>
        </div>

      </div>


    </div>
  );
};

export default Promotions;