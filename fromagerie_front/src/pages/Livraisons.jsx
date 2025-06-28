import React from 'react';
import '../assets/css/Livraison.css';

const Livraison = () => {
  // Données des livraisons
  const livraisons = [
    {
      id: 1,
      fromagerie: "Fromagerie Martin",
      client: "Anosibe.PIZZAMANIA",
      montant: "200 000 Ar",
      produits: "Couda affiné : 50 kg",
      livreur: "Jean Dupont",
      horaire: "10:30 - 11:45"
    },
    {
      id: 2,
      fromagerie: "Fromagerie Martin",
      client: "Anosibe.PIZZAMANIA",
      montant: "200 000 Ar",
      produits: "Couda affiné : 50 kg",
      livreur: "Jean Dupont",
      horaire: "10:30 - 11:45"
    },
    {
      id: 3,
      fromagerie: "Fromagerie Martin",
      client: "Anosibe.PIZZAMANIA",
      montant: "200 000 Ar",
      produits: "Couda affiné : 50 kg",
      livreur: "Jean Dupont",
      horaire: "10:30 - 11:45"
    }
  ];

  // Statistiques
  const stats = {
    livraisonsDuJour: 8,
    livreursActifs: 2,
    distanceOptimisee: "45.2 km",
    tempsEstime: "2h 30min"
  };

  return (
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
          <input type="text" placeholder="Rechercher..." />
        </div>
        <div className="livreurs-stat">
          <p className="livreurs-count">{stats.livreursActifs}</p>
          <p className="livreurs-label">Livreurs actifs</p>
        </div>
      </div>

      {/* Liste des livraisons */}
      <div className="livraison-list">
        {livraisons.map(livraison => (
          <div key={livraison.id} className="livraison-card">
            <div className="livraison-header">
              <h3>{livraison.fromagerie}</h3>
              <span className="client-name">{livraison.client}</span>
            </div>
            <div className="livraison-details">
              <p><strong>Montant :</strong> {livraison.montant}</p>
              <p><strong>Produits :</strong> {livraison.produits}</p>
              <p><strong>Livreur :</strong> {livraison.livreur}</p>
              <p><strong>Prévu :</strong> {livraison.horaire}</p>
            </div>
            <div className="livraison-actions">
              <button className="track-btn">Suivre</button>
              <button className="contact-btn">Contacter</button>
            </div>
          </div>
        ))}
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
  );
};

export default Livraison;