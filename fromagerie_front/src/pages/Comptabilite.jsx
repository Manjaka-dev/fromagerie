import '../assets/styles/Comptabilite.css';
import { NavLink } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import { Search, Filter, Calendar, TrendingUp,ShoppingCart,Settings,Factory, Truck,Calculator,TrendingDown, Users, Package, FileText, Wallet, Eye, ChevronDown, ChevronUp,Bell,Clock,User,BarChart3 } from 'lucide-react';
import SidebarMenu from "../components/SidebarMenu";
import { comptabiliteAPI, formatCurrency, formatDate, getDateRange } from '../services/api';

const Comptabilite = () => {
  // États pour les données
  const [revenus, setRevenus] = useState([]);
  const [depenses, setDepenses] = useState([]);
  const [bilans, setBilans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Ajout de searchQuery
  const [searchQuery, setSearchQuery] = useState('');
  const [activeTab, setActiveTab] = useState('recettes');
  const [searchTerms, setSearchTerms] = useState({
    recettes: '',
    depenses: '',
    salaires: '',
    inventaire: '',
    facturation: '',
    tresorerie: ''
  });
  const [filters, setFilters] = useState({
    recettes: { periode: 'mois', statut: 'tous' },
    depenses: { periode: 'mois', categorie: 'toutes' },
    salaires: { periode: 'mois', employe: 'tous' },
    inventaire: { categorie: 'toutes', statut: 'tous' },
    facturation: { periode: 'mois', statut: 'toutes' },
    tresorerie: { periode: 'mois', type: 'tous' }
  });

  // Charger les données au montage
  useEffect(() => {
    loadComptabiliteData();
  }, []);

  const loadComptabiliteData = async () => {
    setLoading(true);
    setError(null);
    try {
      const dateRange = getDateRange(30); // 30 derniers jours

      // Charger les revenus depuis l'API
      const revenusData = await comptabiliteAPI.getRevenus(dateRange.dateDebut, dateRange.dateFin);
      setRevenus(revenusData || []);

      // Charger les dépenses depuis l'API
      const depensesData = await comptabiliteAPI.getDepenses(dateRange.dateDebut, dateRange.dateFin);
      setDepenses(depensesData || []);

      // Charger les bilans depuis l'API
      const bilansData = await comptabiliteAPI.getBilans();
      setBilans(bilansData || []);

    } catch (err) {
      console.error('Erreur lors du chargement des données comptables:', err);
      setError('Impossible de charger les données comptables. Veuillez vérifier que le backend est démarré.');
    } finally {
      setLoading(false);
    }
  };

  const tabs = [
    { id: 'recettes', label: 'Recettes', icon: TrendingUp },
    { id: 'depenses', label: 'Dépenses', icon: TrendingDown },
    { id: 'salaires', label: 'Salaires', icon: Users },
    { id: 'inventaire', label: 'Inventaire', icon: Package },
    { id: 'bilan', label: 'Bilan Financier', icon: FileText },
    { id: 'facturation', label: 'Facturation', icon: FileText },
    { id: 'tresorerie', label: 'Trésorerie', icon: Wallet }
  ];

  const formatCurrencyLocal = (amount) => {
    return formatCurrency(amount);
  };

  const formatDateLocal = (dateString) => {
    return formatDate(dateString);
  };

  const getStatusBadge = (statut) => {
    const statusConfig = {
      'payé': 'bg-green-100 text-green-800',
      'payée': 'bg-green-100 text-green-800',
      'réglé': 'bg-green-100 text-green-800',
      'en attente': 'bg-yellow-100 text-yellow-800',
      'non payé': 'bg-red-100 text-red-800',
      'en retard': 'bg-red-100 text-red-800',
      'en stock': 'bg-blue-100 text-blue-800',
      'en cours d\'affinage': 'bg-purple-100 text-purple-800'
    };
    return (
      <span className={`px-2 py-1 rounded-full text-xs font-medium ${statusConfig[statut] || 'bg-gray-100 text-gray-800'}`}>
        {statut}
      </span>
    );
  };
  const SearchAndFilter = ({ tab, searchTerm, onSearchChange, filter, onFilterChange }) => (
    <div className="mb-6 space-y-4">
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="flex-1 relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
          <input
            type="text"
            placeholder="Rechercher..."
            value={searchTerm}
            onChange={(e) => onSearchChange(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent rechercheFiltre"
          />
        </div>
        <div className="flex gap-2">
          <select
            value={filter.periode || 'tous'}
            onChange={(e) => onFilterChange({ ...filter, periode: e.target.value })}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent rechercheFiltre"
          >
            <option value="tous">Toutes périodes</option>
            <option value="aujourd'hui">Aujourd'hui</option>
            <option value="cette-semaine">Cette semaine</option>
            <option value="ce-mois">Ce mois</option>
            <option value="cette-annee">Cette année</option>
          </select>
          {tab === 'recettes' && (
            <select
              value={filter.statut || 'tous'}
              onChange={(e) => onFilterChange({ ...filter, statut: e.target.value })}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent rechercheFiltre"
            >
              <option value="tous">Tous statuts</option>
              <option value="payé">Payé</option>
              <option value="en attente">En attente</option>
            </select>
          )}
        </div>
      </div>
    </div>
  );

  const RecettesTab = () => (
    <div className="space-y-6">
      <SearchAndFilter
        tab="recettes"
        searchTerm={searchTerms.recettes}
        onSearchChange={(term) => setSearchTerms({ ...searchTerms, recettes: term })}
        filter={filters.recettes}
        onFilterChange={(filter) => setFilters({ ...filters, recettes: filter })}
      />

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg border border-green-200">
          <h3 className="text-sm font-medium text-green-800">Revenus du jour</h3>
          <p className="text-2xl font-bold text-green-600">{formatCurrencyLocal(590000)}</p>
        </div>
        <div className="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg border border-blue-200">
          <h3 className="text-sm font-medium text-blue-800">Revenus du mois</h3>
          <p className="text-2xl font-bold text-blue-600">{formatCurrencyLocal(2450000)}</p>
        </div>
        <div className="bg-gradient-to-r from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200">
          <h3 className="text-sm font-medium text-purple-800">Revenus de l'année</h3>
          <p className="text-2xl font-bold text-purple-600">{formatCurrencyLocal(18500000)}</p>
        </div>
        <div className="bg-gradient-to-r from-orange-50 to-orange-100 p-4 rounded-lg border border-orange-200">
          <h3 className="text-sm font-medium text-orange-800">Nombre de ventes</h3>
          <p className="text-2xl font-bold text-orange-600">105</p>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Produit</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantité</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Prix unitaire</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Montant total</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Client</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Paiement</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Statut</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {revenus.map((recette) => (
                <tr key={recette.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatDateLocal(recette.date)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{recette.produit}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{recette.quantite}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatCurrencyLocal(recette.prixUnitaire)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{formatCurrencyLocal(recette.quantite * recette.prixUnitaire)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{recette.client}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{recette.paiement}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{getStatusBadge(recette.statut)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );

  const DepensesTab = () => (
    <div className="space-y-6">
      <SearchAndFilter
        tab="depenses"
        searchTerm={searchTerms.depenses}
        onSearchChange={(term) => setSearchTerms({ ...searchTerms, depenses: term })}
        filter={filters.depenses}
        onFilterChange={(filter) => setFilters({ ...filters, depenses: filter })}
      />

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-gradient-to-r from-red-50 to-red-100 p-4 rounded-lg border border-red-200">
          <h3 className="text-sm font-medium text-red-800">Dépenses du jour</h3>
          <p className="text-2xl font-bold text-red-600">{formatCurrencyLocal(210000)}</p>
        </div>
        <div className="bg-gradient-to-r from-orange-50 to-orange-100 p-4 rounded-lg border border-orange-200">
          <h3 className="text-sm font-medium text-orange-800">Dépenses du mois</h3>
          <p className="text-2xl font-bold text-orange-600">{formatCurrencyLocal(1850000)}</p>
        </div>
        <div className="bg-gradient-to-r from-yellow-50 to-yellow-100 p-4 rounded-lg border border-yellow-200">
          <h3 className="text-sm font-medium text-yellow-800">Dépenses de l'année</h3>
          <p className="text-2xl font-bold text-yellow-600">{formatCurrencyLocal(12500000)}</p>
        </div>
        <div className="bg-gradient-to-r from-gray-50 to-gray-100 p-4 rounded-lg border border-gray-200">
          <h3 className="text-sm font-medium text-gray-800">En attente</h3>
          <p className="text-2xl font-bold text-gray-600">{formatCurrencyLocal(45000)}</p>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Catégorie</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Description</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Montant TTC</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Statut</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {depenses.map((depense) => (
                <tr key={depense.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatDate(depense.date)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 capitalize">{depense.categorie}</td>
                  <td className="px-6 py-4 text-sm text-gray-900">{depense.description}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{formatCurrency(depense.montant)}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{getStatusBadge(depense.statut)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );

  // const SalairesTab = () => (
  //   <div className="space-y-6">
  //     <SearchAndFilter
  //       tab="salaires"
  //       searchTerm={searchTerms.salaires}
  //       onSearchChange={(term) => setSearchTerms({ ...searchTerms, salaires: term })}
  //       filter={filters.salaires}
  //       onFilterChange={(filter) => setFilters({ ...filters, salaires: filter })}
  //     />

  //     <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
  //       <div className="bg-gradient-to-r from-indigo-50 to-indigo-100 p-4 rounded-lg border border-indigo-200">
  //         <h3 className="text-sm font-medium text-indigo-800">Masse salariale</h3>
  //         <p className="text-2xl font-bold text-indigo-600">{formatCurrency(1900000)}</p>
  //       </div>
  //       <div className="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg border border-green-200">
  //         <h3 className="text-sm font-medium text-green-800">Salaires payés</h3>
  //         <p className="text-2xl font-bold text-green-600">{formatCurrency(1105000)}</p>
  //       </div>
  //       <div className="bg-gradient-to-r from-red-50 to-red-100 p-4 rounded-lg border border-red-200">
  //         <h3 className="text-sm font-medium text-red-800">En attente</h3>
  //         <p className="text-2xl font-bold text-red-600">{formatCurrency(510000)}</p>
  //       </div>
  //       <div className="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg border border-blue-200">
  //         <h3 className="text-sm font-medium text-blue-800">Employés</h3>
  //         <p className="text-2xl font-bold text-blue-600">3</p>
  //       </div>
  //     </div>

  //     <div className="bg-white rounded-lg shadow-sm border border-gray-200">
  //       <div className="overflow-x-auto">
  //         <table className="w-full">
  //           <thead className="bg-gray-50">
  //             <tr>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Employé</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poste</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Salaire brut</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Salaire net</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Période</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Statut</th>
  //             </tr>
  //           </thead>
  //           <tbody className="bg-white divide-y divide-gray-200">
  //             {salairesData.map((salaire) => (
  //               <tr key={salaire.id} className="hover:bg-gray-50">
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{salaire.employe}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{salaire.poste}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatCurrency(salaire.salaireBrut)}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{formatCurrency(salaire.salaireNet)}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{salaire.periode}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap">{getStatusBadge(salaire.statut)}</td>
  //               </tr>
  //             ))}
  //           </tbody>
  //         </table>
  //       </div>
  //     </div>
  //   </div>
  // );

  // const InventaireTab = () => (
  //   <div className="space-y-6">
  //     <SearchAndFilter
  //       tab="inventaire"
  //       searchTerm={searchTerms.inventaire}
  //       onSearchChange={(term) => setSearchTerms({ ...searchTerms, inventaire: term })}
  //       filter={filters.inventaire}
  //       onFilterChange={(filter) => setFilters({ ...filters, inventaire: filter })}
  //     />

  //     <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
  //       <div className="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg border border-blue-200">
  //         <h3 className="text-sm font-medium text-blue-800">Valeur totale stock</h3>
  //         <p className="text-2xl font-bold text-blue-600">{formatCurrency(1637500)}</p>
  //       </div>
  //       <div className="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg border border-green-200">
  //         <h3 className="text-sm font-medium text-green-800">Produits en stock</h3>
  //         <p className="text-2xl font-bold text-green-600">245</p>
  //       </div>
  //       <div className="bg-gradient-to-r from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200">
  //         <h3 className="text-sm font-medium text-purple-800">En affinage</h3>
  //         <p className="text-2xl font-bold text-purple-600">20</p>
  //       </div>
  //       <div className="bg-gradient-to-r from-yellow-50 to-yellow-100 p-4 rounded-lg border border-yellow-200">
  //         <h3 className="text-sm font-medium text-yellow-800">Références</h3>
  //         <p className="text-2xl font-bold text-yellow-600">5</p>
  //       </div>
  //     </div>

  //     <div className="bg-white rounded-lg shadow-sm border border-gray-200">
  //       <div className="overflow-x-auto">
  //         <table className="w-full">
  //           <thead className="bg-gray-50">
  //             <tr>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Produit</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Stock</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Valeur unitaire</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Valeur totale</th>
  //               <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Statut</th>
  //             </tr>
  //           </thead>
  //           <tbody className="bg-white divide-y divide-gray-200">
  //             {inventaireData.map((item) => (
  //               <tr key={item.id} className="hover:bg-gray-50">
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{item.produit}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{item.stock}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatCurrency(item.valeurUnitaire)}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{formatCurrency(item.stock * item.valeurUnitaire)}</td>
  //                 <td className="px-6 py-4 whitespace-nowrap">{getStatusBadge(item.statut)}</td>
  //               </tr>
  //             ))}
  //           </tbody>
  //         </table>
  //       </div>
  //     </div>
  //   </div>
  // );

  const BilanTab = () => {
    const totalRecettes = 2450000;
    const totalDepenses = 1850000;
    const totalSalaires = 1615000;
    const beneficeNet = totalRecettes - totalDepenses - totalSalaires;

    return (
      <div className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-gradient-to-r from-green-50 to-green-100 p-6 rounded-lg border border-green-200">
            <h3 className="text-sm font-medium text-green-800 mb-2">Total Revenus</h3>
            <p className="text-3xl font-bold text-green-600">{formatCurrency(totalRecettes)}</p>
            <p className="text-xs text-green-700 mt-1">Ce mois</p>
          </div>
          <div className="bg-gradient-to-r from-red-50 to-red-100 p-6 rounded-lg border border-red-200">
            <h3 className="text-sm font-medium text-red-800 mb-2">Total Dépenses</h3>
            <p className="text-3xl font-bold text-red-600">{formatCurrency(totalDepenses)}</p>
            <p className="text-xs text-red-700 mt-1">Ce mois</p>
          </div>
          <div className="bg-gradient-to-r from-indigo-50 to-indigo-100 p-6 rounded-lg border border-indigo-200">
            <h3 className="text-sm font-medium text-indigo-800 mb-2">Total Salaires</h3>
            <p className="text-3xl font-bold text-indigo-600">{formatCurrency(totalSalaires)}</p>
            <p className="text-xs text-indigo-700 mt-1">Ce mois</p>
          </div>
          <div className={`bg-gradient-to-r p-6 rounded-lg border ${beneficeNet >= 0 ? 'from-emerald-50 to-emerald-100 border-emerald-200' : 'from-red-50 to-red-100 border-red-200'}`}>
            <h3 className={`text-sm font-medium mb-2 ${beneficeNet >= 0 ? 'text-emerald-800' : 'text-red-800'}`}>Bénéfice Net</h3>
            <p className={`text-3xl font-bold ${beneficeNet >= 0 ? 'text-emerald-600' : 'text-red-600'}`}>{formatCurrency(beneficeNet)}</p>
            <p className={`text-xs mt-1 ${beneficeNet >= 0 ? 'text-emerald-700' : 'text-red-700'}`}>Ce mois</p>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Répartition des dépenses</h3>
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">Matières premières</span>
                <span className="text-sm font-medium text-gray-900">{formatCurrency(750000)}</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-blue-500 h-2 rounded-full" style={{ width: '40.5%' }}></div>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">Salaires</span>
                <span className="text-sm font-medium text-gray-900">{formatCurrency(1615000)}</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-green-500 h-2 rounded-full" style={{ width: '87.3%' }}></div>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">Transport</span>
                <span className="text-sm font-medium text-gray-900">{formatCurrency(185000)}</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-yellow-500 h-2 rounded-full" style={{ width: '10%' }}></div>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">Énergie</span>
                <span className="text-sm font-medium text-gray-900">{formatCurrency(135000)}</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-purple-500 h-2 rounded-full" style={{ width: '7.3%' }}></div>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">Emballage</span>
                <span className="text-sm font-medium text-gray-900">{formatCurrency(75000)}</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div className="bg-red-500 h-2 rounded-full" style={{ width: '4.1%' }}></div>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Évolution mensuelle</h3>
            <div className="space-y-4">
              <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                <span className="text-sm font-medium text-gray-700">Avril 2025</span>
                <span className="text-sm font-bold text-green-600">{formatCurrency(-125000)}</span>
              </div>
              <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                <span className="text-sm font-medium text-gray-700">Mai 2025</span>
                <span className="text-sm font-bold text-green-600">{formatCurrency(235000)}</span>
              </div>
              <div className="flex justify-between items-center p-3 bg-blue-50 rounded-lg border border-blue-200">
                <span className="text-sm font-medium text-blue-700">Juin 2025</span>
                <span className="text-sm font-bold text-blue-600">{formatCurrency(beneficeNet)}</span>
              </div>
              <div className="mt-4 p-3 bg-gradient-to-r from-green-50 to-emerald-50 rounded-lg border border-green-200">
                <p className="text-sm text-green-800">
                  <span className="font-medium">Tendance positive:</span> Amélioration de {formatCurrency(Math.abs(beneficeNet - 235000))} par rapport au mois précédent
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  };

  const FacturationTab = () => (
    <div className="space-y-6">
      <SearchAndFilter
        tab="facturation"
        searchTerm={searchTerms.facturation}
        onSearchChange={(term) => setSearchTerms({ ...searchTerms, facturation: term })}
        filter={filters.facturation}
        onFilterChange={(filter) => setFilters({ ...filters, facturation: filter })}
      />

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg border border-blue-200">
          <h3 className="text-sm font-medium text-blue-800">Facturé ce mois</h3>
          <p className="text-2xl font-bold text-blue-600">{formatCurrency(515000)}</p>
        </div>
        <div className="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg border border-green-200">
          <h3 className="text-sm font-medium text-green-800">Encaissé</h3>
          <p className="text-2xl font-bold text-green-600">{formatCurrency(315000)}</p>
        </div>
        <div className="bg-gradient-to-r from-red-50 to-red-100 p-4 rounded-lg border border-red-200">
          <h3 className="text-sm font-medium text-red-800">En attente</h3>
          <p className="text-2xl font-bold text-red-600">{formatCurrency(200000)}</p>
        </div>
        <div className="bg-gradient-to-r from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200">
          <h3 className="text-sm font-medium text-purple-800">Factures</h3>
          <p className="text-2xl font-bold text-purple-600">3</p>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">N° Facture</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Client</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Montant total</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Montant payé</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Restant dû</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Paiement</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Statut</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {facturationData.map((facture) => (
                <tr key={facture.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{facture.numeroFacture}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{facture.client}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatDate(facture.dateFacturation)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{formatCurrency(facture.montantTotal)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatCurrency(facture.montantPaye)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatCurrency(facture.montantTotal - facture.montantPaye)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{facture.paiement}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{getStatusBadge(facture.statut)}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <button className="text-blue-600 hover:text-blue-900 mr-2">
                      <Eye className="h-4 w-4" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );

  const TresorerieTab = () => (
    <div className="space-y-6">
      <SearchAndFilter
        tab="tresorerie"
        searchTerm={searchTerms.tresorerie}
        onSearchChange={(term) => setSearchTerms({ ...searchTerms, tresorerie: term })}
        filter={filters.tresorerie}
        onFilterChange={(filter) => setFilters({ ...filters, tresorerie: filter })}
      />

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-gradient-to-r from-emerald-50 to-emerald-100 p-4 rounded-lg border border-emerald-200">
          <h3 className="text-sm font-medium text-emerald-800">Trésorerie totale</h3>
          <p className="text-2xl font-bold text-emerald-600">{formatCurrency(2375000)}</p>
        </div>
        <div className="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg border border-blue-200">
          <h3 className="text-sm font-medium text-blue-800">Comptes bancaires</h3>
          <p className="text-2xl font-bold text-blue-600">{formatCurrency(2000000)}</p>
        </div>
        <div className="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg border border-green-200">
          <h3 className="text-sm font-medium text-green-800">Caisse</h3>
          <p className="text-2xl font-bold text-green-600">{formatCurrency(250000)}</p>
        </div>
        <div className="bg-gradient-to-r from-purple-50 to-purple-100 p-4 rounded-lg border border-purple-200">
          <h3 className="text-sm font-medium text-purple-800">Mobile Money</h3>
          <p className="text-2xl font-bold text-purple-600">{formatCurrency(125000)}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-semibold text-gray-900">Comptes</h3>
          </div>
          <div className="p-6">
            <div className="space-y-4">
              {tresorerieData.map((compte) => (
                <div key={compte.id} className="flex justify-between items-center p-4 bg-gray-50 rounded-lg">
                  <div>
                    <p className="font-medium text-gray-900">{compte.compte}</p>
                    <p className="text-sm text-gray-500 capitalize">{compte.type}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-lg font-bold text-gray-900">{formatCurrency(compte.solde)}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-semibold text-gray-900">Mouvements récents</h3>
          </div>
          <div className="p-6">
            <div className="space-y-4">
              <div className="flex justify-between items-center p-4 border-l-4 border-green-400 bg-green-50">
                <div>
                  <p className="font-medium text-gray-900">Virement reçu</p>
                  <p className="text-sm text-gray-500">Restaurant Le Gourmet</p>
                </div>
                <div className="text-right">
                  <p className="text-lg font-bold text-green-600">+{formatCurrency(165000)}</p>
                  <p className="text-sm text-gray-500">Hier</p>
                </div>
              </div>
              <div className="flex justify-between items-center p-4 border-l-4 border-red-400 bg-red-50">
                <div>
                  <p className="font-medium text-gray-900">Paiement fournisseur</p>
                  <p className="text-sm text-gray-500">Laiterie Bio</p>
                </div>
                <div className="text-right">
                  <p className="text-lg font-bold text-red-600">-{formatCurrency(125000)}</p>
                  <p className="text-sm text-gray-500">Avant-hier</p>
                </div>
              </div>
              <div className="flex justify-between items-center p-4 border-l-4 border-blue-400 bg-blue-50">
                <div>
                  <p className="font-medium text-gray-900">Vente espèces</p>
                  <p className="text-sm text-gray-500">Épicerie Martin</p>
                </div>
                <div className="text-right">
                  <p className="text-lg font-bold text-blue-600">+{formatCurrency(150000)}</p>
                  <p className="text-sm text-gray-500">Il y a 3 jours</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
  const renderTabContent = () => {
    switch (activeTab) {
      case 'recettes': return <RecettesTab />;
      case 'depenses': return <DepensesTab />;
      case 'salaires': return <SalairesTab />;
      case 'inventaire': return <InventaireTab />;
      case 'bilan': return <BilanTab />;
      case 'facturation': return <FacturationTab />;
      case 'tresorerie': return <TresorerieTab />;
      default: return <RecettesTab />;
    }
  };

  const getTabTitle = () => {
    const tabTitles = {
      'recettes': 'Recettes (Revenus)',
      'depenses': 'Dépenses',
      'salaires': 'Salaires',
      'inventaire': 'Inventaire valorisé',
      'bilan': 'Bilan financier',
      'facturation': 'Facturation & paiements clients',
      'tresorerie': 'Trésorerie & mouvements bancaires'
    };
    return tabTitles[activeTab] || 'Comptabilité';
  };

  return (
  <div className="dashboard-container min-h-screen bg-gray-50">
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

      {/* Votre contenu existant */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 containerCompta">
        {/* Navigation des onglets */}
        <div className="mb-8">
          <div className="border-b border-gray-200">
            <nav className="flex space-x-8 overflow-x-auto lienHeader">
              {tabs.map((tab) => {
                const Icon = tab.icon;
                return (
                  <button
                    key={tab.id}
                    onClick={() => setActiveTab(tab.id)}
                    className={`flex items-center bouttonLien space-x-2 py-4 px-1 border-b-2 font-medium text-sm whitespace-nowrap ${
                      activeTab === tab.id
                        ? 'border-blue-500 text-white-600'
                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                    }`}
                  >
                    <Icon className="h-4 w-4" />
                    <span>{tab.label}</span>
                  </button>
                );
              })}
            </nav>
          </div>
        </div>

        {/* Titre de la section */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">{getTabTitle()}</h1>
          <p className="text-gray-600 mt-2">Gestion comptable de votre fromagerie artisanale</p>
        </div>

        {/* Contenu de l'onglet actif */}
        {loading ? (
          <div className="flex justify-center items-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
            <span className="ml-2 text-gray-600">Chargement des données...</span>
          </div>
        ) : error ? (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 text-center">
            <p className="text-red-600">{error}</p>
            <button 
              onClick={loadComptabiliteData}
              className="mt-2 px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
            >
              Réessayer
            </button>
          </div>
        ) : (
          renderTabContent()
        )}
      </div>
    </div>
  </div>
);
};
export default Comptabilite;
