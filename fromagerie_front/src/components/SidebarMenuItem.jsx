// src/components/SidebarMenuItem.jsx
import React from 'react';
import { NavLink } from 'react-router-dom';
import './SidebarMenuItems.css';
const SidebarMenuItem = ({ name, Icon, path }) => {
  return (
    <NavLink 
      to={path}
      className={({ isActive }) => 
        `sidebar-menu-item ${isActive ? 'active' : ''}`
      }
    >
      <Icon className="menu-icon" size={20} />
      <span className="menu-name">{name}</span>
    </NavLink>
  );
};

export default SidebarMenuItem;