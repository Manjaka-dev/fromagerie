package itu.fromagerie.fromagerie.interceptor;

import itu.fromagerie.fromagerie.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private AuthService authService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Vérifier si l'endpoint nécessite une authentification
        String requestURI = request.getRequestURI();
        
        // Endpoints publics qui ne nécessitent pas d'authentification
        if (requestURI.startsWith("/api/auth/") || 
            requestURI.equals("/api/auth/login") ||
            requestURI.equals("/api/auth/register")) {
            return true;
        }
        
        // Vérifier le token d'authentification
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token d'authentification manquant\"}");
            return false;
        }
        
        String token = authHeader.substring(7);
        
        if (!authService.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token d'authentification invalide\"}");
            return false;
        }
        
        // Vérifier les rôles si nécessaire
        if (requestURI.startsWith("/api/admin/")) {
            if (!authService.hasRole(token, "ADMIN")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"error\": \"Accès refusé - Rôle ADMIN requis\"}");
                return false;
            }
        }
        
        if (requestURI.startsWith("/api/manager/")) {
            if (!authService.hasRole(token, "MANAGER") && !authService.hasRole(token, "ADMIN")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"error\": \"Accès refusé - Rôle MANAGER ou ADMIN requis\"}");
                return false;
            }
        }
        
        return true;
    }
} 