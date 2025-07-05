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
  AlertTriangle
} from 'lucide-react';
import { NavLink } from 'react-router-dom';
const Livraison = () => {

  // -----------------RECHERCHE-------------------------------
  const [searchQuery, setSearchQuery] = useState('');
  const [livraisons, setLivraisons] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const zone = searchQuery.trim();

    const endpoint = zone
      ? `http://localhost:8080/api/livraisons/zone/${encodeURIComponent(zone)}`
      : 'http://localhost:8080/api/livraisons';

    setLoading(true);
    fetch(endpoint)
      .then((res) => {
        if (!res.ok) {
          throw new Error("Erreur lors du chargement des livraisons");
        }
        return res.json();
      })
      .then((data) => {
        setLivraisons(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Erreur API :", err);
        setLoading(false);
      });
  }, [searchQuery]);
  // Données des livraison

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
              <input
                type="text"
                placeholder="Rechercher une zone..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>

            <div className="livreurs-stat">
              <p className="livreurs-count">{stats.livreursActifs}</p>
              <p className="livreurs-label">Livreurs actifs</p>
            </div>
          </div>

          {/* Liste des livraisons */}
          <div className="livraison-list">
            {loading ? (
              <p>Chargement des livraisons...</p>
            ) : livraisons.length === 0 ? (
              <p>Aucune livraison disponible.</p>
            ) : (
              livraisons.map((livraison) => (
                <div key={livraison.livraisonId} className="livraison-card">
                  <div className="livraison-header">
                    <h3>Zone : {livraison.zone}</h3>
                    <span className="client-name">Client : {livraison.nomClient}</span>
                  </div>

                  <div className="livraison-details">
                    <p><strong>Date Livraison :</strong> {livraison.dateLivraison}</p>
                    <p><strong>Statut :</strong> {livraison.statut}</p>
                    <p><strong>Commande n° :</strong> {livraison.commandeId} (le {livraison.dateCommande})</p>
                    <p><strong>Adresse :</strong> {livraison.adresseClient}</p>
                    <p><strong>Tél. Client :</strong> {livraison.telephoneClient}</p>
                    <p><strong>Livreur :</strong> {livraison.nomLivreur} ({livraison.telephoneLivreur})</p>
                  </div>

                  <div className="produits-livraison">
                    <h4>Produits à livrer :</h4>
                    <ul>
                      {livraison.produitsALivrer.map((produit, index) => (
                        <li key={index}>
                          {produit.nomProduit} - {produit.quantiteALivrer} u (Livrés: {produit.quantiteLivree}) - Catégorie : {produit.categorieProduit}
                        </li>
                      ))}
                    </ul>
                  </div>

                  <div className="livraison-actions">
                    <button className="track-btn">Suivre</button>
                    <button className="contact-btn">Contacter</button>
                  </div>
                </div>
              ))
            )}
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