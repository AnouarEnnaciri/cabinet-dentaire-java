<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion - Cabinet Dentaire</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-container">
    <h1>Cabinet Dentaire</h1>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <form method="post" action="login">
        <input type="text" name="login" placeholder="Nom d'utilisateur" required/>
        <input type="password" name="password" placeholder="Mot de passe" required/>
        <button type="submit" class="btn-add">Se connecter</button>
    </form>
</div>
</body>
</html>