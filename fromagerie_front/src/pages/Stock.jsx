import React, { useState, useMemo, useEffect } from 'react';
import '../assets/styles/Stock.css';
import { NavLink } from 'react-router-dom';
import SidebarMenu from "../components/SidebarMenu";
import {
    Package,
    AlertTriangle,
    Plus,
    Search,
    Calendar,
    TrendingUp,
    ShoppingCart,
    Calculator,
    User,
    Bell,
    ChevronDown,
    Clock,
    BarChart3, Truck, Settings, Factory, Eye,
    Trash2,
    Download, Edit
} from 'lucide-react';
import { stockAPI, formatDate } from '../services/api';

const Stock = () => {
    const [activeTab, setActiveTab] = useState('stock');
    const [searchQuery, setSearchQuery] = useState('');
    const [activeStockType, setActiveStockType] = useState('matieres');
    const [searchTerm, setSearchTerm] = useState('');
    const [filterType, setFilterType] = useState('all');
    
    // États pour les données du backend
    const [matieresPremieres, setMatieresPremieres] = useState([]);
    const [produitsFinis, setProduitsFinis] = useState([]);
    const [mouvementsStock, setMouvementsStock] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Charger les données au montage
    useEffect(() => {
        loadStockData();
    }, []);

    const loadStockData = async () => {
        setLoading(true);
        setError(null);
        try {
            // Charger les matières premières depuis l'API
            const matieres = await stockAPI.getAllMatieresPremiere();
            setMatieresPremieres(matieres || []);

            // Pour les produits finis, utiliser l'API produits
            const produits = await produitAPI.getAllProduits();
            // Adapter les données des produits pour l'affichage stock
            const produitsFinis = produits.produits?.map(produit => ({
                id: produit.id,
                nom: produit.nom,
                unite: produit.unite || 'Unités',
                quantiteActuelle: produit.quantiteDisponible || 0,
                seuilAlerte: 10,
                seuilCritique: 5,
                prixUnitaire: produit.prix || 0,
                status: produit.quantiteDisponible > 10 ? 'normal' : 
                       produit.quantiteDisponible > 5 ? 'alerte' : 'critique'
            })) || [];
            setProduitsFinis(produitsFinis);

            // Charger les mouvements de stock récents
            const dateDebut = new Date();
            dateDebut.setDate(dateDebut.getDate() - 30);
            const mouvements = await stockAPI.getMouvementsStock(
                dateDebut.toISOString().split('T')[0],
                new Date().toISOString().split('T')[0]
            );
            setMouvementsStock(mouvements || []);

        } catch (err) {
            console.error('Erreur lors du chargement des données de stock:', err);
            setError('Impossible de charger les données de stock. Veuillez vérifier que le backend est démarré.');
        } finally {
            setLoading(false);
        }
    };

    const alertes = [
        {
            id: 1,
            matiere: "Présure",
            type: "Stock faible",
            message: "Stock critique: 85.2 ml restant",
            date: "2024-07-05",
            priorite: "haute"
        },
        {
            id: 2,
            matiere: "Sel de mer",
            type: "Réapprovisionnement",
            message: "Seuil d'alerte atteint",
            date: "2024-07-04",
            priorite: "moyenne"
        }
    ];

    const getStatusColor = (status) => {
        switch (status) {
            case 'critique': return 'text-red-600 bg-red-50';
            case 'alerte': return 'text-orange-600 bg-orange-50';
            default: return 'text-green-600 bg-green-50';
        }
    };

    const getStatusText = (status) => {
        switch (status) {
            case 'critique': return 'Critique';
            case 'alerte': return 'Alerte';
            default: return 'Normal';
        }
    };

    const getStatutProduitColor = (statut) => {
        switch (statut) {
            case 'critique': return 'text-red-600 bg-red-50';
            case 'en_affinage': return 'text-blue-600 bg-blue-50';
            case 'pret_vente': return 'text-green-600 bg-green-50';
            case 'premium': return 'text-purple-600 bg-purple-50';
            default: return 'text-gray-600 bg-gray-50';
        }
    };

    const getStatutProduitText = (statut) => {
        switch (statut) {
            case 'critique': return 'Stock Critique';
            case 'en_affinage': return 'En Affinage';
            case 'pret_vente': return 'Prêt à la Vente';
            case 'premium': return 'Premium';
            default: return 'Inconnu';
        }
    };

    // Optimisation des filtres avec useMemo
    const filteredMatieres = useMemo(() => {
        return matieresPremieres.filter(matiere => {
            const matchesSearch = matiere.nom.toLowerCase().includes(searchTerm.toLowerCase());
            const matchesFilter = filterType === 'all' || matiere.status === filterType;
            return matchesSearch && matchesFilter;
        });
    }, [matieresPremieres, searchTerm, filterType]);

    const filteredProduits = useMemo(() => {
        return produitsFinis.filter(produit => {
            const matchesSearch = produit.nom.toLowerCase().includes(searchTerm.toLowerCase());
            const matchesFilter = filterType === 'all' || produit.statut === filterType;
            return matchesSearch && matchesFilter;
        });
    }, [produitsFinis, searchTerm, filterType]);

    return (
        <div className="dashboard-container min-h-screen bg-gray-50">
            <div className="sidebar">
                <div className="sidebar-header">
                    <h1 className="app-title">CheeseFlow</h1>
                    <p className="app-subtitle">Production</p>
                    <p className="app-subtitle">Gouda Artisanale</p>
                </div>

                <SidebarMenu />
            </div>
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
                <div className="containerCompta">
                    <div className="mb-6 contenuesStock">
                        <div className="flex items-center justify-between mb-4">
                            <div>
                                <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-2">
                                    <Package className="w-8 h-8 text-blue-600" />
                                    Stock Matières Premières
                                </h1>
                                <p className="text-gray-600 mt-1">Gestion et suivi des matières premières - Fromagerie Artisanale</p>
                            </div>
                            <button className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center gap-2">
                                <Plus className="w-4 h-4" />
                                Nouvelle matière
                            </button>
                        </div>

                        {/* Stats Cards */}
                        <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
                            <div className="bg-white p-4 rounded-lg shadow-sm border">
                                <div className="flex items-center justify-between">
                                    <div>
                                        <p className="text-sm text-gray-600">Matières Premières</p>
                                        <p className="text-2xl font-bold text-gray-900">{matieresPremieres.length}</p>
                                    </div>
                                    <Package className="w-8 h-8 text-blue-500" />
                                </div>
                            </div>
                            <div className="bg-white p-4 rounded-lg shadow-sm border">
                                <div className="flex items-center justify-between">
                                    <div>
                                        <p className="text-sm text-gray-600">Produits Finis</p>
                                        <p className="text-2xl font-bold text-gray-900">{produitsFinis.length}</p>
                                    </div>
                                    <Package className="w-8 h-8 text-green-500" />
                                </div>
                            </div>
                            <div className="bg-white p-4 rounded-lg shadow-sm border">
                                <div className="flex items-center justify-between">
                                    <div>
                                        <p className="text-sm text-gray-600">Alertes Actives</p>
                                        <p className="text-2xl font-bold text-orange-600">{alertes.length}</p>
                                    </div>
                                    <AlertTriangle className="w-8 h-8 text-orange-500" />
                                </div>
                            </div>
                            <div className="bg-white p-4 rounded-lg shadow-sm border">
                                <div className="flex items-center justify-between">
                                    <div>
                                        <p className="text-sm text-gray-600">En Affinage</p>
                                        <p className="text-2xl font-bold text-blue-600">{produitsFinis.filter(p => p.statut === 'en_affinage').length}</p>
                                    </div>
                                    <Calendar className="w-8 h-8 text-blue-500" />
                                </div>
                            </div>
                        </div>
                    </div>
                    {/* Navigation Tabs */}
                    <div className="border-b border-gray-200 mb-6 lienStock">
                        <nav className="flex space-x-8" role="tablist">
                            {[
                                { id: 'stock', label: 'Stocks', icon: Package },
                                { id: 'mouvements', label: 'Mouvements', icon: TrendingUp },
                                { id: 'simulation', label: 'Simulation Production', icon: BarChart3 },
                                { id: 'alertes', label: 'Alertes', icon: AlertTriangle }
                            ].map(tab => (
                                <button
                                    key={tab.id}
                                    onClick={() => setActiveTab(tab.id)}
                                    className={`flex items-center gap-2 py-2 px-1 border-b-2 font-medium text-sm transition-colors ${activeTab === tab.id
                                        ? 'border-blue-500 text-blue-600'
                                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                                        }`}
                                    role="tab"
                                    aria-selected={activeTab === tab.id}
                                    aria-controls={`panel-${tab.id}`}
                                >
                                    <tab.icon className="w-4 h-4" />
                                    {tab.label}
                                </button>
                            ))}
                        </nav>
                    </div>

                    {/* Stock Tab */}
                    {activeTab === 'stock' && (
                        <div className="contenusStockTab">
                            <div id="panel-stock " role="tabpanel">
                                {/* Stock Type Toggle */}
                                <div className="bg-white p-4 rounded-lg shadow-sm border mb-4">
                                    <div className="flex items-center gap-4 typeStock">
                                        <span className="text-sm font-medium text-gray-700">Type de stock :</span>
                                        <div className="flex rounded-lg border border-gray-200 overflow-hidden btnTypeStock">
                                            <button
                                                onClick={() => setActiveStockType('matieres')}
                                                className={`px-4 py-2 text-sm font-medium transition-colors ${activeStockType === 'matieres'
                                                    ? 'bg-blue-600 text-white'
                                                    : 'bg-white text-gray-700 hover:bg-gray-50'
                                                    }`}
                                                aria-pressed={activeStockType === 'matieres'}
                                            >
                                                Matières Premières
                                            </button>
                                            <button
                                                onClick={() => setActiveStockType('produits')}
                                                className={`px-4 py-2 text-sm font-medium transition-colors ${activeStockType === 'produits'
                                                    ? 'bg-blue-600 text-white'
                                                    : 'bg-white text-gray-700 hover:bg-gray-50'
                                                    }`}
                                                aria-pressed={activeStockType === 'produits'}
                                            >
                                                Produits Finis
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                {/* Filters */}
                                <div className="bg-white p-4 rounded-lg shadow-sm border mb-6">
                                    <div className="flex flex-col md:flex-row gap-4 items-center">
                                        <div className="flex-1 relative">
                                            <label htmlFor="search" className="sr-only">
                                                Rechercher une matière première ou un produit fini
                                            </label>
                                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                                            <input
                                                id="search"
                                                type="text"
                                                placeholder={`Rechercher ${activeStockType === 'matieres' ? 'une matière première' : 'un produit fini'}...`}
                                                value={searchTerm}
                                                onChange={(e) => setSearchTerm(e.target.value)}
                                                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                            />
                                        </div>
                                        <select
                                            value={filterType}
                                            onChange={(e) => setFilterType(e.target.value)}
                                            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent select-input"
                                            aria-label="Filtrer par statut"
                                        >
                                            <option value="all">Tous les statuts</option>
                                            {activeStockType === 'matieres' ? (
                                                <>
                                                    <option value="normal">Normal</option>
                                                    <option value="alerte">Alerte</option>
                                                    <option value="critique">Critique</option>
                                                </>
                                            ) : (
                                                <>
                                                    <option value="en_affinage">En Affinage</option>
                                                    <option value="pret_vente">Prêt à la Vente</option>
                                                    <option value="premium">Premium</option>
                                                    <option value="critique">Critique</option>
                                                </>
                                            )}
                                        </select>
                                        <button className="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 transition-colors flex items-center gap-2">
                                            <Download className="w-4 h-4" />
                                            Exporter
                                        </button>
                                    </div>
                                </div>

                                {/* Matières Premières Table */}
                                {activeStockType === 'matieres' && (
                                    <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
                                        <div className="overflow-x-auto">
                                            <table className="w-full">
                                                <thead className="bg-gray-50">
                                                    <tr>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Matière Première
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Stock Actuel
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Unité
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Statut
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Conservation
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Dernière MAJ
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Actions
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody className="bg-white divide-y divide-gray-200">
                                                    {filteredMatieres.length === 0 ? (
                                                        <tr>
                                                            <td colSpan="7" className="px-6 py-4 text-center text-gray-500">
                                                                Aucune matière première trouvée pour les critères sélectionnés.
                                                            </td>
                                                        </tr>
                                                    ) : (
                                                        filteredMatieres.map((matiere) => (
                                                            <tr key={matiere.id} className="hover:bg-gray-50">
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm font-medium text-gray-900">{matiere.nom}</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-900 font-semibold">{matiere.quantite}</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-500">{matiere.unite}</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getStatusColor(matiere.status)}`}>
                                                                        {getStatusText(matiere.status)}
                                                                    </span>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-500">{matiere.duree_conservation} jours</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-500">{matiere.dernierMouvement}</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                                                    <div className="flex items-center gap-2">
                                                                        <button className="text-blue-600 hover:text-blue-900" aria-label={`Voir ${matiere.nom}`}>
                                                                            <Eye className="w-4 h-4" />
                                                                        </button>
                                                                        <button className="text-green-600 hover:text-green-900" aria-label={`Modifier ${matiere.nom}`}>
                                                                            <Edit className="w-4 h-4" />
                                                                        </button>
                                                                        <button className="text-red-600 hover:text-red-900" aria-label={`Supprimer ${matiere.nom}`}>
                                                                            <Trash2 className="w-4 h-4" />
                                                                        </button>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        ))
                                                    )}
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                )}

                                {/* Produits Finis Table */}
                                {activeStockType === 'produits' && (
                                    <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
                                        <div className="overflow-x-auto">
                                            <table className="w-full">
                                                <thead className="bg-gray-50">
                                                    <tr>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Produit
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Lot
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Quantité
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Poids Total
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Statut
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Affinage
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Date Production
                                                        </th>
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Actions
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody className="bg-white divide-y divide-gray-200">
                                                    {filteredProduits.length === 0 ? (
                                                        <tr>
                                                            <td colSpan="8" className="px-6 py-4 text-center text-gray-500">
                                                                Aucun produit fini trouvé pour les critères sélectionnés.
                                                            </td>
                                                        </tr>
                                                    ) : (
                                                        filteredProduits.map((produit) => (
                                                            <tr key={produit.id} className="hover:bg-gray-50">
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm font-medium text-gray-900">{produit.nom}</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-500 font-mono">{produit.lot}</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-900 font-semibold">{produit.quantite}</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-900">{(produit.quantite * produit.poids_unitaire).toFixed(1)} kg</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getStatutProduitColor(produit.statut)}`}>
                                                                        {getStatutProduitText(produit.statut)}
                                                                    </span>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-500">{produit.duree_affinage} jours</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap">
                                                                    <div className="text-sm text-gray-500">{produit.date_production}</div>
                                                                </td>
                                                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                                                    <div className="flex items-center gap-2">
                                                                        <button className="text-blue-600 hover:text-blue-900" aria-label={`Voir ${produit.nom}`}>
                                                                            <Eye className="w-4 h-4" />
                                                                        </button>
                                                                        <button className="text-green-600 hover:text-green-900" aria-label={`Modifier ${produit.nom}`}>
                                                                            <Edit className="w-4 h-4" />
                                                                        </button>
                                                                        <button className="text-red-600 hover:text-red-900" aria-label={`Supprimer ${produit.nom}`}>
                                                                            <Trash2 className="w-4 h-4" />
                                                                        </button>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        ))
                                                    )}
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}
                    {/* Mouvements Tab */}
                    {activeTab === 'mouvements' && (
                        <div className="contenusStockTab">
                            <div id="panel-mouvements" role="tabpanel">
                                {/* Movement Type Toggle */}
                                <div className="bg-white p-4 rounded-lg shadow-sm border mb-4">
                                    <div className="flex items-center gap-4">
                                        <span className="text-sm font-medium text-gray-700">Type de mouvement :</span>
                                        <div className="flex rounded-lg border border-gray-200 overflow-hidden">
                                            <button
                                                onClick={() => setActiveStockType('matieres')}
                                                className={`px-4 py-2 text-sm font-medium transition-colors ${activeStockType === 'matieres'
                                                    ? 'bg-blue-600 text-white'
                                                    : 'bg-white text-gray-700 hover:bg-gray-50'
                                                    }`}
                                                aria-pressed={activeStockType === 'matieres'}
                                            >
                                                Matières Premières
                                            </button>
                                            <button
                                                onClick={() => setActiveStockType('produits')}
                                                className={`px-4 py-2 text-sm font-medium transition-colors ${activeStockType === 'produits'
                                                    ? 'bg-blue-600 text-white'
                                                    : 'bg-white text-gray-700 hover:bg-gray-50'
                                                    }`}
                                                aria-pressed={activeStockType === 'produits'}
                                            >
                                                Produits Finis
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div className="bg-white rounded-lg shadow-sm border">
                                    <div className="p-4 border-b">
                                        <h3 className="text-lg font-medium text-gray-900">
                                            Historique des Mouvements - {activeStockType === 'matieres' ? 'Matières Premières' : 'Produits Finis'}
                                        </h3>
                                    </div>
                                    <div className="overflow-x-auto">
                                        <table className="w-full">
                                            <thead className="bg-gray-50">
                                                <tr>
                                                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                        Date & Heure
                                                    </th>
                                                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                        {activeStockType === 'matieres' ? 'Matière' : 'Produit'}
                                                    </th>
                                                    {activeStockType === 'produits' && (
                                                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                            Lot
                                                        </th>
                                                    )}
                                                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                        Type
                                                    </th>
                                                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                        Quantité
                                                    </th>
                                                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                        Commentaire
                                                    </th>
                                                </tr>
                                            </thead>
                                            <tbody className="bg-white divide-y divide-gray-200">
                                                {(activeStockType === 'matieres' ? mouvements : mouvementsProduits).length === 0 ? (
                                                    <tr>
                                                        <td colSpan={activeStockType === 'produits' ? 6 : 5} className="px-6 py-4 text-center text-gray-500">
                                                            Aucun mouvement trouvé.
                                                        </td>
                                                    </tr>
                                                ) : (
                                                    (activeStockType === 'matieres' ? mouvements : mouvementsProduits).map((mouvement) => (
                                                        <tr key={mouvement.id} className="hover:bg-gray-50">
                                                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                                                {mouvement.date}
                                                            </td>
                                                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                                {activeStockType === 'matieres' ? mouvement.matiere : mouvement.produit}
                                                            </td>
                                                            {activeStockType === 'produits' && (
                                                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 font-mono">
                                                                    {mouvement.lot}
                                                                </td>
                                                            )}
                                                            <td className="px-6 py-4 whitespace-nowrap">
                                                                <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${mouvement.type === 'ENTREE' ? 'text-green-600 bg-green-50' :
                                                                    mouvement.type === 'SORTIE' ? 'text-red-600 bg-red-50' :
                                                                        'text-orange-600 bg-orange-50'
                                                                    }`}>
                                                                    {mouvement.type === 'ENTREE' ? 'Entrée' : mouvement.type === 'SORTIE' ? 'Sortie' : 'Ajustement'}
                                                                </span>
                                                            </td>
                                                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                                                {mouvement.type === 'ENTREE' ? '+' : mouvement.type === 'SORTIE' ? '-' : ''}{mouvement.quantite}
                                                            </td>
                                                            <td className="px-6 py-4 text-sm text-gray-500">
                                                                {mouvement.commentaire}
                                                            </td>
                                                        </tr>
                                                    ))
                                                )}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>


                    )}


                    {/* Simulation Production Tab */}
                    {activeTab === 'simulation' && (
                        <div className="contenusStockTab">
                            <div id="panel-simulation" role="tabpanel" className="space-y-6">
                                <div className="bg-white rounded-lg shadow-sm border p-6">
                                    <div className="flex items-center justify-between mb-4">
                                        <h3 className="text-lg font-medium text-gray-900">Simulation de Production</h3>
                                        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center gap-2">
                                            <Plus className="w-4 h-4" />
                                            Nouvelle Simulation
                                        </button>
                                    </div>
                                    <p className="text-gray-600 mb-6">
                                        Suggestions de production basées sur l'analyse des stocks et de la demande
                                    </p>

                                    <div className="space-y-4">
                                        {simulationsProduction.length === 0 ? (
                                            <div className="text-center py-4 text-gray-500">
                                                Aucune simulation de production disponible.
                                            </div>
                                        ) : (
                                            simulationsProduction.map((simulation) => (
                                                <div key={simulation.id} className="border rounded-lg p-4 hover:bg-gray-50">
                                                    <div className="flex items-center justify-between">
                                                        <div className="flex items-center gap-4">
                                                            <div className="flex-shrink-0">
                                                                <div className={`w-3 h-3 rounded-full ${simulation.priorite === 'critique' ? 'bg-red-500' :
                                                                    simulation.priorite === 'haute' ? 'bg-orange-500' :
                                                                        'bg-green-500'
                                                                    }`}></div>
                                                            </div>
                                                            <div>
                                                                <h4 className="font-medium text-gray-900">{simulation.produit}</h4>
                                                                <p className="text-sm text-gray-600">{simulation.justification}</p>
                                                            </div>
                                                        </div>
                                                        <div className="text-right">
                                                            <div className="text-lg font-semibold text-gray-900">
                                                                {simulation.quantite_suggeree} unités
                                                            </div>
                                                            <div className="text-sm text-gray-500">
                                                                {simulation.date_simulation}
                                                            </div>
                                                            <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full mt-1 ${simulation.priorite === 'critique' ? 'text-red-600 bg-red-50' :
                                                                simulation.priorite === 'haute' ? 'text-orange-600 bg-orange-50' :
                                                                    'text-green-600 bg-green-50'
                                                                }`}>
                                                                {simulation.priorite === 'critique' ? 'Critique' :
                                                                    simulation.priorite === 'haute' ? 'Haute' : 'Normale'}
                                                            </span>
                                                        </div>
                                                    </div>
                                                    <div className="mt-4 flex gap-2">
                                                        <button className="bg-blue-600 text-white px-3 py-1 rounded text-sm hover:bg-blue-700 transition-colors">
                                                            Planifier Production
                                                        </button>
                                                        <button className="bg-gray-100 text-gray-700 px-3 py-1 rounded text-sm hover:bg-gray-200 transition-colors">
                                                            Modifier
                                                        </button>
                                                    </div>
                                                </div>
                                            ))
                                        )}
                                    </div>
                                </div>
                            </div>
                        </div>

                    )}

                    {/* Alertes Tab */}
                    {activeTab === 'alertes' && (
                        <div className="contenusStockTab">
                            <div id="panel-alertes" role="tabpanel" className="space-y-4">
                                <div className="bg-white rounded-lg shadow-sm border p-4">
                                    <h3 className="text-lg font-medium text-gray-900 mb-4">Alertes de Stock</h3>
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <div className="p-4 bg-red-50 rounded-lg border border-red-200">
                                            <h4 className="font-medium text-red-900">Stocks Critiques</h4>
                                            <p className="text-2xl font-bold text-red-600">2</p>
                                            <p className="text-sm text-red-700">Nécessitent une action immédiate</p>
                                        </div>
                                        <div className="p-4 bg-orange-50 rounded-lg border border-orange-200">
                                            <h4 className="font-medium text-orange-900">Seuils d'Alerte</h4>
                                            <p className="text-2xl font-bold text-orange-600">3</p>
                                            <p className="text-sm text-orange-700">Réapprovisionnement recommandé</p>
                                        </div>
                                    </div>
                                </div>

                                {alertes.length === 0 ? (
                                    <div className="text-center py-4 text-gray-500">
                                        Aucune alerte de stock disponible.
                                    </div>
                                ) : (
                                    alertes.map((alerte) => (
                                        <div key={alerte.id} className="bg-white rounded-lg shadow-sm border p-4">
                                            <div className="flex items-center justify-between">
                                                <div className="flex items-center gap-3">
                                                    <AlertTriangle className={`w-5 h-5 ${alerte.priorite === 'haute' ? 'text-red-500' : 'text-orange-500'}`} />
                                                    <div>
                                                        <h4 className="font-medium text-gray-900">{alerte.matiere}</h4>
                                                        <p className="text-sm text-gray-600">{alerte.message}</p>
                                                    </div>
                                                </div>
                                                <div className="text-right">
                                                    <p className="text-sm text-gray-500">{alerte.date}</p>
                                                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${alerte.priorite === 'haute' ? 'text-red-600 bg-red-50' : 'text-orange-600 bg-orange-50'
                                                        }`}>
                                                        {alerte.priorite === 'haute' ? 'Haute' : 'Moyenne'}
                                                    </span>
                                                </div>
                                            </div>
                                            <div className="mt-4 flex gap-2">
                                                <button className="bg-blue-600 text-white px-3 py-1 rounded text-sm hover:bg-blue-700 transition-colors">
                                                    Commander
                                                </button>
                                                <button className="bg-gray-100 text-gray-700 px-3 py-1 rounded text-sm hover:bg-gray-200 transition-colors">
                                                    Reporter
                                                </button>
                                                <button className="bg-gray-100 text-gray-700 px-3 py-1 rounded text-sm hover:bg-gray-200 transition-colors">
                                                    Ignorer
                                                </button>
                                            </div>
                                        </div>
                                    ))
                                )}
                            </div>
                        </div>


                    )}
                </div>

            </div>
        </div>
    );
};

export default Stock;