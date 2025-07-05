import React, { useState } from 'react';
import { Search, BarChart3, TrendingUp, Calculator, ShoppingCart, Truck, Users, Factory, ChevronLeft, ChevronRight } from 'lucide-react';
import './stock.css'
const Stock = () => {
  const [selectedMatiere, setSelectedMatiere] = useState('');
  const [typeMouvement, setTypeMouvement] = useState('');
  const [quantite, setQuantite] = useState('');
  const [dateMouvement, setDateMouvement] = useState('');
  const [commentaire, setCommentaire] = useState('');

  const menuItems = [
    { id: 'stock', label: 'Stock', icon: BarChart3, active: false },
    { id: 'statistiques', label: 'Statistiques', icon: TrendingUp, active: false },
    { id: 'comptabilite', label: 'Comptabilité', icon: Calculator, active: false },
    { id: 'ventes', label: 'Ventes', icon: ShoppingCart, active: true },
    { id: 'livraisons', label: 'Livraisons', icon: Truck, active: false },
    { id: 'administration', label: 'Administration', icon: Users, active: false },
    { id: 'production', label: 'Production', icon: Factory, active: false },
  ];

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log({
      selectedMatiere,
      typeMouvement,
      quantite,
      dateMouvement,
      commentaire
    });
  };

  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar */}
      <div className="w-64 bg-white border-r border-gray-200 flex flex-col">
        {/* Header */}
        <div className="p-6 border-b border-gray-200">
          <h1 className="text-2xl font-bold text-blue-600">CheeseFlow</h1>
        </div>

        {/* Navigation */}
        <nav className="flex-1 px-4 py-6">
          <div className="mb-6">
            <h2 className="text-sm font-medium text-gray-900 mb-3">Tableau de Bord</h2>
          </div>
          
          <div className="space-y-1">
            {menuItems.map((item) => {
              const Icon = item.icon;
              return (
                <button
                  key={item.id}
                  className={`w-full flex items-center px-3 py-2 text-sm font-medium rounded-md transition-colors ${
                    item.active
                      ? 'bg-blue-50 text-blue-600 border-r-2 border-blue-600'
                      : 'text-gray-700 hover:bg-gray-50 hover:text-gray-900'
                  }`}
                >
                  <Icon className="mr-3 h-5 w-5" />
                  {item.label}
                </button>
              );
            })}
          </div>
        </nav>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        {/* Top Bar */}
        <div className="bg-white border-b border-gray-200 px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                <input
                  type="text"
                  placeholder="Rechercher..."
                  className="pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
              <div className="flex items-center space-x-2">
                <span className="text-sm text-gray-600">Filtre</span>
                <button className="p-1">
                  <ChevronLeft className="h-4 w-4 text-gray-400" />
                </button>
              </div>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-sm text-gray-600">Simuler production</span>
              <button className="bg-black text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-gray-800 transition-colors">
                Se connecter
              </button>
            </div>
          </div>
        </div>

        {/* Form Content */}
        <div className="flex-1 flex items-center justify-center p-8">
          <div className="w-full max-w-md">
            <div className="space-y-4">
              {/* Navigation arrows */}
              <div className="flex justify-between items-center mb-8">
                <button type="button" className="p-2 hover:bg-gray-100 rounded-full">
                  <ChevronLeft className="h-6 w-6 text-gray-400" />
                </button>
                <button type="button" className="p-2 hover:bg-gray-100 rounded-full">
                  <ChevronRight className="h-6 w-6 text-gray-400" />
                </button>
              </div>

              {/* Matière première dropdown */}
              <div className="relative">
                <select
                  value={selectedMatiere}
                  onChange={(e) => setSelectedMatiere(e.target.value)}
                  className="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent appearance-none bg-white text-gray-500"
                >
                  <option value="">matière première</option>
                  <option value="lait">Lait</option>
                  <option value="creme">Crème</option>
                  <option value="fromage">Fromage</option>
                </select>
                <ChevronLeft className="absolute right-3 top-1/2 transform -translate-y-1/2 rotate-90 h-4 w-4 text-gray-400 pointer-events-none" />
              </div>

              {/* Type mouvement */}
              <input
                type="text"
                placeholder="type mouvement"
                value={typeMouvement}
                onChange={(e) => setTypeMouvement(e.target.value)}
                className="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-500"
              />

              {/* Quantité */}
              <input
                type="number"
                placeholder="quantité"
                value={quantite}
                onChange={(e) => setQuantite(e.target.value)}
                className="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-500"
              />

              {/* Date mouvement */}
              <input
                type="date"
                placeholder="date mouvement"
                value={dateMouvement}
                onChange={(e) => setDateMouvement(e.target.value)}
                className="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-500 text-gray-500"
              />

              {/* Commentaire */}
              <textarea
                placeholder="commentaire"
                value={commentaire}
                onChange={(e) => setCommentaire(e.target.value)}
                rows={3}
                className="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-500 resize-none"
              />

              {/* Submit button */}
              <button
                type="button"
                onClick={handleSubmit}
                className="w-full bg-black text-white py-3 rounded-md font-medium hover:bg-gray-800 transition-colors mt-6"
              >
                Valider
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export { Stock };