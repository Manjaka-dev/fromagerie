
// src/components/Sidebar.jsx
import { NavLink } from 'react-router-dom';
import { FaBoxes, FaChartBar, FaCashRegister, FaShoppingCart, FaTruck, FaTools, FaIndustry } from 'react-icons/fa';

const Sidebar = () => {
  const navItemStyle = {
    display: 'flex',
    alignItems: 'center',
    padding: '12px 20px',
    textDecoration: 'none',
    color: '#333',
    fontWeight: 500,
    borderRadius: '8px',
    marginBottom: '10px'
  };

  const activeStyle = {
    backgroundColor: '#eef3ff',
    color: '#0066ff'
  };

  return (
    <div style={{
      width: '240px',
      backgroundColor: '#ffffff',
      borderRight: '1px solid #e0e0e0',
      padding: '30px 20px',
      display: 'flex',
      flexDirection: 'column'
    }}>
      <h2 style={{ color: '#2c3e50', marginBottom: '40px', fontWeight: 'bold' }}>CheeseFlow</h2>
      <NavLink to="/stock" style={({ isActive }) => ({ ...navItemStyle, ...(isActive ? activeStyle : {}) })}>
        <FaBoxes style={{ marginRight: '10px' }} /> Stock
      </NavLink>
      <NavLink to="/statistiques" style={({ isActive }) => ({ ...navItemStyle, ...(isActive ? activeStyle : {}) })}>
        <FaChartBar style={{ marginRight: '10px' }} /> Statistiques
      </NavLink>
      <NavLink to="#" style={({ isActive }) => ({ ...navItemStyle, ...(isActive ? activeStyle : {}) })}>
        <FaChartBar style={{ marginRight: '10px' }} /> Comptabilité
      </NavLink>
      <NavLink to="/promotions" style={({ isActive }) => ({ ...navItemStyle, ...(isActive ? activeStyle : {}) })}>
        <FaCashRegister style={{ marginRight: '10px' }} /> Ventes
      </NavLink>
      <NavLink to="/livraisons" style={({ isActive }) => ({ ...navItemStyle, ...(isActive ? activeStyle : {}) })}>
        <FaTruck style={{ marginRight: '10px' }} /> Livraisons
      </NavLink>
      <NavLink to="#" style={navItemStyle}>
        <FaTools style={{ marginRight: '10px' }} /> Administration
      </NavLink>
      <NavLink to="#" style={navItemStyle}>
        <FaIndustry style={{ marginRight: '10px' }} /> Production
      </NavLink>
    </div>
  );
};
export default Sidebar;