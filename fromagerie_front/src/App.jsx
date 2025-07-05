import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Login } from "./pages/auth/login/Login";
import { Inscription } from "./pages/auth/Inscription/Inscription";
import { TableauDeBord } from "./pages/tableauDeBord/Home";
import { CommandesPage } from "./pages/commande/Commande"; 
import Promotions from "./pages/Promotions";
import Livraison from "./pages/Livraisons";
import Production from "./pages/Production";
import Comptabilite from "./pages/Comptabilite"; // Import the Comptabilite component
import StockProduitsFinis from "./pages/stock/StockProduitFini";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/stock" element={<StockProduitsFinis />}></Route>
        <Route path="/inscription" element={<Inscription />} />
        <Route path="/TableauDeBord" element={<TableauDeBord />} />
        <Route path="/commandes" element={<CommandesPage />} />     
        <Route path="/promotion" element={<Promotions />} />     
        <Route path="/livraisons" element={<Livraison />} />     
        <Route path="/production" element={<Production />} />     
        <Route path="/comptabilite" element={<Comptabilite />} /> {/* Add the route */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;