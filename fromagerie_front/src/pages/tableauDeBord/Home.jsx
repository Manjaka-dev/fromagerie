import React, { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, LineChart, Line } from 'recharts';
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
import './../../assets/styles/tableauDeBord/home.css';
import SidebarMenu from "../../components/SidebarMenu";
const TableauDeBord = () => {
  const [activeMenuItem, setActiveMenuItem] = useState('Tableau de Bord');
  const [showFilters, setShowFilters] = useState(false);
  const [searchName, setSearchName] = useState('');
  const [searchDate, setSearchDate] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');

  // Dans votre composant
  const [selectedYear, setSelectedYear] = useState('all');
  const [caData, setCaData] = useState([
    { year: '2023', month: 'Jan', value: 18000 },
    { year: '2023', month: 'Fév', value: 32000 },
    { year: '2023', month: 'Jan', value: 18000 },
    { year: '2023', month: 'Fév', value: 32000 },
    { year: '2023', month: 'Fév', value: 32000 },
    { year: '2023', month: 'Mar', value: 22000 },
    { year: '2024', month: 'Jan', value: 15000 },
    { year: '2024', month: 'Fév', value: 28000 },
    { year: '2024', month: 'Mar', value: 19000 },
    { year: '2025', month: 'Jan', value: 12000 },
    { year: '2025', month: 'Fév', value: 25000 },
    { year: '2023', month: 'Jan', value: 18000 },
    { year: '2023', month: 'Fév', value: 32000 },
    { year: '2023', month: 'Mar', value: 22000 },
    { year: '2024', month: 'Jan', value: 15000 },
    { year: '2024', month: 'Fév', value: 28000 },
    { year: '2024', month: 'Mar', value: 19000 },
    { year: '2025', month: 'Jan', value: 12000 },
    { year: '2025', month: 'Fév', value: 25000 },
    { year: '2023', month: 'Jan', value: 18000 },
    { year: '2023', month: 'Fév', value: 32000 },
    { year: '2023', month: 'Mar', value: 22000 },
    { year: '2024', month: 'Jan', value: 15000 },
    { year: '2024', month: 'Fév', value: 28000 },
    { year: '2024', month: 'Mar', value: 19000 },
    { year: '2025', month: 'Jan', value: 12000 },
    { year: '2025', month: 'Fév', value: 25000 }
    // ... autres données
  ]);

  // Filtrer les données selon l'année sélectionnée
  const filteredCaData = selectedYear === 'all'
    ? caData
    : caData.filter(item => item.year === selectedYear);

  // Calcul du CA annuel total
  const totalAnnuel = filteredCaData.length > 0
    ? filteredCaData.reduce((sum, item) => sum + item.value, 0)
    : 0;

  // Calcul du CA de l'année précédente
  const totalAnneePrecedente = selectedYear !== 'all' && selectedYear > 2023 // Adaptez l'année de départ
    ? caData
      .filter(item => item.year === String(Number(selectedYear) - 1))
      .reduce((sum, item) => sum + item.value, 0)
    : 0;

  // Calcul du taux de croissance
  const croissance = selectedYear !== 'all' && totalAnneePrecedente > 0
    ? ((totalAnnuel - totalAnneePrecedente) / totalAnneePrecedente * 100).toFixed(1)
    : null;

  // --------------------------------------------------

  // Données pour le graphique en secteurs de production
  const productionData = [
    { name: 'Prod Hebdomadaires', value: 52.1, color: '#1f2937' },
    { name: 'Taux de qualité', value: 22.8, color: '#60a5fa' },
    { name: 'Livraisons planifié', value: 13.9, color: '#86efac' }
  ];
  // --------------------------------------------------
  // Ajoutez ces données avec les autres données statiques
  const topClients = [
    { name: 'Epicerie Bio Lyon', quantity: 250, value: 12500 },
    { name: 'Fromagerie Martin', quantity: 180, value: 9000 },
    { name: 'Marché de Belleville', quantity: 150, value: 7500 }
  ];
  // ---------------------------------------------
  // Données des livraisons (étendues pour la démonstration)
  const allLivraisons = [
    { name: 'Epicerie Bio Lyon', quantity: '50 kg', date: '12 Juin à 09:00', status: 'Confirmé' },
    { name: 'Epicerie Bio Lyon', quantity: '50 kg', date: '12 Juin à 09:00', status: 'Confirmé' },
    { name: 'Fromagerie Martin', quantity: '75 kg', date: '13 Juin à 14:30', status: 'Préparation' },
    { name: 'Epicerie Bio Lyon', quantity: '50 kg', date: '12 Juin à 09:00', status: 'Confirmé' },
    { name: 'Marché de Belleville', quantity: '30 kg', date: '14 Juin à 08:00', status: 'Préparation' },
    { name: 'Restaurant Le Gourmet', quantity: '25 kg', date: '15 Juin à 16:00', status: 'Confirmé' }
  ];

  // Fonction de filtrage
  const getFilteredLivraisons = () => {
    return allLivraisons.filter(livraison => {
      const matchesName = livraison.name.toLowerCase().includes(searchName.toLowerCase());
      const matchesDate = searchDate === '' || livraison.date.includes(searchDate);
      const matchesStatus = selectedStatus === '' || livraison.status === selectedStatus;

      return matchesName && matchesDate && matchesStatus;
    });
  };

  const clearFilters = () => {
    setSearchName('');
    setSearchDate('');
    setSelectedStatus('');
  };

  const hasActiveFilters = searchName !== '' || searchDate !== '' || selectedStatus !== '';
  // ------------------------------
  const menuItems = [
    { name: 'Tableau de Bord', icon: BarChart3, active: true, path: '/TableauDeBord' },
    { name: 'Stock', icon: Package, path: '/stock' },
    { name: 'Comptabilité', icon: Calculator, path: '/comptabilite' },
    { name: 'Commandes', icon: ShoppingCart, path: '/commandes' },
    { name: 'Livraisons', icon: Truck, path: '/livraisons' },
    { name: 'Administration', icon: Settings, path: '/administration' },
    { name: 'Production', icon: Factory, path: '/production' }
  ];

  const getStatusColor = (status) => {
    switch (status) {
      case 'Confirmé': return 'status-confirmed';
      case 'Préparation': return 'status-preparation';
      default: return 'status-default';
    }
  };


  const [showDetails, setShowDetails] = useState(false);
  const kpiData = [
    {
      id: 1,
      title: "Fromages conformes",
      description: "Taux de qualité (fromages sans défaut)",
      value: 81,
      color: "#ef4444",
      bgColor: "#ecb3b3"
    },
    {
      id: 2,
      title: "Stock utilisé",
      description: "% de matières premières déjà utilisées cette semaine",
      value: 22,
      color: "#22c55e",
      bgColor: "#a7e8bf"
    },
    {
      id: 3,
      title: "Commandes livrées",
      description: "% de commandes déjà expédiées vs commandes reçues",
      value: 62,
      color: "#3b82f6",
      bgColor: "rgba(59, 131, 246, 0.48)"
    }
  ];

  const createPieData = (value) => [
    { name: 'completed', value: value },
    { name: 'remaining', value: 100 - value }
  ];

  // ----------------------STATISTIQUE PERTE-----------------------------

  const [startDate, setStartDate] = useState(formatDate(new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)));
  const [endDate, setEndDate] = useState(formatDate(new Date()));
  const [tempStartDate, setTempStartDate] = useState(startDate);
  const [tempEndDate, setTempEndDate] = useState(endDate);

  const hasDateChanged = tempStartDate !== startDate || tempEndDate !== endDate;

  const handleApplyDateFilter = () => {
    setStartDate(tempStartDate);
    setEndDate(tempEndDate);
    // Ici vous pouvez ajouter un appel API ou filtrer les données locales
    fetchDataForDateRange(tempStartDate, tempEndDate);
  };

  // Fonction utilitaire pour formater la date au format YYYY-MM-DD
  function formatDate(date) {
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }
  const [selectedLossType, setSelectedLossType] = useState('total');


  // Données des pertes par période
  const lossData = [
    { period: 'Lun', date: '17 Jun', production: 45, deterioration: 8, defauts: 3, total: 11 },
    { period: 'Mar', date: '18 Jun', production: 52, deterioration: 12, defauts: 5, total: 17 },
    { period: 'Mer', date: '19 Jun', production: 48, deterioration: 6, defauts: 2, total: 8 },
    { period: 'Jeu', date: '20 Jun', production: 55, deterioration: 15, defauts: 7, total: 22 },
    { period: 'Ven', date: '21 Jun', production: 50, deterioration: 9, defauts: 4, total: 13 },
    { period: 'Sam', date: '22 Jun', production: 42, deterioration: 5, defauts: 1, total: 6 },
    { period: 'Dim', date: '23 Jun', production: 38, deterioration: 4, defauts: 2, total: 6 }
  ];

  // Données des types de pertes
  const lossTypeData = [
    { name: 'Détérioration', value: 45.2, color: '#ef4444', count: 59 },
    { name: 'Défauts qualité', value: 28.7, color: '#f97316', count: 24 },
  ];

  const totalProduction = lossData.reduce((sum, item) => sum + item.production, 0);
  const totalLosses = lossData.reduce((sum, item) => sum + item.total, 0);
  const lossPercentage = ((totalLosses / totalProduction) * 100).toFixed(1);
  const averageDailyLoss = (totalLosses / lossData.length).toFixed(1);
  const previousPeriodLoss = 95;
  const lossEvolution = ((totalLosses - previousPeriodLoss) / previousPeriodLoss * 100).toFixed(1);

  const lossTypeColors = {
    deterioration: '#ef4444',  // Rouge
    defauts: '#f97316',       // Orange
  };

  const lossTypeLabels = {
    deterioration: 'Détérioration',
    defauts: 'Défauts qualité'
  };
  // ---------------------------------------------------------

  // Données des bénéfices
  const benefitData = [
    {
      period: 'Journalier',
      value: 600000,
      trend: 2.5,
      isPositive: true,
      icon: TrendingUp,
      comparison: 'vs hier'
    },
    {
      period: 'Mensuel',
      value: 3000000,
      trend: 5.1,
      isPositive: true,
      icon: TrendingUp,
      comparison: 'vs mois dernier'
    },
    {
      period: 'Annuel',
      value: 36000000,
      trend: 1.2,
      isPositive: false,
      icon: TrendingDown,
      comparison: 'vs année dernière'
    }
  ];

  // -----------------RECHERCHE-------------------------------
  const [searchQuery, setSearchQuery] = useState('');

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
            <Search className="search-icon" size={18} />
            <input
              type="text"
              placeholder="Rechercher des produits, références..."
              className="search_input"
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

        {/* Dashboard Content */}
        <div className="dashboard-content">

          {/* 3 blocs de bénéfices : journaliere,mensuel,annuel */}
          {/* Section Bénéfices */}
          <div className="benefits-container">
            {benefitData.map((benefit, index) => (
              <div key={index} className="benefit-card">
                <div className="benefit-header">
                  <div>
                    <div className="benefit-value">
                      {benefit.period === 'Journalier'
                        ? ` ${benefit.value.toLocaleString('fr-FR', { minimumFractionDigits: 0, maximumFractionDigits: 2 })}`
                        : benefit.value.toLocaleString('fr-FR')}
                    </div>
                    <div className="benefit-period">Bénéfice {benefit.period}</div>
                  </div>
                  <div className={`benefit-trend ${benefit.isPositive ? 'trend-positive' : 'trend-negative'}`}>
                    <benefit.icon className="benefit-icon" size={14} />
                    {benefit.trend}% {benefit.comparison}
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="dashboard-grid">

            {/* Livraisons Section */}
            <div className="livraisons-section">
              <div className="card">
                <div className="card-livraison">
                  <h2 className="section-title">Prochaines Livraisons</h2>
                  <div className="header-actions">
                    <button
                      className={`filter-btn ${showFilters ? 'active' : ''}`}
                      onClick={() => setShowFilters(!showFilters)}
                    >
                      <Filter className="filter-icon" />
                      Filtres
                      {hasActiveFilters && <span className="filter-badge"></span>}
                    </button>
                    <button className="view-all-btn">
                      Voir Planning Complet
                      <span className="arrow">→</span>
                    </button>
                  </div>
                </div>

                {/* Filtres */}
                {showFilters && (
                  <div className="filters-container">
                    <div className="filters-row">
                      <div className="filter-group">
                        <label className="filter-label">Rechercher par nom</label>
                        {/* <Search className="search-icon" /> */}
                        <input
                          type="text"
                          placeholder="Nom du client..."
                          value={searchName}
                          onChange={(e) => setSearchName(e.target.value)}
                          className="searchInputFilter"
                        />
                      </div>

                      <div className="filter-group">
                        <label className="filter-label">Filtrer par date</label>
                        <input
                          type="text"
                          placeholder="Ex: 12 Juin, 13 Juin..."
                          value={searchDate}
                          onChange={(e) => setSearchDate(e.target.value)}
                          className="dateInputFilter"
                        />
                      </div>

                      <div className="filter-group">
                        <label className="filter-label">Statut</label>
                        <select
                          value={selectedStatus}
                          onChange={(e) => setSelectedStatus(e.target.value)}
                          className="status-select"
                        >
                          <option value="">Tous les statuts</option>
                          <option value="Confirmé">Confirmé</option>
                          <option value="Préparation">Préparation</option>
                        </select>
                      </div>

                      {hasActiveFilters && (
                        <button className="clear-filters-btn" onClick={clearFilters}>
                          <X className="clear-icon" />
                          Effacer
                        </button>
                      )}
                    </div>
                  </div>
                )}

                <div className="livraisons-list">
                  {getFilteredLivraisons().length > 0 ? (
                    getFilteredLivraisons().map((livraison, index) => (
                      <div key={index} className="livraison-item">
                        <div className="livraison-info">
                          <h3 className="livraison-name">{livraison.name}</h3>
                          <div className="livraison-date">
                            <Calendar className="calendar-icon" />
                            <span>{livraison.date}</span>
                          </div>
                        </div>

                        <div className="livraison-details">
                          <div className="livraison-quantity">{livraison.quantity}</div>
                          <span className={`status-badge ${getStatusColor(livraison.status)}`}>
                            {livraison.status}
                          </span>
                        </div>
                      </div>
                    ))
                  ) : (
                    <div className="no-results">
                      <p>Aucune livraison trouvée avec ces critères.</p>
                      <button onClick={clearFilters} className="reset-btn">
                        Réinitialiser les filtres
                      </button>
                    </div>
                  )}
                </div>
              </div>
            </div>
            <div className="right-bloc">
              {/* Production Chart */}
              <div className="production-section">
                <div className="card">
                  <h3 className="section-title">Production</h3>
                  <div className="chart-container">
                    <ResponsiveContainer width="100%" height="100%">
                      <PieChart>
                        <Pie
                          data={productionData}
                          cx="50%"
                          cy="50%"
                          innerRadius={40}
                          outerRadius={80}
                          dataKey="value"
                        >
                          {productionData.map((entry, index) => (
                            <Cell key={`cell-${index}`} fill={entry.color} />
                          ))}
                        </Pie>
                        <Tooltip formatter={(value) => `${value}%`} />
                      </PieChart>
                    </ResponsiveContainer>
                  </div>

                  <div className="legend">
                    {productionData.map((item, index) => (
                      <div key={index} className="legend-item">
                        <div className="legend-indicator" style={{ backgroundColor: item.color }}></div>
                        <span className="legend-label">{item.name}</span>
                        <span className="legend-value">{item.value}%</span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
              {/* Top 3 clients */}
              <div className="top-clients-section">
                <div className="card">
                  <h3 className="section-title">Top 3 Clients</h3>
                  <div className="clients-list">
                    {topClients.map((client, index) => (
                      <div key={index} className="client-item">
                        <div className="client-rank">{index + 1}</div>
                        <div className="client-info">
                          <div className="client-name">{client.name}</div>
                          <div className="client-quantity">{client.quantity} kg commandés</div>
                        </div>
                        <div className="client-value">{client.value} Ar </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>

          </div>

          {/* Evolution du Chiffre d'Affaires */}
          <div className="ca-section">
            <div className="card">
              <div className="card-header">
                <div className="ca-header">
                  <h3 className="section-title">Evolution du Chiffre d'Affaires</h3>
                  <div className="year-filter">
                    <select
                      value={selectedYear}
                      onChange={(e) => setSelectedYear(e.target.value)}
                      className="year-select"
                    >
                      <option value="all">Toutes années</option>
                      <option value="2023">2023</option>
                      <option value="2024">2024</option>
                      <option value="2025">2025</option>
                    </select>
                  </div>
                </div>
                <div className="financial-info">
                  <div className="total-info">
                    {selectedYear === 'all'
                      ? "CA global cumulé"
                      : `CA ${selectedYear} total`
                    }: {new Intl.NumberFormat('fr-FR', {
                      style: 'currency',
                      currency: 'EUR',
                      maximumFractionDigits: 0
                    }).format(totalAnnuel)}
                  </div>

                  {/* Affichage conditionnel du taux */}
                  {croissance !== null && (
                    <div className={`growth-info ${croissance > 0 ? 'positive' : 'negative'}`}>
                      {croissance > 0 ? '↑' : '↓'} {Math.abs(croissance)}% vs {Number(selectedYear) - 1}
                    </div>
                  )}
                </div>

              </div>

              <div className="bar-chart-container">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={filteredCaData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                    <CartesianGrid strokeDasharray="3 3" stroke="rgba(30, 233, 233, 0.1)" />
                    <XAxis
                      dataKey="month"
                      axisLine={false}
                      tickLine={false}
                      tick={{ fontSize: 12, fill: '#7f8c8d' }}
                    />
                    <YAxis
                      axisLine={false}
                      tickLine={false}
                      tick={{ fontSize: 12, fill: '#7f8c8d' }}
                      tickFormatter={(value) => `${value / 1000}K`}
                    />
                    <Tooltip
                      formatter={(value) => [`${value} Ar`, 'CA']}
                      labelStyle={{ color: '#2c3e50' }}
                      contentStyle={{
                        backgroundColor: 'rgba(255, 255, 255, 0.95)',
                        border: '1px solid rgba(75, 192, 192, 0.2)',
                        borderRadius: '8px',
                        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)'
                      }}
                    />
                    <Bar
                      dataKey="value"
                      radius={[4, 4, 0, 0]}
                    >
                      {filteredCaData.map((entry, index) => {
                        const values = filteredCaData.map(item => item.value);
                        const max = Math.max(...values);
                        const min = Math.min(...values);
                        const moyenne = values.reduce((a, b) => a + b, 0) / values.length;

                        let fillColor;
                        if (entry.value === max) {
                          fillColor = 'rgba(79, 168, 76, 0.64)'; // Vert foncé pour le CA max
                        } else if (entry.value === min) {
                          fillColor = 'rgba(190, 4, 4, 0.79)'; // Rouge pour le CA min
                        } else if (Math.abs(entry.value - moyenne) < (max - min) * 0.1) {
                          fillColor = 'rgba(254, 166, 3, 0.68)'; // Bleu pour les valeurs proches de la moyenne
                        } else {
                          fillColor = 'rgba(254, 166, 3, 0.68)'; // gris pour les autres valeurs
                        }

                        return <Cell key={`cell-${index}`} fill={fillColor} />;
                      })}
                    </Bar>
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </div>
          </div>
          {/* ------------------------------------------------------------------------------ */}

          {/* Statistique de PERTES */}
          <div className="loss-statistics-section">
            <div className="card">
              {/* En-tête de la section */}
              <div className="card-header">
                <div className="loss-header">
                  <div className="loss-title-group">
                    <TrendingDown className="loss-icon" />
                    <h3 className="section-title">Statistique d'évolution de perte</h3>
                  </div>

                  {/* Contrôles de date avec bouton Appliquer */}
                  <div className="loss-controls">
                    <div className="date-range-selector">
                      <div className="date-input-group">
                        <label htmlFor="start-date">Du</label>
                        <input
                          type="date"
                          id="start-date"
                          value={startDate}
                          onChange={(e) => setTempStartDate(e.target.value)}
                          className="date-input"
                        />
                      </div>
                      <div className="date-input-group">
                        <label htmlFor="end-date">Au</label>
                        <input
                          type="date"
                          id="end-date"
                          value={tempEndDate}
                          onChange={(e) => setTempEndDate(e.target.value)}
                          className="date-input"
                          min={tempStartDate}
                        />
                      </div>
                      <button
                        className="apply-date-btn"
                        onClick={handleApplyDateFilter}
                        disabled={!hasDateChanged}
                      >
                        Appliquer
                      </button>
                    </div>
                  </div>
                </div>

                {/* KPI de pertes */}
                <div className="loss-kpi-row">
                  <div className="loss-kpi-item">
                    <div className="loss-kpi-value">{totalLosses} kg</div>
                    <div className="loss-kpi-label">Total des pertes</div>
                    <div className={`loss-kpi-trend ${lossEvolution > 0 ? 'negative' : 'positive'}`}>
                      {lossEvolution > 0 ? '↑' : '↓'} {Math.abs(lossEvolution)}% vs période précédente
                    </div>
                  </div>

                  <div className="loss-kpi-item">
                    <div className="loss-kpi-value">{lossPercentage}%</div>
                    <div className="loss-kpi-label">Taux de perte</div>
                    <div className="loss-kpi-detail">{totalLosses}kg / {totalProduction}kg produits</div>
                  </div>

                  <div className="loss-kpi-item">
                    <div className="loss-kpi-value">{averageDailyLoss} kg</div>
                    <div className="loss-kpi-label">Moyenne journalière</div>
                    <div className="loss-kpi-detail">Sur {lossData.length} jours</div>
                  </div>
                </div>
              </div>

              {/* Contenu principal avec graphiques */}
              <div className="loss-statistics-section">
                <div className="loss-content">
                  {/* Graphique linéaire d'évolution */}
                  <div className="loss-chart-container">
                    <div className="chart-header">
                      <h4 className="chart-title">Évolution des pertes</h4>
                      <div className="chart-controls">
                        <select
                          value={selectedLossType}
                          onChange={(e) => setSelectedLossType(e.target.value)}
                          className="loss-type-select"
                        >
                          <option value="deterioration">Détérioration</option>
                          <option value="defauts">Défauts qualité</option>
                        </select>
                      </div>
                    </div>

                    <div className="line-chart-wrapper">
                      <ResponsiveContainer width="100%" height={250}>
                        <LineChart data={lossData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                          <CartesianGrid strokeDasharray="3 3" stroke="rgba(0, 0, 0, 0.1)" />
                          <XAxis
                            dataKey="period"
                            axisLine={false}
                            tickLine={false}
                            tick={{ fontSize: 12, fill: '#6b7280' }}
                          />
                          <YAxis
                            axisLine={false}
                            tickLine={false}
                            tick={{ fontSize: 12, fill: '#6b7280' }}
                            label={{ value: 'kg', angle: -90, position: 'insideLeft' }}
                          />
                          <Tooltip
                            formatter={(value, name) => [`${value} kg`, lossTypeLabels[name]]}
                            labelFormatter={(label) => label}
                          />

                          {selectedLossType === 'all' ? (
                            // Afficher toutes les lignes
                            <>
                              <Line
                                type="monotone"
                                dataKey="deterioration"
                                stroke={lossTypeColors.deterioration}
                                strokeWidth={2}
                                name="deterioration"
                                dot={{ fill: lossTypeColors.deterioration, r: 4 }}
                              />
                              <Line
                                type="monotone"
                                dataKey="defauts"
                                stroke={lossTypeColors.defauts}
                                strokeWidth={2}
                                name="defauts"
                                dot={{ fill: lossTypeColors.defauts, r: 4 }}
                              />
                            </>
                          ) : (
                            // Afficher seulement la ligne sélectionnée
                            <Line
                              type="monotone"
                              dataKey={selectedLossType}
                              stroke={lossTypeColors[selectedLossType]}
                              strokeWidth={3}
                              name={selectedLossType}
                              dot={{ fill: lossTypeColors[selectedLossType], r: 6 }}
                            />
                          )}
                        </LineChart>
                      </ResponsiveContainer>
                    </div>
                  </div>

                  {/* Répartition par type de perte */}
                  <div className="loss-breakdown">
                    <div className="breakdown-header">
                      <h4 className="chart-title">Répartition par type de perte</h4>
                      <div className="breakdown-total">
                        Total: {lossTypeData.reduce((sum, item) => sum + item.count, 0)} incidents
                      </div>
                    </div>

                    <div className="breakdown-list">
                      {lossTypeData.map((item, index) => (
                        <div key={index} className="breakdown-item">
                          <div className="breakdown-info">
                            <div className="breakdown-name">
                              <div
                                className="breakdown-indicator"
                                style={{ backgroundColor: item.color }}
                              ></div>
                              <span>{item.name}</span>
                            </div>
                            <div className="breakdown-count">{item.count} cas</div>
                          </div>

                          <div className="breakdown-bar-container">
                            <div
                              className="breakdown-bar"
                              style={{
                                width: `${item.value}%`,
                                backgroundColor: item.color
                              }}
                            ></div>
                            <span className="breakdown-percentage">{item.value}%</span>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>

                {/* Alertes et recommandations */}
                <div className="loss-alerts">
                  <div className="alert-item alert-warning">
                    <AlertTriangle className="alert-icon" />
                    <div className="alert-content">
                      <div className="alert-title">Attention: Augmentation des pertes détectée</div>
                      <div className="alert-message">
                        Les pertes par détérioration ont augmenté de 15% cette semaine.
                        Vérifiez les conditions de stockage.
                      </div>
                    </div>
                  </div>
                </div>
              </div>



            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export { TableauDeBord };