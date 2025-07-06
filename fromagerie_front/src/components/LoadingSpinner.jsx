import React from 'react';
import { Loader } from 'lucide-react';

const LoadingSpinner = ({ 
  message = "Chargement...",
  size = "medium",
  fullScreen = false 
}) => {
  const sizeClasses = {
    small: "w-4 h-4",
    medium: "w-8 h-8", 
    large: "w-12 h-12"
  };

  const containerClass = fullScreen 
    ? "loading-fullscreen" 
    : "loading-container";

  return (
    <div className={containerClass}>
      <div className="loading-content">
        <Loader 
          className={`loading-spinner ${sizeClasses[size]}`}
        />
        {message && (
          <p className="loading-message">{message}</p>
        )}
      </div>
      
      <style jsx>{`
        .loading-container {
          display: flex;
          justify-content: center;
          align-items: center;
          padding: 2rem;
          min-height: 200px;
        }
        
        .loading-fullscreen {
          display: flex;
          justify-content: center;
          align-items: center;
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(255, 255, 255, 0.8);
          z-index: 9999;
        }
        
        .loading-content {
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 1rem;
        }
        
        .loading-spinner {
          color: #3b82f6;
          animation: spin 1s linear infinite;
        }
        
        .loading-message {
          color: #6b7280;
          font-weight: 500;
          text-align: center;
        }
        
        @keyframes spin {
          from {
            transform: rotate(0deg);
          }
          to {
            transform: rotate(360deg);
          }
        }
      `}</style>
    </div>
  );
};

export default LoadingSpinner;
