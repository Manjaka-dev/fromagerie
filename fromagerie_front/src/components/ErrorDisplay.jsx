import React from 'react';
import { AlertTriangle, RefreshCw } from 'lucide-react';

const ErrorDisplay = ({ 
  error, 
  onRetry, 
  title = "Une erreur s'est produite",
  description = "Impossible de charger les données. Vérifiez votre connexion et réessayez."
}) => {
  return (
    <div className="error-display">
      <div className="error-content">
        <AlertTriangle size={48} className="error-icon" />
        <h3 className="error-title">{title}</h3>
        <p className="error-description">{description}</p>
        {error && (
          <p className="error-details">{error}</p>
        )}
        {onRetry && (
          <button 
            onClick={onRetry}
            className="retry-button"
          >
            <RefreshCw size={16} />
            Réessayer
          </button>
        )}
      </div>
      
      <style jsx>{`
        .error-display {
          display: flex;
          justify-content: center;
          align-items: center;
          min-height: 400px;
          padding: 2rem;
          text-align: center;
        }
        
        .error-content {
          max-width: 400px;
        }
        
        .error-icon {
          color: #ef4444;
          margin-bottom: 1rem;
        }
        
        .error-title {
          font-size: 1.5rem;
          font-weight: 600;
          color: #374151;
          margin-bottom: 0.5rem;
        }
        
        .error-description {
          color: #6b7280;
          margin-bottom: 1rem;
          line-height: 1.5;
        }
        
        .error-details {
          background: #fee2e2;
          color: #dc2626;
          padding: 0.75rem;
          border-radius: 0.5rem;
          font-size: 0.875rem;
          margin-bottom: 1rem;
          word-break: break-word;
        }
        
        .retry-button {
          display: inline-flex;
          align-items: center;
          gap: 0.5rem;
          background: #3b82f6;
          color: white;
          border: none;
          padding: 0.75rem 1.5rem;
          border-radius: 0.5rem;
          font-weight: 500;
          cursor: pointer;
          transition: background-color 0.2s;
        }
        
        .retry-button:hover {
          background: #2563eb;
        }
      `}</style>
    </div>
  );
};

export default ErrorDisplay;
