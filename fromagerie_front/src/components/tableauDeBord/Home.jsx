import React, { useState } from "react";
import "./style.css";

export const TableauDeBord = () => {
  const [activePage, setActivePage] = useState("dashboard");

  const renderContent = () => {
    switch (activePage) {
      case "dashboard":
        return <div className="content">Bienvenue sur le tableau de bord</div>;
      case "stock":
        return <div className="content">Gestion du stock</div>;
      case "ventes":
        return <div className="content">Page des ventes</div>;
      case "compta":
        return <div className="content">Comptabilité</div>;
      default:
        return <div className="content">Page non trouvée</div>;
    }
  };

  return (
    <div className="tableau-de-bord">
      <div className="div">
        <aside className="sidebar">
          <div className="list">
            <button className="menu-item">
              <div className="text-wrapper-3">Tableau de Bord</div>
            </button>

            <button className="menu-item-2">
              <div className="search" />
              <div className="text-wrapper-3">Stock</div>
            </button>

            <button className="menu-item-2">
              <div className="radio" />
              <div className="text-wrapper-3">Statistiques</div>
            </button>

            <button className="menu-item-2">
              <div className="search-2" />
              <div className="text-wrapper-3">Comptabilité</div>
            </button>

            <button
              className="menu-item-2"
              onClick={() => setActivePage("ventes")}
            >
              <div className="search-3" />
              <div className="text-wrapper-3">Ventes</div>
            </button>

            <button className="menu-item-2">
              <div className="search-4" />
              <div className="text-wrapper-3">Livraisons</div>
            </button>

            <button className="menu-item-2">
              <div className="search-5" />
              <div className="text-wrapper-3">Administration</div>
            </button>
          </div>

          <div className="title">Artisanal Cheese</div>
        </aside>

        <main className="frame">
          <div className="list-2">
            <button className="menu-item-2">
              <div className="text-wrapper-4">Tableau de Bord</div>
            </button>

            <button className="menu-item-2">
              <div className="search-6" />
              <div className="text-wrapper-4">Stock</div>
            </button>

            <button className="menu-item-2">
              <div className="radio-2" />
              <div className="text-wrapper-4">Statistiques</div>
            </button>

            <button className="menu-item-2">
              <div className="search-7" />
              <div className="text-wrapper-3">Comptabilité</div>
            </button>

            <button
              className="menu-item-2"
              onClick={() => setActivePage("ventes")}
            >
              <div className="search-3" />
              <div className="text-wrapper-3">Ventes</div>
            </button>

            <button className="menu-item-2">
              <div className="search-9" />
              <div className="text-wrapper-3">Livraisons</div>
            </button>

            <button className="menu-item-2">
              <div className="search-10" />
              <div className="text-wrapper-3">Administration</div>
            </button>

            <button className="menu-item-2">
              <div className="search-11" />
              <div className="text-wrapper-3">Production</div>
            </button>
          </div>

          <div className="title-2">CheeseFlow</div>
          
        </main>
      </div>
      <div>
          {renderContent()}
      </div>
    </div>
  );
};
