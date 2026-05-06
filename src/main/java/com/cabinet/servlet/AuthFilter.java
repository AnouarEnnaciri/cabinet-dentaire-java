package com.cabinet.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre (rien à faire ici pour l'instant)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();

        // Pages publiques (tout le monde peut accéder)
        if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg")) {
            chain.doFilter(request, response);
            return;
        }

        if (uri.contains("/login") || uri.contains("/index") || uri.equals("/") || uri.contains("/homepage") || uri.contains("/public-rdv")) {
            chain.doFilter(request, response);
            return;
        }

        // Vérifier si l'utilisateur est connecté
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        // Règles d'accès par rôle
        if ("secretaire".equals(role)) {
            if (uri.contains("/medical-records") && !uri.contains("?readonly")) {
                System.out.println("Secrétaire accède aux dossiers médicaux (lecture seule)");
            }
            if (uri.contains("/prescriptions")) {
                res.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }
        }

        if ("medecin".equals(role) && uri.contains("/billing")) {
            res.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage du filtre (rien à faire ici pour l'instant)
    }
}