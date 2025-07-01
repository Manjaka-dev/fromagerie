import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Login } from "./pages/auth/login/Login";
import { Inscription } from "./pages/auth/Inscription/Inscription";
import { TableauDeBord } from "./pages/tableauDeBord/Home";
import {CommandesPage} from "./pages/commande/Commande"; 
import Promotions from "./pages/Promotions";
import Livraison from "./pages/Livraisons";
import Production from "./pages/Production";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/inscription" element={<Inscription />} />
        <Route path="/TableauDeBord" element={<TableauDeBord />} />
        <Route path="/commandes" element={<CommandesPage />} />     
        <Route path="/promotion" element={<Promotions />} />     
        <Route path="/livraisons" element={<Livraison />} />     
        <Route path="/production" element={<Production />} />     
      </Routes>
    </BrowserRouter>
  );
}

export default App;
