import React, { useState, useEffect } from 'react';
import { CheckCircle, AlertCircle, Info, X } from 'lucide-react';

const NotificationManager = () => {
  const [notifications, setNotifications] = useState([]);

  // Fonction pour ajouter une notification
  const addNotification = (type, message, duration = 5000) => {
    const id = Date.now();
    const notification = { id, type, message, duration };
    
    setNotifications(prev => [...prev, notification]);
    
    // Auto-remove après la durée spécifiée
    setTimeout(() => {
      removeNotification(id);
    }, duration);
    
    return id;
  };

  // Fonction pour supprimer une notification
  const removeNotification = (id) => {
    setNotifications(prev => prev.filter(notif => notif.id !== id));
  };

  // Exporter les fonctions globalement
  useEffect(() => {
    window.showNotification = addNotification;
  }, []);

  const getIcon = (type) => {
    switch (type) {
      case 'success':
        return <CheckCircle size={20} />;
      case 'error':
        return <AlertCircle size={20} />;
      case 'info':
        return <Info size={20} />;
      default:
        return <Info size={20} />;
    }
  };

  const getTypeClass = (type) => {
    switch (type) {
      case 'success':
        return 'notification-success';
      case 'error':
        return 'notification-error';
      case 'info':
        return 'notification-info';
      default:
        return 'notification-info';
    }
  };

  return (
    <div className="notification-container">
      {notifications.map(notification => (
        <div
          key={notification.id}
          className={`notification ${getTypeClass(notification.type)}`}
        >
          <div className="notification-content">
            <div className="notification-icon">
              {getIcon(notification.type)}
            </div>
            <p className="notification-message">
              {notification.message}
            </p>
          </div>
          <button
            onClick={() => removeNotification(notification.id)}
            className="notification-close"
          >
            <X size={16} />
          </button>
        </div>
      ))}
      
      <style>{`
        .notification-container {
          position: fixed;
          top: 1rem;
          right: 1rem;
          z-index: 10000;
          max-width: 400px;
        }
        
        .notification {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding: 1rem;
          margin-bottom: 0.5rem;
          border-radius: 0.5rem;
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          animation: slideIn 0.3s ease-out;
        }
        
        .notification-success {
          background: #d1fae5;
          color: #065f46;
          border-left: 4px solid #10b981;
        }
        
        .notification-error {
          background: #fee2e2;
          color: #991b1b;
          border-left: 4px solid #ef4444;
        }
        
        .notification-info {
          background: #dbeafe;
          color: #1e40af;
          border-left: 4px solid #3b82f6;
        }
        
        .notification-content {
          display: flex;
          align-items: center;
          gap: 0.75rem;
          flex: 1;
        }
        
        .notification-icon {
          flex-shrink: 0;
        }
        
        .notification-message {
          margin: 0;
          font-weight: 500;
          line-height: 1.4;
        }
        
        .notification-close {
          background: none;
          border: none;
          color: currentColor;
          cursor: pointer;
          padding: 0.25rem;
          border-radius: 0.25rem;
          opacity: 0.7;
          transition: opacity 0.2s;
        }
        
        .notification-close:hover {
          opacity: 1;
        }
        
        @keyframes slideIn {
          from {
            transform: translateX(100%);
            opacity: 0;
          }
          to {
            transform: translateX(0);
            opacity: 1;
          }
        }
      `}</style>
    </div>
  );
};

// Fonctions utilitaires pour afficher des notifications
export const showSuccess = (message) => {
  if (window.showNotification) {
    window.showNotification('success', message);
  }
};

export const showError = (message) => {
  if (window.showNotification) {
    window.showNotification('error', message);
  }
};

export const showInfo = (message) => {
  if (window.showNotification) {
    window.showNotification('info', message);
  }
};

export default NotificationManager;
