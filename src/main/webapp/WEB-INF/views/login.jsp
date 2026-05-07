<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body class="login-body">

<div class="login-container">
    <i class="fas fa-tooth login-icon"></i>
    <h1>Cabinet Dentaire</h1>
    <p class="subtitle">Espace personnel sécurisé</p>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error-message">
            <i class="fas fa-exclamation-triangle"></i> <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form method="post" action="login">
        <div class="form-group">
            <input type="text" name="login" placeholder="Identifiant" required/>
        </div>
        <div class="form-group">
            <input type="password" name="password" placeholder="Mot de passe" required/>
        </div>
        <button type="submit" class="btn-add">
            <i class="fas fa-arrow-right-to-bracket"></i> Se connecter
        </button>
    </form>

    <div class="login-footer">
        <p><a href="${pageContext.request.contextPath}/">← Retour à l'accueil</a></p>
    </div>
</div>

</body>
</html>