<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
    String role = (String) session.getAttribute("role");
    if (!"medecin".equals(role) && !"admin".equals(role)) {
        response.sendRedirect("dashboard");
        return;
    }

    List<String> consultationsLabels = (List<String>) request.getAttribute("consultationsLabels");
    List<Integer> consultationsValues = (List<Integer>) request.getAttribute("consultationsValues");
    List<String> revenueLabels = (List<String>) request.getAttribute("revenueLabels");
    List<Double> revenueValues = (List<Double>) request.getAttribute("revenueValues");
    List<Map<String, Object>> topActes = (List<Map<String, Object>>) request.getAttribute("topActes");

    StringBuilder consLabels = new StringBuilder();
    StringBuilder consVals = new StringBuilder();
    StringBuilder revLabels = new StringBuilder();
    StringBuilder revVals = new StringBuilder();

    for (int i = 0; i < consultationsLabels.size(); i++) {
        if (i > 0) { consLabels.append(","); consVals.append(","); }
        consLabels.append("'").append(consultationsLabels.get(i)).append("'");
        consVals.append(consultationsValues.get(i));
    }
    for (int i = 0; i < revenueLabels.size(); i++) {
        if (i > 0) { revLabels.append(","); revVals.append(","); }
        revLabels.append("'").append(revenueLabels.get(i)).append("'");
        revVals.append(revenueValues.get(i));
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Mes Statistiques - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/statistiques.css">
</head>
<body>
<div class="container">

    <div class="top-bar">
        <a href="dashboard" class="back-link"><i class="fas fa-arrow-left"></i> Retour tableau de bord</a>
        <a href="logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-chart-line"></i> Mes Statistiques</h1>
    <p class="subtitle">Dr. <%= request.getAttribute("medecinNom") %></p>

    <!-- Stats cards -->
    <div class="stats-grid">
        <div class="stats-card">
            <i class="fas fa-stethoscope"></i>
            <h3><%= request.getAttribute("totalConsultations") %></h3>
            <p>Mes consultations</p>
        </div>
        <div class="stats-card">
            <i class="fas fa-users"></i>
            <h3><%= request.getAttribute("totalPatients") %></h3>
            <p>Mes patients</p>
        </div>
        <div class="stats-card">
            <i class="fas fa-coins"></i>
            <h3><%= String.format("%.0f", request.getAttribute("totalRevenue")) %> DH</h3>
            <p>Mon chiffre d'affaires</p>
        </div>
    </div>

    <!-- Charts -->
    <div class="charts-grid">
        <div class="chart-box">
            <h3>Consultations par mois</h3>
            <canvas id="consultationsChart"></canvas>
        </div>
        <div class="chart-box">
            <h3>Revenus par mois</h3>
            <canvas id="revenueChart"></canvas>
        </div>
    </div>

    <!-- Top actes -->
    <div class="chart-box">
        <h3>Mes actes les plus pratiqués</h3>
        <% if (topActes != null && !topActes.isEmpty()) { %>
        <table class="top-acts-table">
            <thead>
                <tr><th>Acte</th><th>Nombre</th></tr>
            </thead>
            <tbody>
                <% for (Map<String, Object> acte : topActes) { %>
                <tr>
                    <td><%= acte.get("libelle") %></td>
                    <td><%= acte.get("count") %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else { %>
        <p>Aucun acte enregistré pour le moment.</p>
        <% } %>
    </div>

</div>

<!-- Pass data to JS -->
<div id="chart-data"
     data-cons-labels="<%= consLabels %>"
     data-cons-values="<%= consVals %>"
     data-revenue-labels="<%= revLabels %>"
     data-revenue-values="<%= revVals %>"
     class="hidden-template">
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="${pageContext.request.contextPath}/js/statistiques-medecin.js"></script>
</body>
</html>