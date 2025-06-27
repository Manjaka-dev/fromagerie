import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Login } from "./components/auth/login/Login";
import { TableauDeBord } from "./components/tableauDeBord/Home"; 

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/TableauDeBord" element={<TableauDeBord />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
