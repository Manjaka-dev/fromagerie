import React, { useState, useEffect } from 'react';
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
import { productionAPI, formatDate } from '../services/api';

const Production = () => {
  const [searchQuery, setSearchQuery] = useState('');
  
  // États pour les données de production
  const [productions, setProductions] = useState([]);
  const [fiches, setFiches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  // Ajout de recentProductions pour éviter l'erreur
  const [recentProductions, setRecentProductions] = useState([]);

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

  // Charger les données au montage
  useEffect(() => {
    loadProductionData();
  }, []);

  // Données hardcodées pour les catégories de produits
  const categoriesFromage = [
    "Fromage à pâte pressée",
    "Fromage frais",
    "Fromage affiné",
    "Fromage à pâte molle",
    "Fromage de chèvre",
    "Yaourt",
    "Autre produit laitier"
  ];
  
  // Données hardcodées pour les allergènes
  const allergenesCommuns = [
    "Lactose",
    "Protéines du lait",
    "Sans allergènes",
    "Gluten (traces)",
    "Fruits à coque (traces)",
    "Œufs (traces)"
  ];
  
  const loadProductionData = async () => {
    setLoading(true);
    setError(null);
    try {
      // Charger les productions récentes depuis l'API
      const today = new Date().toISOString().split('T')[0];
      
      try {
        const productionsData = await productionAPI.getProductionsByDate(today);
        console.log("Productions récentes récupérées:", productionsData);
        
        let productionsArray = [];
        if (Array.isArray(productionsData)) {
          productionsArray = productionsData;
        } else if (productionsData && typeof productionsData === 'object') {
          // Si l'API renvoie un objet avec une propriété contenant les productions
          productionsArray = productionsData.productions || productionsData.data || [];
        }
        
        setProductions(productionsArray);
        setRecentProductions(productionsArray); // Également définir recentProductions
      } catch (prodErr) {
        console.error('Erreur lors du chargement des productions:', prodErr);
        // Fallback avec des données de test en attendant que l'API soit opérationnelle
        const fallbackData = [
          {
            id: 1,
            category: "Fromage à pâte pressée",
            name: "Gouda Artisanal 250g",
            weight: "250g",
            costPrice: "10 000 Ar",
            sellingPrice: "15 000 Ar",
            date: today,
            status: "Terminé",
            ingredients: "Lait de vache, sel, ferments lactiques"
          },
          {
            id: 2,
            category: "Fromage frais",
            name: "Fromage Frais aux Herbes",
            weight: "200g",
            costPrice: "7 000 Ar",
            sellingPrice: "12 000 Ar",
            date: today,
            status: "En cours",
            ingredients: "Lait de vache, sel, herbes aromatiques"
          }
        ];
        setProductions(fallbackData);
        setRecentProductions(fallbackData); // Également définir recentProductions
      }

      // Charger les fiches de production depuis l'API
      try {
        const fichesData = await productionAPI.getFichesByProduit(1); // Par exemple, produit ID 1
        console.log("Fiches de production récupérées:", fichesData);
        setFiches(Array.isArray(fichesData) ? fichesData : []);
      } catch (fichesErr) {
        console.error('Erreur lors du chargement des fiches de production:', fichesErr);
        setFiches([]);
      }

    } catch (err) {
      console.error('Erreur lors du chargement des données de production:', err);
      setError('Impossible de charger les données de production. Veuillez vérifier que le backend est démarré.');
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour créer une nouvelle fiche de production
  const handleSave = async () => {
    try {
      // Créer un DTO de fiche production avec les données du formulaire
      const ficheData = {
        nomProduit: product.name,
        categorie: product.category,
        poids: product.weight,
        prixRevient: product.costPrice,
        prixVente: product.sellingPrice,
        ingredients: product.ingredients,
        allergenes: product.allergens,
        dateExpiration: product.expirationDate,
        dateCreation: new Date().toISOString().split('T')[0],
        quantiteNecessaire: 1.0 // Valeur par défaut pour quantiteNecessaire
      };

      try {
        await productionAPI.createFicheProduction(ficheData);
        await loadProductionData(); // Recharger les données
      } catch (err) {
        console.error('Erreur lors de la création de la fiche de production:', err);
        setError('Impossible de créer la fiche de production: ' + (err.message || 'Erreur inconnue'));
        return;
      }

      // Ajouter à la liste des productions récentes
      const nouvelleProduction = {
        id: Date.now(),
        category: product.category,
        name: product.name,
        weight: product.weight,
        costPrice: product.costPrice,
        sellingPrice: product.sellingPrice,
        date: new Date().toISOString().split('T')[0],
        status: 'En cours',
        batchNumber: `BATCH${Date.now()}`
      };
      setProductions([nouvelleProduction, ...productions]);

      // Réinitialiser le formulaire
      handleCancel();

    } catch (err) {
      console.error('Erreur lors de la création de la fiche de production:', err);
      setError('Erreur lors de la création de la fiche de production');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProduct(prev => ({
      ...prev,
      [name]: value
    }));
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
                    {categoriesFromage.map((categorie, index) => (
                      <option key={index} value={categorie}>{categorie}</option>
                    ))}
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
                    {allergenesCommuns.map((allergene, index) => (
                      <option key={index} value={allergene}>{allergene}</option>
                    ))}
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
                {loading ? (
                  <div className="loading-message">Chargement des productions...</div>
                ) : error ? (
                  <div className="error-message">{error}</div>
                ) : productions.length === 0 ? (
                  <div className="empty-message">Aucune production récente trouvée</div>
                ) : (
                  productions.map((prod, index) => (
                    <div key={index} className="production-item">
                      <div className="production-header">
                        <span className="production-category">{prod.category || prod.categorie || "Non catégorisé"}</span>
                        <span className="production-name">{prod.name || prod.nom || "Sans nom"}</span>
                      </div>
                      <div className="production-details">
                        <div className="detail-group">
                          <span className="detail-label">Poids:</span>
                          <span>{prod.weight || prod.poids || "Non spécifié"}</span>
                        </div>
                        <div className="detail-group">
                          <span className="detail-label">Prix revient:</span>
                          <span>{prod.costPrice || prod.prixRevient || "Non spécifié"}</span>
                        </div>
                        <div className="detail-group">
                          <span className="detail-label">Ingredients:</span>
                          <span>{prod.ingredients || "Non spécifié"}</span>
                        </div>
                        <div className="detail-group">
                          <span className="detail-label">Prix vente:</span>
                          <span>{prod.sellingPrice || prod.prixVente || "Non spécifié"}</span>
                        </div>
                      </div>
                    </div>
                  ))
                )}
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