<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cabinet.model.Utilisateur" %>
<%@ page import="com.cabinet.model.RendezVous" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
    String role = (String) session.getAttribute("role");
    if (!"admin".equals(role) && !"secretaire".equals(role)) {
        response.sendRedirect("dashboard");
        return;
    }

    RendezVous rdv = (RendezVous) request.getAttribute("rdv");
    List<Utilisateur> medecins = (List<Utilisateur>) request.getAttribute("medecins");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmer le rendez-vous - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="top-bar">
        <a href="pending-rdv" class="back-link"><i class="fas fa-arrow-left"></i> Retour aux demandes</a>
        <a href="logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-calendar-check"></i> Confirmer le rendez-vous</h1>

    <div class="patient-info">
        <h3>Demande de : <%= rdv.getPatientNom() %></h3>
        <p><strong>Téléphone :</strong> <%= rdv.getPatientTelephone() %></p>
        <p><strong>Date souhaitée :</strong> <%= rdv.getDate() %></p>
        <p><strong>Motif :</strong> <%= rdv.getMotif() != null ? rdv.getMotif() : "Non précisé" %></p>
    </div>

    <form method="post" action="confirm-rdv">
        <input type="hidden" name="rdvId" value="<%= rdv.getId() %>">

        <div class="form-group">
            <label>Médecin :</label>
            <select name="medecinId" required>
                <option value="">-- Sélectionner un médecin --</option>
                <%
                    for (Utilisateur m : medecins) {
                %>
                <option value="<%= m.getId() %>"><%= m.getNom() %></option>
                <%
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label>Date :</label>
            <input type="date" name="date" value="<%= rdv.getDate() %>" required>
        </div>

        <div class="form-group">
            <label>Heure :</label>
            <input type="time" name="heure" required>
        </div>

        <button type="submit" class="btn-add">Confirmer le rendez-vous</button>
        <a href="pending-rdv" class="btn-cancel">Annuler</a>
    </form>
</div>
</body>
</html>