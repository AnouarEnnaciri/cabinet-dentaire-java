<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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

    List<String> revenueLabels = (List<String>) request.getAttribute("revenueLabels");
    List<Double> revenueValues = (List<Double>) request.getAttribute("revenueValues");
    List<String> rdvLabels = (List<String>) request.getAttribute("rdvLabels");
    List<Integer> rdvValues = (List<Integer>) request.getAttribute("rdvValues");
    List<Map<String, Object>> topActs = (List<Map<String, Object>>) request.getAttribute("topActs");

    StringBuilder revLabels = new StringBuilder();
    StringBuilder revVals = new StringBuilder();
    StringBuilder rdvLbls = new StringBuilder();
    StringBuilder rdvVals = new StringBuilder();

    for (int i = 0; i < revenueLabels.size(); i++) {
        if (i > 0) { revLabels.append(","); revVals.append(","); }
        revLabels.append("'").append(revenueLabels.get(i)).append("'");
        revVals.append(revenueValues.get(i));
    }
    for (int i = 0; i < rdvLabels.size(); i++) {
        if (i > 0) { rdvLbls.append(","); rdvVals.append(","); }
        rdvLbls.append("'").append(rdvLabels.get(i)).append("'");
        rdvVals.append(rdvValues.get(i));
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Statistiques - Cabinet Dentaire</title>
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

    <h1><i class="fas fa-chart-line"></i> Statistiques</h1>

    <!-- Stats cards -->
    <div class="stats-grid">
        <div class="stats-card">
            <i class="fas fa-users"></i>
            <h3><%= request.getAttribute("totalPatients") %></h3>
            <p>Patients</p>
        </div>
        <div class="stats-card">
            <i class="fas fa-calendar-alt"></i>
            <h3><%= request.getAttribute("totalRdvs") %></h3>
            <p>Rendez-vous</p>
        </div>
        <div class="stats-card">
            <i class="fas fa-coins"></i>
            <h3><%= String.format("%.0f", request.getAttribute("totalRevenue")) %> DH</h3>
            <p>Chiffre d'affaires</p>
        </div>
        <div class="stats-card">
            <i class="fas fa-clock"></i>
            <h3><%= request.getAttribute("pendingRdvs") %></h3>
            <p>RDV en attente</p>
        </div>
    </div>

    <!-- Charts -->
    <div class="charts-grid">
        <div class="chart-box">
            <h3>Revenus mensuels</h3>
            <canvas id="revenueChart"></canvas>
        </div>
        <div class="chart-box">
            <h3>Rendez-vous par mois</h3>
            <canvas id="rdvChart"></canvas>
        </div>
    </div>

    <!-- Top actes table -->
    <div class="chart-box">
        <h3>Actes les plus pratiqués</h3>
        <table class="top-acts-table">
            <thead>
                <tr><th>Acte</th><th>Nombre</th></tr>
            </thead>
            <tbody>
                <% for (Map<String, Object> act : topActs) { %>
                <tr>
                    <td><%= act.get("libelle") %></td>
                    <td><%= act.get("count") %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

</div>

<!-- Pass data to JS via hidden elements -->
<div id="chart-data"
     data-revenue-labels="<%= revLabels %>"
     data-revenue-values="<%= revVals %>"
     data-rdv-labels="<%= rdvLbls %>"
     data-rdv-values="<%= rdvVals %>"
     class="hidden-template">
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="${pageContext.request.contextPath}/js/statistiques.js"></script>
</body>
</html>