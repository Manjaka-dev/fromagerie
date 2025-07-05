import React, { useState } from 'react';
import '../assets/styles/production.css';
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

const Production = () => {

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
    console.log('Produit sauvegardé:', product);
    alert('Produit sauvegardé avec succès!');
  };

  const handleCancel = () => {
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
            <input
              type="text"
              placeholder="Rechercher des produits, références..."
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

        <div className="production-page">
          <div className="production-container">
            {/* Formulaire unifié */}
            <div className="form-container">
              <h1 className="production-title">Nouvelle Production</h1>
              
              <div className="form-grid">
              <div className="form-group">
                  <label>Catégorie</label>
                  <select
                    name="category"
                    value={product.category}
                    onChange={handleChange}
                    className="formInputText"
                  >
                    <option value="">Sélectionner une catégorie</option>
                  </select>
              </div>

                <div className="form-group">
                  <label>Nom du produit</label>
                  <input
                    type="text"
                    name="name"
                    value={product.name}
                    onChange={handleChange}
                    placeholder="Ex : Goudra anticanci primium"
                    className="formInputText"
                  />
                </div>

                <div className="form-group">
                  <label>Poids (g)</label>
                  <input
                    type="text"
                    name="weight"
                    value={product.weight}
                    onChange={handleChange}
                    placeholder="B00"
                    className="formInputText"
                  />
                </div>

                <div className="form-group">
                  <label>Prix de vente (A4)</label>
                  <input
                    type="text"
                    name="sellingPrice"
                    value={product.sellingPrice}
                    onChange={handleChange}
                    placeholder="26 000"
                    className="formInputText"
                  />
                </div>

                <div className="form-group">
                  <label>Prix de revient (A4)</label>
                  <input
                    type="text"
                    name="costPrice"
                    value={product.costPrice}
                    onChange={handleChange}
                    placeholder="8 000"
                    className="formInputText"
                  />
                </div>

                <div className="form-group">
                  <label>Date de pérémption</label>
                  <input
                    type="date"
                    name="expirationDate"
                    value={product.expirationDate}
                    onChange={handleChange}
                    className="formInputText"
                  />
                </div>

                <div className="form-group full-width">
                  <label>Ingredients</label>
                  <textarea
                    name="ingredients"
                    value={product.ingredients}
                    onChange={handleChange}
                    placeholder="Séi, présures, fermant yaourt ..."
                    className="formInputText"
                    rows="3"
                  />
                </div>

                <div className="form-group">
                  <label>Allergènes</label>
                  <select
                    name="allergens"
                    value={product.allergens}
                    onChange={handleChange}
                    className="formInputText"
                  >
                    <option value="">Sélectionner les allergènes</option>
                  </select>
                </div>
              </div>

              <div className="form-actions">
                <button className="cancel-btn" onClick={handleCancel}>
                  Annuler
                </button>
                <button className="save-btn" onClick={handleSave}>
                  Sauvegarder
                </button>
              </div>
            </div>

            {/* Liste des productions récentes */}
            <div className="recent-productions">
              <h2 className="section-title">Productions Récentes</h2>
              <div className="production-list">
                {recentProductions.map((prod, index) => (
                  <div key={index} className="production-item">
                    <div className="production-header">
                      <span className="production-category">{prod.category}</span>
                      <span className="production-name">{prod.name}</span>
                    </div>
                    <div className="production-details">
                      <div className="detail-group">
                        <span className="detail-label">Poids:</span>
                        <span>{prod.weight}</span>
                      </div>
                      <div className="detail-group">
                        <span className="detail-label">Prix revient:</span>
                        <span>{prod.costPrice}</span>
                      </div>
                      <div className="detail-group">
                        <span className="detail-label">Ingredients:</span>
                        <span>{prod.ingredients}</span>
                      </div>
                      <div className="detail-group">
                        <span className="detail-label">Prix vente:</span>
                        <span>{prod.sellingPrice}</span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Conseils de production */}
            <div className="production-tips">
              <h2 className="section-title">Conseils de Production</h2>
              <div className="tips-container">
                <div className="tip-card">
                  <Factory className="tip-icon" />
                  <p>Respectez les temps d'affinage pour chaque catégorie</p>
                </div>
                <div className="tip-card">
                  <AlertTriangle className="tip-icon" />
                  <p>Veillez à la température de stockage</p>
                </div>
                <div className="tip-card">
                  <Calendar className="tip-icon" />
                  <p>Documentez chaque étape de production</p>
                </div>
                <div className="tip-card">
                  <Package className="tip-icon" />
                  <p>Contrôlez régulièrement la qualité</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Production;