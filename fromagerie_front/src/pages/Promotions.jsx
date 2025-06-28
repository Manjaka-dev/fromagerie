import React from 'react';
import '../assets/css/Promotions.css';

const Promotions = () => {
  // Données des promotions
  const promotions = [
    {
      id: 1,
      nom: "Pack Famille",
      description: "Réduction sur l'achat de 3 fromages Couda ou plus",
      reduction: "25 %",
      periode: "Du : 01/06/2025 au 01/07/2025"
    },
    {
      id: 2,
      nom: "Clients fidèles",
      description: "Remise exclusive pour nos clients réguliers",
      reduction: "25 %",
      periode: "Du : 01/06/2025 au 01/07/2025"
    },
    {
      id: 3,
      nom: "Offres été Couda",
      description: "Promotion spéciale pour les fromages Couda artisanaux pendant la saison estivale",
      reduction: "25 %",
      periode: "Du : 01/06/2025 au 01/07/2025"
    }
  ];

  // Statistiques
  const stats = {
    promotionsActives: 2,
    reductionMoyenne: "15 %",
    economiesClients: "2 500 Ar",
    promotionsExpirees: 1
  };

  return (
    <div className="promotion-container">
      <h1>GESTION PROMOTION</h1>
      
      {/* Section Statistiques */}
      <div className="stats-grid">
        <div className="stat-card">
          <h3>Promotions Actives</h3>
          <p className="stat-value">{stats.promotionsActives}</p>
        </div>
        
        <div className="stat-card">
          <h3>Réduction Moyenne</h3>
          <p className="stat-value">{stats.reductionMoyenne}</p>
        </div>
        
        <div className="stat-card">
          <h3>Economies Clients</h3>
          <p className="stat-value">{stats.economiesClients}</p>
        </div>
        
        <div className="stat-card">
          <h3>Promotions exprimées</h3>
          <p className="stat-value">{stats.promotionsExpirees}</p>
        </div>
      </div>

      <div className="divider"></div>

      {/* Liste des promotions */}
      <h2>Listes des promotions :</h2>
      
      <div className="promotion-list">
        {promotions.map(promo => (
          <div key={promo.id} className="promotion-card">
            <div className="promotion-header">
              <h3>{promo.nom}</h3>
              <span className="reduction-badge">{promo.reduction}</span>
            </div>
            <p className="promotion-description">{promo.description}</p>
            <p className="promotion-period">{promo.periode}</p>
            <div className="promotion-actions">
              <button className="edit-btn">Modifier</button>
              <button className="delete-btn">Supprimer</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Promotions;