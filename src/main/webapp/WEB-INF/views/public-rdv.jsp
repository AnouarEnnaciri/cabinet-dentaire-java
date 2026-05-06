<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Prendre rendez-vous - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <h1><i class="fas fa-calendar-plus"></i> Demande de rendez-vous</h1>

    <% if (request.getAttribute("success") != null) { %>
        <div class="success-message">
            <i class="fas fa-check-circle"></i> <%= request.getAttribute("success") %>
        </div>
    <% } %>

    <form method="post" action="public-rdv">
        <div class="form-group">
            <label>Nom complet :</label>
            <input type="text" name="nom" required>
        </div>

        <div class="form-group">
            <label>Téléphone :</label>
            <input type="tel" name="telephone" required>
        </div>

        <div class="form-group">
            <label>Date souhaitée :</label>
            <input type="date" name="date" required>
        </div>

        <div class="form-group">
            <label>Motif :</label>
            <textarea name="motif" rows="3" placeholder="Décrivez brièvement votre besoin..."></textarea>
        </div>

        <button type="submit" class="btn-add">Envoyer la demande</button>
        <a href="/" class="btn-cancel">Retour à l'accueil</a>
    </form>
</div>
</body>
</html>