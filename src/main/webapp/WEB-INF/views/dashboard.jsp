<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cabinet.model.Utilisateur" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
    Utilisateur user = (Utilisateur) session.getAttribute("user");
    String role = (String) session.getAttribute("role");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de bord - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<div class="dashboard-container">
    <div class="dashboard-header">
        <div class="header-left">
            <a href="${pageContext.request.contextPath}/" class="home-link"><i class="fas fa-home"></i> Accueil</a>
            <h1><i class="fas fa-tooth"></i> Cabinet Dentaire</h1>
            <p>Bienvenue, <strong><%= user.getNom() != null ? user.getNom() : user.getLogin() %></strong></p>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Déconnexion</a>
    </div>

    <div class="dashboard-cards">
        <!-- Patients -->
        <div class="dashboard-card">
            <i class="fas fa-users"></i>
            <h3>Patients</h3>
            <p>Gérer les dossiers patients</p>
            <a href="${pageContext.request.contextPath}/patients" class="card-btn">Accéder</a>
        </div>

        <!-- Rendez-vous (admin + secrétaire) -->
        <% if (!"medecin".equals(role)) { %>
        <div class="dashboard-card">
            <i class="fas fa-calendar-alt"></i>
            <h3>Rendez-vous</h3>
            <p>Planifier et consulter les RDV</p>
            <a href="${pageContext.request.contextPath}/rendezvous" class="card-btn">Accéder</a>
        </div>
        <% } %>

        <!-- Demandes de rendez-vous (admin + secrétaire) -->
        <% if ("admin".equals(role) || "secretaire".equals(role)) { %>
        <div class="dashboard-card">
            <i class="fas fa-clock"></i>
            <h3>Demandes RDV</h3>
            <p>Valider les demandes de rendez-vous</p>
            <a href="${pageContext.request.contextPath}/pending-rdv" class="card-btn">Accéder</a>
        </div>
        <% } %>

        <!-- Mon agenda (médecin uniquement) -->
        <% if ("medecin".equals(role)) { %>
        <div class="dashboard-card">
            <i class="fas fa-calendar-check"></i>
            <h3>Mon agenda</h3>
            <p>Voir mes rendez-vous</p>
            <a href="${pageContext.request.contextPath}/rendezvous" class="card-btn">Accéder</a>
        </div>
        <% } %>

        <!-- Dossiers médicaux -->
        <div class="dashboard-card">
            <i class="fas fa-file-alt"></i>
            <h3>Dossiers médicaux</h3>
            <p>Consulter l'historique des patients</p>
            <a href="${pageContext.request.contextPath}/medical-records" class="card-btn">Accéder</a>
        </div>

        <!-- Facturation (admin + secrétaire) -->
        <% if (!"medecin".equals(role)) { %>
        <div class="dashboard-card">
            <i class="fas fa-file-invoice-dollar"></i>
            <h3>Facturation</h3>
            <p>Factures et paiements</p>
            <a href="${pageContext.request.contextPath}/factures" class="card-btn">Accéder</a>
        </div>
        <% } %>

        <!-- Administration (admin uniquement) -->
        <% if ("admin".equals(role)) { %>
        <div class="dashboard-card">
            <i class="fas fa-user-cog"></i>
            <h3>Administration</h3>
            <p>Gérer les médecins et secrétaires</p>
            <a href="${pageContext.request.contextPath}/admin/users" class="card-btn">Accéder</a>
        </div>
        <% } %>

        <!-- Statistiques (admin uniquement) -->
        <% if ("admin".equals(role)) { %>
        <div class="dashboard-card">
            <i class="fas fa-chart-line"></i>
            <h3>Statistiques</h3>
            <p>Tableau de bord des performances</p>
            <a href="${pageContext.request.contextPath}/statistiques" class="card-btn">Accéder</a>
        </div>
        <% } %>

        <!-- Mes statistiques (médecin uniquement) -->
        <% if ("medecin".equals(role)) { %>
        <div class="dashboard-card">
            <i class="fas fa-chart-bar"></i>
            <h3>Mes statistiques</h3>
            <p>Mes consultations et revenus</p>
            <a href="${pageContext.request.contextPath}/statistiques-medecin" class="card-btn">Accéder</a>
        </div>
        <% } %>
    </div>
</div>
</body>
</html>