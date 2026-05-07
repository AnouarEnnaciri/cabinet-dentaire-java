
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
    String role = (String) session.getAttribute("role");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cabinet.model.Patient" %>
<!DOCTYPE html>
<html>
<head>
    <title>Patients - Cabinet Dentaire</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="container">
    <div class="top-bar">
        <a href="dashboard" class="back-link"><i class="fas fa-arrow-left"></i> Retour tableau de bord</a>
        <a href="logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1>Cabinet Dentaire</h1>
    <p class="subtitle">Gestion des patients</p>

    <% if ("admin".equals(role) || "secretaire".equals(role)) { %>
    <h2>Ajouter un patient</h2>
    <form method="post" action="patients">
        <input type="hidden" name="action" value="add"/>
        <input type="text" name="name" placeholder="Nom complet" required/>
        <input type="text" name="phone" placeholder="Téléphone"/>
        <input type="email" name="email" placeholder="Email"/>
        <input type="number" name="age" placeholder="Âge"/>
        <button class="btn-add" type="submit">Ajouter</button>
    </form>
    <% } %>

    <h2>Liste des patients</h2>
    <table class="medical-history-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nom</th>
                <th>Téléphone</th>
                <th>Email</th>
                <th>Âge</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Patient> patients = (List<Patient>) request.getAttribute("patientList");
                if (patients != null) {
                    for (Patient p : patients) {
                        // Escape special characters for JavaScript
                        String safeName = p.getName().replace("'", "\\'");
                        String safePhone = p.getPhone() != null ? p.getPhone().replace("'", "\\'") : "";
                        String safeEmail = p.getEmail() != null ? p.getEmail().replace("'", "\\'") : "";
            %>
            <tr>
                <td><%= p.getId() %></td>
                <td><%= p.getName() %></td>
                <td><%= p.getPhone() != null ? p.getPhone() : "-" %></td>
                <td><%= (p.getEmail() != null && !p.getEmail().isEmpty()) ? p.getEmail() : "À compléter" %></td>
                <td><%= p.getAge() > 0 ? p.getAge() : "-" %></td>
                <td class="action-buttons">
                    <a href="medical-records?patientId=<%= p.getId() %>" class="btn-view">Voir dossier</a>
                    <% if ("admin".equals(role) || "secretaire".equals(role)) { %>
                    <button class="btn-edit" onclick="openEditModal(<%= p.getId() %>, '<%= safeName %>', '<%= safePhone %>', '<%= safeEmail %>', <%= p.getAge() %>)">Modifier</button>
                    <button class="btn-delete" onclick="confirmDelete(<%= p.getId() %>)">Supprimer</button>
                    <% } %>
                </td>
            </tr
            <%
                    }
                }
            %>
        </tbody>
    </table>
</div>

<!-- Modale de suppression -->
<div id="deleteModal" class="modal-overlay">
    <div class="modal-box">
        <h3>Supprimer le patient</h3>
        <p>Êtes-vous sûr de vouloir supprimer ce patient ?<br>Cette action est irréversible.</p>
        <form method="post" action="patients">
            <input type="hidden" name="action" value="delete"/>
            <input type="hidden" name="id" id="deletePatientId"/>
            <button type="submit" class="btn-delete">Oui, supprimer</button>
            <button type="button" onclick="closeModal()" class="btn-cancel">Annuler</button>
        </form>
    </div>
</div>

<!-- Modale de modification -->
<div id="editModal" class="modal-overlay">
    <div class="modal-box">
        <h3>Modifier le patient</h3>
        <form method="post" action="patients">
            <input type="hidden" name="action" value="update"/>
            <input type="hidden" name="id" id="editId"/>
            <input type="text" name="name" id="editName" placeholder="Nom complet" required/>
            <input type="text" name="phone" id="editPhone" placeholder="Téléphone"/>
            <input type="email" name="email" id="editEmail" placeholder="Email"/>
            <input type="number" name="age" id="editAge" placeholder="Âge"/>
            <button type="submit" class="btn-add">Enregistrer</button>
            <button type="button" onclick="closeEditModal()" class="btn-cancel">Annuler</button>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/patients.js"></script>

</body>
</html>
