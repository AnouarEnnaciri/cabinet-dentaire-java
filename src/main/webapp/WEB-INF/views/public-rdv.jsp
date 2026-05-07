<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Prendre rendez-vous - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/public-rdv.css">
</head>
<body class="public-rdv-body">

<div class="rdv-container">
    <!-- Top bar with back button -->
    <div class="rdv-top-bar">
        <a href="${pageContext.request.contextPath}/" class="back-home">
            <i class="fas fa-arrow-left"></i> Retour à l'accueil
        </a>
    </div>

    <h1><i class="fas fa-calendar-plus"></i> Demande de rendez-vous</h1>
    <p class="subtitle">Remplissez le formulaire pour une demande de consultation</p>

    <% if (request.getAttribute("success") != null) { %>
        <div class="success-message">
            <i class="fas fa-check-circle"></i> <%= request.getAttribute("success") %>
        </div>
    <% } %>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error-message">
            <i class="fas fa-exclamation-triangle"></i> <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form method="post" action="public-rdv">
        <div class="form-group">
            <label><i class="fas fa-user"></i> Nom complet</label>
            <input type="text" name="nom" placeholder="Votre nom" required>
        </div>

        <div class="form-group">
            <label><i class="fas fa-phone"></i> Téléphone</label>
            <input type="tel" name="telephone" placeholder="Votre numéro" required>
        </div>

        <div class="form-group">
            <label><i class="fas fa-calendar-day"></i> Date souhaitée</label>
            <input type="date" name="date" required>
        </div>

        <div class="form-group">
            <label><i class="fas fa-comment"></i> Motif</label>
            <textarea name="motif" rows="3" placeholder="Décrivez brièvement votre besoin..."></textarea>
        </div>

        <button type="submit" class="btn-submit">
            <i class="fas fa-paper-plane"></i> Envoyer la demande
        </button>
    </form>
</div>

</body>
</html>