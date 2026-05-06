<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    Utilisateur user = (Utilisateur) request.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Modifier utilisateur - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="top-bar">
        <a href="${pageContext.request.contextPath}/admin/users" class="back-link"><i class="fas fa-arrow-left"></i> Retour</a>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-user-edit"></i> Modifier utilisateur</h1>

    <form method="post" action="${pageContext.request.contextPath}/admin/edit-user">
        <input type="hidden" name="id" value="<%= user.getId() %>">

        <div class="form-group">
            <label>Nom d'utilisateur :</label>
            <input type="text" name="login" value="<%= user.getLogin() %>" required>
        </div>

        <div class="form-group">
            <label>Nom complet :</label>
            <input type="text" name="nom" value="<%= user.getNom() != null ? user.getNom() : "" %>" required>
        </div>

        <div class="form-group">
            <label>Email :</label>
            <input type="email" name="email" value="<%= user.getEmail() != null ? user.getEmail() : "" %>" required>
        </div>

        <div class="form-group">
            <label>Rôle :</label>
            <select name="role" required>
                <option value="medecin" <%= "medecin".equals(user.getRole()) ? "selected" : "" %>>Médecin</option>
                <option value="secretaire" <%= "secretaire".equals(user.getRole()) ? "selected" : "" %>>Secrétaire</option>
            </select>
        </div>

        <div class="form-group">
            <label>Nouveau mot de passe :</label>
            <input type="password" name="newPassword" placeholder="Laisser vide pour ne pas changer">
            <small class="info-message">Optionnel</small>
        </div>

        <button type="submit" class="btn-add">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/admin/users" class="btn-cancel">Annuler</a>
    </form>
</div>
</body>
</html>