// src/components/SidebarMenu.jsx
import React from 'react';
import SidebarMenuItem from './SidebarMenuItem';
import menuItems from './menuItems';
import './SidebarMenuItems.css';

const SidebarMenu = () => {
  return (
    <div className="sidebar-nav">
      <h2 style={{ color: '#2c3e50', marginBottom: '40px', fontWeight: 'bold' }}>CheeseFlow</h2>
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