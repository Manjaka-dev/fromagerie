import React, { useState } from 'react';
import { Search, Calendar, BarChart3, TrendingUp, Package, Truck, Settings, Factory } from 'lucide-react';
import './vente.css'
const Vente = () => {
  const [selectedDays, setSelectedDays] = useState('7 jours');
  const [searchTerm, setSearchTerm] = useState('');

  const stockData = [
    {
      categorie: 'website.net',
      produit: '4321',
      dateFabrication: '4321',
      datePeremption: '4321',
      quantite: '4321'
    },
    {
      categorie: 'website.net',
      produit: '4321',
      dateFabrication: '-8%',
      datePeremption: '4033',
      quantite: '+84%'
    },
    {
      categorie: 'website.net',
      produit: '4321',
      dateFabrication: '+2%',
      datePeremption: '+84%',
      quantite: '+84%'
    },
    {
      categorie: 'website.net',
      produit: '3128',
      dateFabrication: '+33%',
      datePeremption: '4321',
      quantite: '4321'
    }
  ];

  const menuItems = [
    { icon: BarChart3, label: 'Stock', active: true },
    { icon: TrendingUp, label: 'Statistiques', active: false },
    { icon: Package, label: 'Comptabilité', active: false },
    { icon: Factory, label: 'Ventes', active: false },
    { icon: Truck, label: 'Livraisons', active: false },
    { icon: Settings, label: 'Administration', active: false },
    { icon: Factory, label: 'Production', active: false }
  ];

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <div className="w-64 bg-white shadow-sm border-r border-gray-200">
        <div className="p-6">
          <h1 className="text-2xl font-bold text-blue-600">CheeseFlow</h1>
        </div>
        
        <div className="px-4 mb-6">
          <h2 className="text-sm font-medium text-gray-700 mb-4">Tableau de Bord</h2>
        </div>

        <nav className="px-4 space-y-1">
          {menuItems.map((item, index) => {
            const Icon = item.icon;
            return (
              <div
                key={index}
                className={`flex items-center px-3 py-2 rounded-lg cursor-pointer transition-colors ${
                  item.active 
                    ? 'bg-blue-50 text-blue-600 font-medium' 
                    : 'text-gray-600 hover:bg-gray-50'
                }`}
              >
                <Icon className="w-5 h-5 mr-3" />
                <span className="text-sm">{item.label}</span>
              </div>
            );
          })}
        </nav>
      </div>

      {/* Main Content */}
      <div className="flex-1 p-8">
        {/* Header */}
        <div className="flex items-center justify-between mb-8">
          <div className="flex items-center space-x-4">
            {/* Search Bar */}
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <input
                type="text"
                placeholder="Rechercher un client..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent w-80"
              />
            </div>

            {/* Date Filter */}
            <div className="flex items-center space-x-2 px-4 py-2 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50">
              <Calendar className="w-4 h-4 text-gray-500" />
              <span className="text-sm text-gray-700">{selectedDays}</span>
            </div>

            {/* Simulate Production Button */}
            <button className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors text-sm font-medium">
              Simuler production
            </button>
          </div>

          {/* User Avatar */}
          <div className="w-8 h-8 bg-orange-500 rounded-full flex items-center justify-center">
            <span className="text-white text-sm font-medium">U</span>
          </div>
        </div>

        {/* Stock Table */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          <div className="p-6 border-b border-gray-200">
            <h2 className="text-lg font-semibold text-gray-900">Stock Produits Finis</h2>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Catégorie
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Produit
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Date Fabrication
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Date peremption
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Quantité
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {stockData.map((row, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {row.categorie}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-blue-600 font-medium">
                      {row.produit}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <span className={`${
                        row.dateFabrication.includes('%') 
                          ? row.dateFabrication.includes('-') 
                            ? 'text-red-600' 
                            : 'text-green-600'
                          : 'text-gray-900'
                      }`}>
                        {row.dateFabrication}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <span className={`${
                        row.datePeremption.includes('%') 
                          ? 'text-green-600'
                          : 'text-gray-900'
                      }`}>
                        {row.datePeremption}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <span className={`${
                        row.quantite.includes('%') 
                          ? 'text-green-600'
                          : 'text-gray-900'
                      }`}>
                        {row.quantite}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export { Vente };