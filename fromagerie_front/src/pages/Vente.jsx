import { FaSearch, FaTag, FaBell, FaUserCircle, FaCalendarAlt } from "react-icons/fa";
import "../assets/css/Vente.css"; 
import { Link } from 'react-router-dom';

const Ventes = () => {
  const colors = ["#f4f6ff", "#fff4f4", "#f4f6ff", "#f1fff4"];

  return (
    <div>
      {/* Top Bar */}
      <div className="ventes-top-bar">
        <input type="text" placeholder="Rechercher un client ..." className="ventes-search-input" />
        <div className="ventes-top-right">
          <button className="ventes-date-btn"><FaCalendarAlt /> 7 jours</button>
          <Link to="/promotion" className="ventes-promo-btn">
            <FaTag /> Gestion promotion
            </Link>
          <FaBell size={20} style={{ margin: '0 15px' }} />
          <FaUserCircle size={28} />
        </div>
      </div>

      {/* Résumé chiffré */}
      <div className="ventes-metrics">
        {["CA en cours", "En attente", "En preparation", "Livrés ce mois"].map((label, i) => (
          <div key={i} className="ventes-metric-card" style={{ backgroundColor: colors[i] }}>
            <h4>{label}</h4>
            <h2>$4,156.45</h2>
          </div>
        ))}
      </div>

      {/* Colonnes de commandes */}
      <div className="ventes-status-row">
        {["Confirmé", "Préparation", "Livré", "Devis"].map((status, i) => (
          <div key={i} className="ventes-column">
            <div className="ventes-column-header">
              <h3>{status}</h3>
              <span className="ventes-badge">2</span>
            </div>
            {[1, 2].map((_, j) => (
              <div key={j} className="ventes-card">
                <p className="ventes-client">Fromagerie Martin</p>
                <h3>200 000 Ar</h3>
                <p>Contact : M.Martin</p>
                <p>Gouda affiné : 50 kg</p>
                <p>📅 2025-06-05</p>
                <span className="ventes-priority">Haute</span>
              </div>
            ))}
            <button className="ventes-add-btn">+ Ajouter</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Ventes;
