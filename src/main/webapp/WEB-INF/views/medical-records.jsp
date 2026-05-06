<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cabinet.model.*" %>

<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }

    String role = (String) session.getAttribute("role");

    Patient patient = (Patient) request.getAttribute("patient");
    List<Consultation> consultations = (List<Consultation>) request.getAttribute("consultations");
    List<Prescription> prescriptions = (List<Prescription>) request.getAttribute("prescriptions");
    List<Antecedent> antecedents = (List<Antecedent>) request.getAttribute("antecedents");
    List<Acte> actes = (List<Acte>) request.getAttribute("actes");
    List<Medicament> medicaments = (List<Medicament>) request.getAttribute("medicaments");
    Long dossierId = (Long) request.getAttribute("dossierId");

    String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Dossiers médicaux - Cabinet Dentaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body data-context="<%= ctx %>">

<!-- ACTES TEMPLATE (REQUIRED for dynamic actes) -->
<select id="acte-options-template" class="hidden-template">
    <option value="">-- Sélectionner un acte --</option>
    <% if (actes != null) {
        for (Acte acte : actes) { %>
        <option value="<%= acte.getId() %>"><%= acte.getLibelle() %> - <%= acte.getPrixBase() %> DH</option>
    <% }} %>
</select>

<div class="container">

    <!-- TOP BAR -->
    <div class="top-bar">
        <a href="<%= ctx %>/dashboard" class="back-link">
            <i class="fas fa-arrow-left"></i> Retour tableau de bord
        </a>
        <a href="<%= ctx %>/logout" class="logout-btn">Déconnexion</a>
    </div>

    <h1><i class="fas fa-folder-medical"></i> Dossiers médicaux</h1>

    <!-- SEARCH -->
    <form method="get" action="<%= ctx %>/medical-records" class="search-form">
        <input type="number" name="patientId" placeholder="Entrez l'ID du patient" required class="search-input">
        <button type="submit" class="btn-add"><i class="fas fa-search"></i> Charger le dossier</button>
    </form>

    <% if (patient != null) { %>

    <!-- ANTÉCÉDENTS -->
    <h2>Antécédents médicaux</h2>
    <% if (antecedents != null && !antecedents.isEmpty()) { %>
    <div class="antecedents-list">
        <% for (Antecedent a : antecedents) { %>
        <div class="antecedent-badge <%= a.getType() %>">
            <i class="fas fa-<%= "allergie".equals(a.getType()) ? "allergies" : "notes-medical" %>"></i>
            <%= a.getDescription() %>
            <% if ("admin".equals(role) || "medecin".equals(role)) { %>
            <button class="delete-antecedent" onclick="deleteAntecedentWithModal(<%= a.getId() %>, <%= patient.getId() %>)">×</button>
            <% } %>
        </div>
        <% } %>
    </div>
    <% } else { %>
    <p class="info-message">Aucun antécédent enregistré.</p>
    <% } %>

    <% if ("admin".equals(role) || "medecin".equals(role)) { %>
    <button type="button" class="btn-add" onclick="openAddAntecedentModal(<%= patient.getId() %>)">
        <i class="fas fa-plus"></i> Ajouter un antécédent
    </button>
    <% } %>

    <!-- PATIENT INFO -->
    <div class="patient-info">
        <h3>Patient #<%= patient.getId() %></h3>
        <p><strong>Nom :</strong> <%= patient.getName() %></p>
        <p><strong>Téléphone :</strong> <%= patient.getPhone() != null ? patient.getPhone() : "Non renseigné" %></p>
        <p><strong>Email :</strong> <%= (patient.getEmail() != null && !patient.getEmail().isEmpty()) ? patient.getEmail() : "<em>À compléter</em>" %></p>
        <p><strong>Âge :</strong> <%= (patient.getAge() > 0) ? patient.getAge() + " ans" : "<em>À compléter</em>" %></p>
    </div>

    <!-- HISTORIQUE MÉDICAL -->
    <h2>Historique médical</h2>
    <% if (consultations != null && !consultations.isEmpty()) { %>


    <table class="medical-history-table">
        <thead>
            <tr>
                <th>Date</th>
                <th>Diagnostic</th>
                <th>Traitement</th>
                <th>Ordonnance</th>
                <th>Notes</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% for (Consultation c : consultations) { %>
            <tr class="consultation-row">
                <td class="consultation-date"><%= c.getDate() %></td>
                <td class="consultation-diagnostic"><%= c.getDiagnostic() != null ? c.getDiagnostic() : "-" %></td>
                <td class="consultation-traitement"><%= c.getTraitement() != null ? c.getTraitement() : "-" %></td>
                <td class="consultation-prescription">
                    <%
                        boolean found = false;
                        if (prescriptions != null) {
                            for (Prescription p : prescriptions) {
                                if (p.getConsultationId() == c.getId()) {
                                    found = true;
                    %>
                    <div class="prescription-display">
                        <strong><%= p.getMedicamentNom() %></strong>
                        <small class="posologie-text"><%= p.getPosologie() %></small>
                    </div>
                    <%          }
                            }
                        }
                        if (!found) { %>
                        <span class="empty-field">-</span>
                    <% } %>
                </td
                <td class="consultation-notes"><%= c.getNotes() != null ? c.getNotes() : "-" %> </td
                <td class="action-cell">
                    <% if ("admin".equals(role) || "secretaire".equals(role)) { %>
                    <button class="btn-facture" onclick="genererFacture(<%= patient.getId() %>, <%= c.getId() %>)">
                        <i class="fas fa-file-invoice"></i> Facture
                    </button>
                    <% } %>

                    <% if ("admin".equals(role) || "medecin".equals(role)) { %>
                    <div class="btn-group">
                        <select id="certificatType_<%= c.getId() %>" class="certificat-select">
                            <option value="standard">Certificat standard</option>
                            <option value="arret-travail">Arrêt de travail</option>
                            <option value="scolaire">Certificat scolaire</option>
                            <option value="sport">Certificat sport</option>
                        </select>
                        <button class="btn-certificat" onclick="genererCertificat(<%= patient.getId() %>, <%= c.getId() %>)">
                            <i class="fas fa-file-pdf"></i> Certificat
                        </button>
                    </div>

                    <a href="<%= ctx %>/consultations/edit?id=<%= c.getId() %>&patientId=<%= patient.getId() %>" class="btn-fix">Modifier</a>
                    <button class="btn-delete" onclick="deleteConsultation(<%= c.getId() %>, <%= patient.getId() %>)">Supprimer</button>
                    <% } %>
                </td
            </tr
            <% } %>
        </tbody>
     </table

    <% } else { %>
    <p class="info-message">Aucun historique médical trouvé pour ce patient.</p>
    <% } %>

    <!-- AJOUTER CONSULTATION -->
    <% if ("admin".equals(role) || "medecin".equals(role)) { %>
    <h2>Ajouter une consultation</h2>

    <form method="post" action="<%= ctx %>/medical-records" class="consultation-form" id="consultationForm">
        <input type="hidden" name="action" value="addConsultation"/>
        <input type="hidden" name="patientId" value="<%= patient.getId() %>"/>
        <input type="hidden" name="dossierId" value="<%= dossierId %>"/>

        <div class="form-group">
            <label>Date :</label>
            <input type="date" name="date" required/>
        </div>

        <div class="form-group">
            <label>Diagnostic :</label>
            <textarea name="diagnostic" rows="2" class="full-width-text"></textarea>
        </div>

        <div class="form-group">
            <label>Traitement :</label>
            <textarea name="traitement" rows="2" class="full-width-text"></textarea>
        </div>

        <div class="form-group">
            <label>Actes réalisés :</label>
            <div id="actes-list">
                <div class="acte-row">
                    <select name="acteId_0" class="acte-select" onchange="toggleDentField(this, 0)">
                        <option value="">-- Sélectionner un acte --</option>
                        <% for (Acte acte : actes) { %>
                        <option value="<%= acte.getId() %>"><%= acte.getLibelle() %> - <%= acte.getPrixBase() %> DH</option>
                        <% } %>
                    </select>
                    <input type="number" name="numDent_0" class="dent-input" placeholder="N° dent">
                    <label class="inline-label">Qté:</label>
                    <input type="number" name="quantite_0" class="quantite-input" value="1">
                    <button type="button" class="btn-add-acte" onclick="addActeRow()">+</button>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label><i class="fas fa-prescription-bottle"></i> Prescriptions / Ordonnance :</label>
            <div id="prescriptions-list">
                <div class="prescription-row">
                    <select name="medicament_0" class="medicament-select" onchange="toggleCustomInput(this, 0)">
                        <option value="">-- Sélectionner un médicament --</option>
                        <option value="___CUSTOM___">--- Écrire un autre médicament ---</option>
                        <% if (medicaments != null) {
                            for (Medicament m : medicaments) { %>
                        <option value="<%= m.getNom() %>"><%= m.getNom() %></option>
                        <% }} %>
                    </select>
                    <input type="text" name="custom_medicament_0" class="custom-medicament" placeholder="Nom du médicament">
                    <input type="text" name="posologie_0" class="posologie-input" placeholder="Posologie (ex: 2x/jour, 7 jours)">
                    <button type="button" class="btn-add-prescription" onclick="addPrescriptionRow()">+</button>
                </div>
            </div>
            <small class="info-message">Ajoutez tous les médicaments prescrits avec leur posologie.</small>
        </div>

        <div class="form-group">
            <label>Notes :</label>
            <textarea name="notes" rows="2" class="full-width-text"></textarea>
        </div>

        <button type="submit" class="btn-add"><i class="fas fa-save"></i> Enregistrer la consultation</button>
    </form>
    <% } else if ("secretaire".equals(role)) { %>
    <p class="info-message"><i class="fas fa-info-circle"></i> Accès limité : seul le médecin peut ajouter des consultations.</p>
    <% } %>

    <% } else if (request.getAttribute("patientNotFound") != null) { %>
    <div class="error-message">
        <i class="fas fa-exclamation-triangle"></i> Patient avec l'ID <strong><%= request.getAttribute("patientId") %></strong> n'existe pas.
    </div>
    <% } %>

