import React, { useState, useEffect } from 'react';
import { ArrowUp, ArrowDown, ChevronDown, Filter, Search, Package, RefreshCw, Clock, Bell, User } from 'lucide-react';
import SidebarMenu from "../../components/SidebarMenu";

const StockProduitsFinis = () => {
  const [lots, setLots] = useState([]);
  const [stocks, setStocks] = useState([]);
  const [mouvements, setMouvements] = useState([]);
  const [loading, setLoading] = useState(true);
  
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'asc' });
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedProduit, setSelectedProduit] = useState('');
  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const simulatedLots = [
          { id: 1, produit_id: 1, numero_lot: 'LOT-2023-001', date_fabrication: '2023-10-15', date_peremption: '2023-12-15', poids : 1.5 },
          { id: 2, produit_id: 2, numero_lot: 'LOT-2023-002', date_fabrication: '2023-09-20', date_peremption: '2024-03-20', poids : 2.0 },
          { id: 3, produit_id: 1, numero_lot: 'LOT-2023-003', date_fabrication: '2023-11-05', date_peremption: '2024-02-05', poids : 1.8 },
          
        ];

        const simulatedStocks = [
          { id: 1, lot_id: 1, quantite: 120 },
          { id: 2, lot_id: 2, quantite: 85 },
          { id: 3, lot_id: 3, quantite: 65 },
        ];

        const simulatedMouvements = [
          { id: 1, lot_id: 1, type_mouvement: 'ENTREE', quantite: 50, date_mouvement: '2023-10-16T08:30:00', commentaire: 'Livraison initiale' },
          { id: 2, lot_id: 1, type_mouvement: 'SORTIE', quantite: 30, date_mouvement: '2023-11-05T14:15:00', commentaire: 'Commande client' },
          { id: 3, lot_id: 2, type_mouvement: 'ENTREE', quantite: 100, date_mouvement: '2023-09-21T10:00:00', commentaire: 'Production' },
        ];

        setLots(simulatedLots);
        setStocks(simulatedStocks);
        setMouvements(simulatedMouvements);
        setLoading(false);
      } catch (error) {
        console.error("Erreur de chargement des données:", error);
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const calculateVariations = (lotId) => {
    const lotMouvements = mouvements.filter(m => m.lot_id === lotId);
    const entreeTotal = lotMouvements
      .filter(m => m.type_mouvement === 'ENTREE')
      .reduce((sum, m) => sum + m.quantite, 0);
    
    const sortieTotal = lotMouvements
      .filter(m => m.type_mouvement === 'SORTIE')
      .reduce((sum, m) => sum + m.quantite, 0);
    
    const stockActuel = stocks.find(s => s.lot_id === lotId)?.quantite || 0;
    const variation = stockActuel / (entreeTotal - sortieTotal) * 100 - 100;
    
    return {
      entreeTotal,
      sortieTotal,
      variation: variation.toFixed(1) + '%'
    };
  };

  const mergedData = lots.map(lot => {
    const stock = stocks.find(s => s.lot_id === lot.id);
    const variations = calculateVariations(lot.id);
    const produit = lot.produit_id === 1 ? 'Camembert' : 'Comté'; // À remplacer par vos données produits
    
    return {
      ...lot,
      produit,
      quantite: stock?.quantite || 0,
      variationQuantite: variations.variation,
      entreeTotal: variations.entreeTotal,
      sortieTotal: variations.sortieTotal
    };
  });

  const requestSort = (key) => {
    let direction = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
    return new Date(dateString).toLocaleDateString('fr-FR', options);
  };

  const getVariationClass = (value) => {
    if (!value) return 'text-gray-500';
    const numericValue = parseFloat(value);
    if (numericValue > 0) return 'text-green-500';
    if (numericValue < 0) return 'text-red-500';
    return 'text-gray-500';
  };

  const filteredData = mergedData
    .filter(item => {
      return (
        item.numero_lot.toLowerCase().includes(searchTerm.toLowerCase()) ||
        item.produit.toLowerCase().includes(searchTerm.toLowerCase())
      ) && (
        selectedProduit === '' || item.produit === selectedProduit
      );
    })
    .sort((a, b) => {
      if (sortConfig.key) {
        if (a[sortConfig.key] < b[sortConfig.key]) {
          return sortConfig.direction === 'asc' ? -1 : 1;
        }
        if (a[sortConfig.key] > b[sortConfig.key]) {
          return sortConfig.direction === 'asc' ? 1 : -1;
        }
      }
      return 0;
    });

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <RefreshCw className="animate-spin h-8 w-8 text-blue-500" />
      </div>
    );
  }

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
    <div className="p-6 bg-white shadow">
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
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-800 flex items-center">
          <Package className="mr-2 h-6 w-6" />
          Stock Produits Finis
        </h1>
        <div className="flex space-x-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            <input
              type="text"
              placeholder="Rechercher par lot ou produit..."
              className="pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <select
            className="px-4 py-2 border rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={selectedProduit}
            onChange={(e) => setSelectedProduit(e.target.value)}
          >
            <option value="">Tous les produits</option>
            <option value="Camembert">Camembert</option>
            <option value="Comté">Comté</option>
          </select>
          <button className="flex items-center px-4 py-2 border rounded-lg bg-gray-100 hover:bg-gray-200">
            <Filter className="h-4 w-4 mr-2" />
            Filtres
          </button>
        </div>
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-100">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Numéro de Lot
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Produit
              </th>
              <th 
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer"
                onClick={() => requestSort('date_fabrication')}
              >
                <div className="flex items-center">
                  Date Fabrication
                  {sortConfig.key === 'date_fabrication' && (
                    sortConfig.direction === 'asc' ? 
                    <ArrowUp className="ml-1 h-3 w-3" /> : 
                    <ArrowDown className="ml-1 h-3 w-3" />
                  )}
                </div>
              </th>
              <th 
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer"
                onClick={() => requestSort('date_peremption')}
              >
                <div className="flex items-center">
                  Date Péremption
                  {sortConfig.key === 'date_peremption' && (
                    sortConfig.direction === 'asc' ? 
                    <ArrowUp className="ml-1 h-3 w-3" /> : 
                    <ArrowDown className="ml-1 h-3 w-3" />
                  )}
                </div>
              </th>
              <th 
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer"
                onClick={() => requestSort('quantite')}
              >
                <div className="flex items-center">
                  Stock Actuel
                  {sortConfig.key === 'quantite' && (
                    sortConfig.direction === 'asc' ? 
                    <ArrowUp className="ml-1 h-3 w-3" /> : 
                    <ArrowDown className="ml-1 h-3 w-3" />
                  )}
                </div>
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Type Mouvement
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Variation Stock
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Poids
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {filteredData.map((lot) => (
              <tr key={lot.id} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {lot.numero_lot}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {lot.produit}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {formatDate(lot.date_fabrication)}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {formatDate(lot.date_peremption)}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {lot.quantite} unités
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <div className="flex flex-col">
                    <span className="text-green-500">+{lot.entreeTotal} entrées</span>
                    <span className="text-red-500">-{lot.sortieTotal} sorties</span>
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm">
                  <span className={`${getVariationClass(lot.variationQuantite)} font-medium`}>
                    {lot.variationQuantite}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm">
                  <span className={`${getVariationClass(lot.poids)} font-medium`}>
                    {lot.poids}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="mt-4 text-sm text-gray-500">
        <p>Total: {filteredData.length} lots affichés</p>
      </div>
    </div>
    </div>
  );
};

export default StockProduitsFinis;