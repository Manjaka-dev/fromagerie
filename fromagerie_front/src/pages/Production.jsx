import React, { useState } from 'react';
import '../assets/styles/production.css';
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

const Production = () => {


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

  const [product, setProduct] = useState({
    category: '',
    name: '',
    weight: '',
    sellingPrice: '',
    costPrice: '',
    ingredients: '',
    allergens: '',
    expirationDate: ''
  });

  const recentProductions = [
    {
      category: 'Nom Catégorie',
      name: 'Nom du produit',
      weight: 'Poids',
      costPrice: '5 000 A+',
      ingredients: 'sal, Yocurt, ...',
      sellingPrice: '28 000 A+'
    },
    {
      category: 'Nom Catégorie',
      name: 'Nom du produit',
      weight: 'Poids',
      costPrice: '5 000 A+',
      ingredients: 'sal, Yocurt, ...',
      sellingPrice: '28 000 A+'
    },
    {
      category: 'Nom Catégorie',
      name: 'Nom du produit',
      weight: 'Poids',
      costPrice: '5 000 A+',
      ingredients: 'sal, Yocurt, ...',
      sellingPrice: '28 000 A+'
    }
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProduct(prev => ({
      ...prev,
      [name]: value
    }));
  };


  const handleSave = () => {
    // Logique pour sauvegarder le produit
    console.log('Produit sauvegardé:', product);
    alert('Produit sauvegardé avec succès!');
  };

  const handleCancel = () => {
    // Réinitialiser le formulaire
    setProduct({
      category: '',
      name: '',
      weight: '',
      sellingPrice: '',
      costPrice: '',
      ingredients: '',
      allergens: '',
      expirationDate: ''
    });
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
              className="search-input"
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

        <div className="production-page">
          <h1>Informations du produit</h1>

          <div className="product-form">
            <div className="form-section">
              <h2>Catégorie</h2>
              <select
                name="category"
                value={product.category}
                onChange={handleChange}
                className="form-input"
              >
                <option value="">Sélectionner une catégorie --</option>
                {/* Add actual category options here */}
              </select>

              <input
                type="text"
                name="name"
                value={product.name}
                onChange={handleChange}
                placeholder="Nom du produit"
                className="form-input"
              />
              <span className="example">Ex : Goudra anticanci primium</span>
            </div>

            <div className="form-section">
              <h2>Poids (g)</h2>
              <input
                type="text"
                name="weight"
                value={product.weight}
                onChange={handleChange}
                placeholder="B00"
                className="form-input"
              />

              <h2>Prix de vente (A4)</h2>
              <input
                type="text"
                name="sellingPrice"
                value={product.sellingPrice}
                onChange={handleChange}
                placeholder="26 000"
                className="form-input"
              />
            </div>

            <div className="form-section">
              <h2>Prix de revient (A4)</h2>
              <input
                type="text"
                name="costPrice"
                value={product.costPrice}
                onChange={handleChange}
                placeholder="8 000"
                className="form-input"
              />
            </div>

            <div className="form-section">
              <h2>Ingredients</h2>
              <textarea
                name="ingredients"
                value={product.ingredients}
                onChange={handleChange}
                placeholder="Séi, prélaises, fermant yacuit ..."
                className="form-input"
              />
            </div>

            <div className="form-section">
              <h2>Allergènes</h2>
              <select
                name="allergens"
                value={product.allergens}
                onChange={handleChange}
                className="form-input"
              >
                <option value="">Sélectionner les allergènes --</option>
                {/* Add actual allergen options here */}
              </select>
            </div>

            <div className="form-section">
              <h2>Date de préemption</h2>
              <input
                type="date"
                name="expirationDate"
                value={product.expirationDate}
                onChange={handleChange}
                className="form-input"
              />
            </div>
          </div>
          <div>
            <div className="form-actions">
              <button className="cancel-btn" onClick={handleCancel}>
                Annuler
              </button>
              <button className="save-btn" onClick={handleSave}>
                Sauvegarder
              </button>
            </div>
          </div>
          <hr />

          <div className="recent-productions">
            <h3>Productions Récentes</h3>
            <table>
              <tbody>
                {recentProductions.map((prod, index) => (
                  <React.Fragment key={index}>
                    <tr>
                      <td><strong>{prod.category}</strong></td>
                      <td>{prod.name}</td>
                    </tr>
                    <tr>
                      <td>{prod.weight}</td>
                      <td>Prix de revient : {prod.costPrice}</td>
                    </tr>
                    <tr>
                      <td>Ingredients : {prod.ingredients}</td>
                      <td>Prix de vente : {prod.sellingPrice}</td>
                    </tr>
                  </React.Fragment>
                ))}
              </tbody>
            </table>
          </div>

          <hr />

          <div className="production-tips">
            <h3>Conseils Production</h3>
            <ul>
              <li>Respectez les temps d'offrinage pour chaque catégorie</li>
              <li>Veillre la température de stockage</li>
              <li>Documents chaque étage de production</li>
              <li>Contrôlez régulièrement la qualité</li>
            </ul>
          </div>
        </div>

      </div>



    </div>
  );
};

export default Production;