</div>

<!-- MODALS -->
<div id="consultationDeleteModal" class="modal-overlay">
    <div class="modal-box">
        <h3>Supprimer la consultation</h3>
        <p>Cette action est irréversible.</p>
        <div class="modal-actions">
            <button class="btn-delete" onclick="confirmDeleteConsultation()">Oui, supprimer</button>
            <button class="btn-cancel" onclick="closeDeleteConsultationModal()">Annuler</button>
        </div>
    </div>
</div>

<div id="antecedentModal" class="modal-overlay">
    <div class="modal-box">
        <h3>Ajouter un antécédent médical</h3>
        <form id="antecedentForm">
            <input type="hidden" name="patientId" id="antecedentPatientId">
            <div class="form-group">
                <label>Type :</label>
                <select name="type" required>
                    <option value="allergie">Allergie</option>
                    <option value="maladie">Maladie chronique</option>
                    <option value="traitement">Traitement en cours</option>
                </select>
            </div>
            <div class="form-group">
                <label>Description :</label>
                <textarea name="description" rows="3" required placeholder="Ex: Allergie à la pénicilline"></textarea>
            </div>
            <div class="modal-actions">
                <button type="button" class="btn-add" onclick="saveAntecedent()">Enregistrer</button>
                <button type="button" class="btn-cancel" onclick="closeAntecedentModal()">Annuler</button>
            </div>
        </form>
    </div>
</div>

<div id="deleteAntecedentModal" class="modal-overlay">
    <div class="modal-box">
        <h3>Supprimer l'antécédent</h3>
        <p>Êtes-vous sûr de vouloir supprimer cet antécédent ?</p>
        <div class="modal-actions">
            <button class="btn-delete" id="confirmDeleteAntecedentBtn">Oui, supprimer</button>
            <button class="btn-cancel" onclick="closeDeleteAntecedentModal()">Annuler</button>
        </div>
    </div>
</div>

<script src="<%= ctx %>/js/medical-records.js"></script>
</body>
</html>