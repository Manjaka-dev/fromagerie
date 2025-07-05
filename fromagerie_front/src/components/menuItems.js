import { BarChart3, Package, Calculator, ShoppingCart, Truck, Settings, Factory } from 'lucide-react';

const menuItems = [
  { name: 'Tableau de Bord', icon: BarChart3, active: true, path: '/TableauDeBord' },
  { name: 'Stock', icon: Package, path: '/stock' },
  { name: 'Comptabilité', icon: Calculator, path: '/comptabilite' },
  { name: 'Commandes', icon: ShoppingCart, path: '/commandes' },
  { name: 'Livraisons', icon: Truck, path: '/livraisons' },
  { name: 'Administration', icon: Settings, path: '/administration' },
  { name: 'Production', icon: Factory, path: '/production' }
];

export default menuItems;
