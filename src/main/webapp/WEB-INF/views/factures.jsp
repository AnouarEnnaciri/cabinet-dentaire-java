<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cabinet.model.Facture" %>
<%@ page import="com.cabinet.dao.PatientDAO" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
    String role = (String) session.getAttribute("role");
    if ("medecin".equals(role)) {
        response.sendRedirect("dashboard");
        return;
    }

    PatientDAO patientDAO = new PatientDAO();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Facturation - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <script src="${pageContext.request.contextPath}/js/factures.js"></script>
</head>
<body>
<div class="container">
    <div class="top-bar">
        <a href="dashboard" class="back-link"><i class="fas fa-arrow-left"></i> Retour tableau de bord</a>
        <a href="logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-file-invoice-dollar"></i> Facturation</h1>

    <%
        List<Facture> factures = (List<Facture>) request.getAttribute("factures");
        if (factures != null && !factures.isEmpty()) {
    %>
    <table class="medical-history-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Patient</th>
                <th>Date</th>
                <th>Montant total</th>
                <th>Montant payé</th>
                <th>Reste à payer</th>
                <th>Statut</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                for (Facture f : factures) {
                    double resteAPayer = f.getMontantTotal() - f.getMontantPaye();
                    String patientName = "Patient #" + f.getPatientId();
                    try {
                        com.cabinet.model.Patient p = patientDAO.findById(f.getPatientId());
                        if (p != null) patientName = p.getName();
                    } catch(Exception e) {}
            %>
            <tr>
                <td><%= f.getId() %></td>
                <td><%= patientName %></td>
                <td><%= f.getDateFacture().toString().substring(0, 10) %></td>
                <td><%= String.format("%.2f", f.getMontantTotal()) %> DH</td>
                <td><%= String.format("%.2f", f.getMontantPaye()) %> DH</td>
                <td style="color: <%= resteAPayer > 0 ? "#d9534f" : "#5cb85c" %>; font-weight: bold;">
                    <%= String.format("%.2f", resteAPayer) %> DH
                </td>
                <td>
                    <% if ("ANNULÉE".equals(f.getStatut())) { %>
                        <span style="color: #d9534f; font-weight: bold;">✗ ANNULÉE</span>
                    <% } else if (resteAPayer == 0) { %>
                        <span style="color: #5cb85c; font-weight: bold;">✓ PAYÉE</span>
                    <% } else if (f.getMontantPaye() > 0) { %>
                        <span style="color: #f0ad4e; font-weight: bold;">PARTIELLE</span>
                    <% } else { %>
                        <span style="color: #d9534f; font-weight: bold;">EN_ATTENTE</span>
                    <% } %>
                </td>
                <td>
                    <% if ("ANNULÉE".equals(f.getStatut())) { %>
                        <span style="color: #999;">-</span>
                    <% } else if (resteAPayer == 0) { %>
                        <i class="fas fa-check-circle" style="color: #5cb85c; font-size: 1.2rem;"></i>
                    <% } else { %>
                        <button onclick="payerFacture(<%= f.getId() %>, <%= f.getMontantTotal() %>, <%= f.getMontantPaye() %>)"
                                class="btn-add" style="padding: 5px 10px;">
                            <i class="fas fa-credit-card"></i> Encaisser
                        </button>
                        <% if ("EN_ATTENTE".equals(f.getStatut())) { %>
                        <button onclick="annulerFacture(<%= f.getId() %>)"
                                class="btn-delete" style="padding: 5px 10px; margin-left: 5px;">
                            <i class="fas fa-ban"></i> Annuler
                        </button>
                        <% } %>
                    <% } %>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <% } else { %>
    <p class="info-message">Aucune facture trouvée.</p>
    <% } %>
</div>

<!-- MODAL PAIEMENT -->
<div id="paiementModal" class="modal-overlay">
    <div class="modal-box" style="max-width: 400px;">
        <h3><i class="fas fa-credit-card"></i> Encaisser le paiement</h3>
        <div id="paiementInfo" style="margin-bottom: 20px;">
            <p><strong>Total :</strong> <span id="modalTotal">0</span> DH</p>
            <p><strong>Déjà payé :</strong> <span id="modalDejaPaye">0</span> DH</p>
            <p><strong>Reste à payer :</strong> <span id="modalReste">0</span> DH</p>
        </div>

        <div class="form-group">
            <label>Montant à encaisser (DH) :</label>
            <input type="number" id="montantPaiement" class="search-input" step="0.01" placeholder="Saisir le montant reçu"/>
        </div>

        <div class="modal-actions" style="margin-top: 20px;">
            <button class="btn-add" onclick="confirmerPaiement()">
                <i class="fas fa-check"></i> Valider
            </button>
            <button class="btn-cancel" onclick="fermerModalPaiement()">
                <i class="fas fa-times"></i> Annuler
            </button>
        </div>
    </div>
</div>

<!-- MODAL ANNULATION -->
<div id="annulationModal" class="modal-overlay">
    <div class="modal-box" style="max-width: 400px;">
        <h3><i class="fas fa-ban" style="color: #d9534f;"></i> Annuler la facture</h3>
        <div style="margin-bottom: 20px;">
            <p style="color: #d9534f; font-weight: bold;">Attention !</p>
            <p>Cette action est irréversible.</p>
            <p>La facture sera marquée comme <strong>ANNULÉE</strong> et ne pourra plus être modifiée.</p>
        </div>

        <div class="modal-actions" style="margin-top: 20px;">
            <button class="btn-delete" onclick="confirmerAnnulation()">
                <i class="fas fa-check"></i> Oui, annuler
            </button>
            <button class="btn-cancel" onclick="fermerModalAnnulation()">
                <i class="fas fa-times"></i> Non, retour
            </button>
        </div>
    </div>
</div>

</body>
</html>