<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
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

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Demandes de rendez-vous - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<div class="container">
    <div class="top-bar">
        <a href="dashboard" class="back-link"><i class="fas fa-arrow-left"></i> Retour tableau de bord</a>
        <a href="logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-clock"></i> Demandes de rendez-vous en attente</h1>

    <%
        List<RendezVous> pendingRdvs = (List<RendezVous>) request.getAttribute("pendingRdvs");
        if (pendingRdvs != null && !pendingRdvs.isEmpty()) {
    %>
    <table class="medical-history-table">
        <thead>
            <tr>
                <th>Date demande</th>
                <th>Patient</th>
                <th>Téléphone</th>
                <th>Date souhaitée</th>
                <th>Motif</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%
                for (RendezVous rdv : pendingRdvs) {
                    String dateDemandeFormatted = "";
                    String dateSouhaiteeFormatted = "";

                    if (rdv.getDateDemande() != null) {
                        dateDemandeFormatted = rdv.getDateDemande().format(dateTimeFormatter);
                    }
                    if (rdv.getDate() != null) {
                        dateSouhaiteeFormatted = rdv.getDate().format(dateFormatter);
                    }
            %>
            <tr>
                <td><%= dateDemandeFormatted %></td>
                <td><%= rdv.getPatientNom() %></td>
                <td><%= rdv.getPatientTelephone() %></td>
                <td><%= dateSouhaiteeFormatted %></td>
                <td><%= rdv.getMotif() != null ? rdv.getMotif() : "-" %></td>
                <td class="pending-actions">
                    <a href="confirm-rdv?id=<%= rdv.getId() %>" class="btn-accept">Accepter</a>
                    <form method="post" action="pending-rdv" style="display:inline;">
                        <input type="hidden" name="id" value="<%= rdv.getId() %>">
                        <input type="hidden" name="action" value="refuser">
                        <button type="submit" class="btn-refuse">Refuser</button>
                    </form>
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
    <%
        } else {
    %>
    <p>Aucune demande en attente.</p>
    <%
        }
    %>
</div>
</body>
</html>