import { FaChartBar, FaBoxes, FaCalculator, FaShoppingCart, FaTruck, FaCogs, FaIndustry } from 'react-icons/fa';

const menuItems = [
  { name: 'Tableau de Bord', icon: FaChartBar, active: true, path: '/' },
  { name: 'Stock', icon: FaBoxes, path: '/stock' },
  { name: 'Comptabilité', icon: FaCalculator, path: '/comptabilite' },
  { name: 'Ventes', icon: FaShoppingCart, path: '/Vente' },
  { name: 'Livraisons', icon: FaTruck, path: '/livraisons' },
  { name: 'Administration', icon: FaCogs, path: '/administration' },
  { name: 'Production', icon: FaIndustry, path: '/production' }
];

export default menuItems;
