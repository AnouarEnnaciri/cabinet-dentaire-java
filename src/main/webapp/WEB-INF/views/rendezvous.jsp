<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cabinet.model.RendezVous" %>
<%@ page import="com.cabinet.model.Patient" %>
<%@ page import="com.cabinet.model.Utilisateur" %>
<%@ page import="com.cabinet.dao.PatientDAO" %>
<%@ page import="com.cabinet.dao.UtilisateurDAO" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
    String role = (String) session.getAttribute("role");

    PatientDAO patientDAO = new PatientDAO();
    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Rendez-vous - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/rendezvous.css">
    <script src="${pageContext.request.contextPath}/js/rendezvous.js"></script>
</head>
<body>
<div class="container">
    <div class="top-bar">
        <a href="dashboard" class="back-link"><i class="fas fa-arrow-left"></i> Retour tableau de bord</a>
        <a href="logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-calendar-alt"></i> Rendez-vous</h1>

    <%
        if (!"medecin".equals(role)) {
    %>
    <div class="rdv-form-section">
        <h2>Ajouter un rendez-vous</h2>
        <form method="post" action="rendezvous" class="rdv-form">
            <input type="hidden" name="action" value="add"/>
            <div class="form-group">
                <label>ID Patient</label>
                <input type="number" name="patientId" placeholder="ID du patient" required/>
            </div>
            <div class="form-group">
                <label>Médecin</label>
                <select name="medecinId" required>
                    <option value="">-- Sélectionner --</option>
                    <%
                        List<Utilisateur> medecins = utilisateurDAO.findByRole("medecin");
                        if (medecins != null) {
                            for (Utilisateur m : medecins) {
                    %>
                    <option value="<%= m.getId() %>"><%= m.getNom() != null ? m.getNom() : m.getLogin() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label>Date</label>
                <input type="date" name="date" required/>
            </div>
            <div class="form-group">
                <label>Heure</label>
                <input type="time" name="heure" required/>
            </div>
            <div class="form-group">
                <label>Motif</label>
                <input type="text" name="motif" placeholder="Motif"/>
            </div>
            <div class="form-group">
                <button type="submit" class="btn-add"><i class="fas fa-plus"></i> Ajouter</button>
            </div>
        </form>
    </div>
    <%
        }
    %>


    <h2>Liste des rendez-vous</h2>
    <table class="medical-history-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Patient</th>
                <th>Médecin</th>
                <th>Date</th>
                <th>Heure</th>
                <th>Motif</th>
                <th>Statut</th>
                <% if (!"medecin".equals(role)) { %>
                <th>Action</th>
                <% } %>
            </tr>
        </thead>
        <tbody>
            <%
                List<RendezVous> rdvs = (List<RendezVous>) request.getAttribute("rdvList");
                if (rdvs != null && !rdvs.isEmpty()) {
                    for (RendezVous rdv : rdvs) {
                        String patientName = "Inconnu";
                        if (rdv.getPatientId() != null) {
                            Patient p = patientDAO.findById(rdv.getPatientId());
                            if (p != null) patientName = p.getName();
                        }

                        String medecinName = "Médecin " + rdv.getMedecinId();
                        if (rdv.getMedecinId() != null) {
                            Utilisateur medecin = utilisateurDAO.findById(rdv.getMedecinId());
                            if (medecin != null && medecin.getNom() != null) {
                                medecinName = medecin.getNom();
                            }
                        }
            %>
            <tr class="rdv-row">
                <td class="rdv-id"><%= rdv.getId() %></td>
                <td class="rdv-patient"><%= patientName %></td>
                <td class="rdv-medecin"><%= medecinName %></td>
                <td class="rdv-date"><%= rdv.getDate() %></td>
                <td class="rdv-time"><%= rdv.getHeure() != null ? rdv.getHeure() : "-" %></td>
                <td class="rdv-motif"><%= rdv.getMotif() != null ? rdv.getMotif() : "-" %></td>
                <td class="rdv-statut"><%= rdv.getStatut() %></td>
                <% if (!"medecin".equals(role)) { %>
                <td class="rdv-action">
                    <button class="btn-delete" onclick="confirmDelete(<%= rdv.getId() %>)">Supprimer</button>
                </td>
                <% } %>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="8" style="text-align: center;">Aucun rendez-vous trouvé.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</div>
<!-- MODAL SUPPRESSION RENDEZ-VOUS -->
<div id="deleteRdvModal" class="modal-overlay">
    <div class="modal-box">
        <h3><i class="fas fa-trash-alt" style="color: #d9534f;"></i> Supprimer le rendez-vous</h3>
        <p>Êtes-vous sûr de vouloir supprimer ce rendez-vous ?</p>
        <p style="color: #d9534f; font-size: 14px;">Cette action est irréversible.</p>
        <div class="modal-actions">
            <button class="btn-delete" id="confirmDeleteBtn">Oui, supprimer</button>
            <button class="btn-cancel" id="cancelDeleteBtn">Annuler</button>
        </div>
    </div>
</div>
</body>
</html>