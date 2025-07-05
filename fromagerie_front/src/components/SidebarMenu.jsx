// src/components/SidebarMenu.jsx
import React from 'react';
import SidebarMenuItem from './SidebarMenuItem'; // Ajoutez cette ligne
import menuItems from './menuItems';
import './SidebarMenuItems.css'; // Assurez-vous d'importer le fichier CSS pour le style

const SidebarMenu = () => {
  return (
    <div className="sidebar-nav">
      {menuItems.map((item) => (
        <SidebarMenuItem 
          key={item.name}
          name={item.name}
          Icon={item.icon}
          path={item.path}
        />
      ))}
    </div>
  );
};

export default SidebarMenu;