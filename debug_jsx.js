// Script temporaire pour vérifier et corriger l'erreur JSX
// Utilisez ce script pour localiser et corriger l'erreur dans Commande.jsx

const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'fromagerie_front', 'src', 'pages', 'commande', 'Commande.jsx');

// Lire le fichier
fs.readFile(filePath, 'utf8', (err, data) => {
  if (err) {
    console.error('Erreur lors de la lecture du fichier:', err);
    return;
  }
  
  // Diviser le contenu en lignes
  const lines = data.split('\n');
  
  // Vérifier la ligne problématique et quelques lignes autour
  console.log('Lignes autour de 984:');
  for (let i = 980; i < 990; i++) {
    console.log(`${i}: "${lines[i-1]}"`);
  }
  
  // Vérifier la structure HTML/JSX autour de la ligne problématique
  const problematicLine = lines[984-1];
  console.log('\nLigne problématique (984):', problematicLine);
  
  // Vérifier l'intégrité des balises HTML/JSX
  let stack = [];
  let inComment = false;
  
  // Traiter le fichier ligne par ligne
  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    
    // Compter les ouvertures et fermetures de div
    const openDivs = (line.match(/<div/g) || []).length;
    const closeDivs = (line.match(/<\/div>/g) || []).length;
    
    if (i >= 980-5 && i <= 990+5) {
      console.log(`Ligne ${i+1}: ouverture=${openDivs}, fermeture=${closeDivs}`);
    }
  }

  // Récupérer la partie problématique 
  const problematicSection = lines.slice(950, 985).join('\n');
  
  console.log('\nPartie problématique:');
  console.log(problematicSection);
  
  // Tenter de trouver les caractères invisibles ou non imprimables
  console.log('\nCaractères spéciaux dans la ligne 984:');
  for (let i = 0; i < problematicLine.length; i++) {
    const charCode = problematicLine.charCodeAt(i);
    console.log(`Position ${i}: '${problematicLine[i]}' (code: ${charCode})`);
  }

  // Suggérer une correction
  console.log('\nRésolution suggérée:');
  console.log('Remplacer la ligne par:                    </div>');
});
