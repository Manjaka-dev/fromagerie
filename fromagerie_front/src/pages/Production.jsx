import React, { useState } from 'react';
import '../assets/css/production.css';

const Production = () => {
  const [product, setProduct] = useState({
    category: '',
    name: '',
    weight: '',
    sellingPrice: '',
    costPrice: '',
    ingredients: '',
    allergens: '',
    expirationDate: ''
  });

  const recentProductions = [
    {
      category: 'Nom Catégorie',
      name: 'Nom du produit',
      weight: 'Poids',
      costPrice: '5 000 A+',
      ingredients: 'sal, Yocurt, ...',
      sellingPrice: '28 000 A+'
    },
    {
      category: 'Nom Catégorie',
      name: 'Nom du produit',
      weight: 'Poids',
      costPrice: '5 000 A+',
      ingredients: 'sal, Yocurt, ...',
      sellingPrice: '28 000 A+'
    },
    {
      category: 'Nom Catégorie',
      name: 'Nom du produit',
      weight: 'Poids',
      costPrice: '5 000 A+',
      ingredients: 'sal, Yocurt, ...',
      sellingPrice: '28 000 A+'
    }
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProduct(prev => ({
      ...prev,
      [name]: value
    }));
  };


  const handleSave = () => {
    // Logique pour sauvegarder le produit
    console.log('Produit sauvegardé:', product);
    alert('Produit sauvegardé avec succès!');
  };

  const handleCancel = () => {
    // Réinitialiser le formulaire
    setProduct({
      category: '',
      name: '',
      weight: '',
      sellingPrice: '',
      costPrice: '',
      ingredients: '',
      allergens: '',
      expirationDate: ''
    });
  };
  return (
    <div className="production-page">
      <h1>Informations du produit</h1>
      
      <div className="product-form">
        <div className="form-section">
          <h2>Catégorie</h2>
          <select 
            name="category" 
            value={product.category}
            onChange={handleChange}
            className="form-input"
          >
            <option value="">Sélectionner une catégorie --</option>
            {/* Add actual category options here */}
          </select>
          
          <input
            type="text"
            name="name"
            value={product.name}
            onChange={handleChange}
            placeholder="Nom du produit"
            className="form-input"
          />
          <span className="example">Ex : Goudra anticanci primium</span>
        </div>
        
        <div className="form-section">
          <h2>Poids (g)</h2>
          <input
            type="text"
            name="weight"
            value={product.weight}
            onChange={handleChange}
            placeholder="B00"
            className="form-input"
          />
          
          <h2>Prix de vente (A4)</h2>
          <input
            type="text"
            name="sellingPrice"
            value={product.sellingPrice}
            onChange={handleChange}
            placeholder="26 000"
            className="form-input"
          />
        </div>
        
        <div className="form-section">
          <h2>Prix de revient (A4)</h2>
          <input
            type="text"
            name="costPrice"
            value={product.costPrice}
            onChange={handleChange}
            placeholder="8 000"
            className="form-input"
          />
        </div>
        
        <div className="form-section">
          <h2>Ingredients</h2>
          <textarea
            name="ingredients"
            value={product.ingredients}
            onChange={handleChange}
            placeholder="Séi, prélaises, fermant yacuit ..."
            className="form-input"
          />
        </div>
        
        <div className="form-section">
          <h2>Allergènes</h2>
          <select 
            name="allergens" 
            value={product.allergens}
            onChange={handleChange}
            className="form-input"
          >
            <option value="">Sélectionner les allergènes --</option>
            {/* Add actual allergen options here */}
          </select>
        </div>
        
        <div className="form-section">
          <h2>Date de préemption</h2>
          <input
            type="date"
            name="expirationDate"
            value={product.expirationDate}
            onChange={handleChange}
            className="form-input"
          />
        </div>
      </div>
      <div>
      <div className="form-actions">
          <button className="cancel-btn" onClick={handleCancel}>
            Annuler
          </button>
          <button className="save-btn" onClick={handleSave}>
            Sauvegarder
          </button>
        </div>
      </div>
      <hr />
      
      <div className="recent-productions">
        <h3>Productions Récentes</h3>
        <table>
          <tbody>
            {recentProductions.map((prod, index) => (
              <React.Fragment key={index}>
                <tr>
                  <td><strong>{prod.category}</strong></td>
                  <td>{prod.name}</td>
                </tr>
                <tr>
                  <td>{prod.weight}</td>
                  <td>Prix de revient : {prod.costPrice}</td>
                </tr>
                <tr>
                  <td>Ingredients : {prod.ingredients}</td>
                  <td>Prix de vente : {prod.sellingPrice}</td>
                </tr>
              </React.Fragment>
            ))}
          </tbody>
        </table>
      </div>
      
      <hr />
      
      <div className="production-tips">
        <h3>Conseils Production</h3>
        <ul>
          <li>Respectez les temps d'offrinage pour chaque catégorie</li>
          <li>Veillre la température de stockage</li>
          <li>Documents chaque étage de production</li>
          <li>Contrôlez régulièrement la qualité</li>
        </ul>
      </div>
    </div>
  );
};

export default Production;