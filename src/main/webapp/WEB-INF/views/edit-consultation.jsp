<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cabinet.model.Consultation" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
    String role = (String) session.getAttribute("role");
    if (!"admin".equals(role) && !"medecin".equals(role)) {
        response.sendRedirect("dashboard");
        return;
    }
    Consultation consultation = (Consultation) request.getAttribute("consultation");
    Long patientId = (Long) request.getAttribute("patientId");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Modifier consultation - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="top-bar">
        <a href="/medical-records?patientId=<%= patientId %>" class="back-link"><i class="fas fa-arrow-left"></i> Retour au dossier</a>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-edit"></i> Modifier la consultation</h1>

    <form method="post" action="${pageContext.request.contextPath}/consultations/edit">
        <input type="hidden" name="id" value="<%= consultation.getId() %>"/>
        <input type="hidden" name="patientId" value="<%= patientId %>"/>

        <div class="form-group">
            <label>Date :</label>
            <input type="date" name="date" value="<%= consultation.getDate() %>" disabled readonly/>
            <small class="info-message">La date ne peut pas être modifiée</small>
        </div>

        <div class="form-group">
            <label>Diagnostic :</label>
            <textarea name="diagnostic" rows="3" class="full-width-text"><%= consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "" %></textarea>
        </div>

        <div class="form-group">
            <label>Traitement :</label>
            <textarea name="traitement" rows="3" class="full-width-text"><%= consultation.getTraitement() != null ? consultation.getTraitement() : "" %></textarea>
        </div>

        <div class="form-group">
            <label>Notes :</label>
            <textarea name="notes" rows="2" class="full-width-text"><%= consultation.getNotes() != null ? consultation.getNotes() : "" %></textarea>
        </div>

        <button type="submit" class="btn-add"><i class="fas fa-save"></i> Enregistrer</button>
        <a href="${pageContext.request.contextPath}/medical-records?patientId=<%= patientId %>" class="btn-cancel">Annuler</a>
    </form>
</div>
</body>
</html>