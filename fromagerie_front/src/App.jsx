// src/App.jsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Stock from './pages/Stock';
import Statistiques from './pages/Statistiques';
import Promotions from './pages/Promotions';
import Livraisons from './pages/Livraisons';

function App() {
  return (
    <Router>
      <div style={{ display: 'flex', height: '100vh', fontFamily: 'Segoe UI, sans-serif' }}>
        <Sidebar />
        <div style={{ flex: 1, padding: '30px', backgroundColor: '#f9f9fb' }}>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/stock" element={<Stock />} />
            <Route path="/statistiques" element={<Statistiques />} />
            <Route path="/promotions" element={<Promotions />} />
            <Route path="/livraisons" element={<Livraisons />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;