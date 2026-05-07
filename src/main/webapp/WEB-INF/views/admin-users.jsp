<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cabinet.model.Utilisateur" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
    String role = (String) session.getAttribute("role");
    if (!"admin".equals(role)) {
        response.sendRedirect("dashboard");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Administration - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>

<%
    String msg = (String) session.getAttribute("msg");
    if (msg != null) {
%>
<div class="success-message">
    <i class="fas fa-check-circle"></i> <%= msg %>
</div>
<%
        session.removeAttribute("msg");
    }
%>

<div class="container">

    <div class="top-bar">
        <a href="${pageContext.request.contextPath}/dashboard" class="back-link">
            <i class="fas fa-arrow-left"></i> Retour tableau de bord
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-user-cog"></i> Administration</h1>
    <p>Gestion des médecins et secrétaires</p>

    <!-- Formulaire d'ajout -->
    <h2>Ajouter un utilisateur</h2>
    <form method="post" action="${pageContext.request.contextPath}/admin/users">
        <input type="hidden" name="action" value="add"/>

        <input type="text" name="login" placeholder="Nom d'utilisateur" required/>
        <input type="password" name="password" placeholder="Mot de passe" required/>
        <input type="text" name="nom" placeholder="Nom complet" required/>
        <input type="email" name="email" placeholder="Email" required/>

        <select name="userRole" required>
            <option value="medecin">Médecin</option>
            <option value="secretaire">Secrétaire</option>
        </select>

        <button type="submit" class="btn-add">
            <i class="fas fa-plus"></i> Ajouter
        </button>
    </form>

    <!-- Liste des médecins -->
    <h2>Médecins</h2>
    <table class="medical-history-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Login</th>
                <th>Nom</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <%
            List<Utilisateur> medecins = (List<Utilisateur>) request.getAttribute("medecins");
            if (medecins != null) {
                for (Utilisateur m : medecins) {
        %>
        <tr>
            <td><%= m.getId() %></td>
            <td><%= m.getLogin() %></td>
            <td><%= m.getNom() %></td>
            <td><%= m.getEmail() %></td>
            <td class="action-cell">
                <a href="${pageContext.request.contextPath}/admin/edit-user?id=<%= m.getId() %>" class="btn-edit">
                    Modifier
                </a>
                <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="id" value="<%= m.getId() %>"/>
                    <button type="submit" class="btn-delete" onclick="return confirm('Supprimer <%= m.getNom() %> ?')">
                        Supprimer
                    </button>
                </form>
            </td
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>

    <!-- Liste des secrétaires -->
    <h2>Secrétaires</h2>
    <table class="medical-history-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Login</th>
                <th>Nom</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <%
            List<Utilisateur> secretaires = (List<Utilisateur>) request.getAttribute("secretaires");
            if (secretaires != null) {
                for (Utilisateur s : secretaires) {
        %>
        <tr>
            <td><%= s.getId() %></td>
            <td><%= s.getLogin() %></td>
            <td><%= s.getNom() %></td>
            <td><%= s.getEmail() %></td>
            <td class="action-cell">
                <a href="${pageContext.request.contextPath}/admin/edit-user?id=<%= s.getId() %>" class="btn-edit">
                    Modifier
                </a>
                <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="id" value="<%= s.getId() %>"/>
                    <button type="submit" class="btn-delete" onclick="return confirm('Supprimer <%= s.getNom() %> ?')">
                        Supprimer
                    </button>
                </form>
            </td
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>

</div>

</body>
</html>