package com.cabinet.servlet;

import com.cabinet.dao.AntecedentDAO;
import com.cabinet.model.Antecedent;
import com.cabinet.dao.ActeDAO;
import com.cabinet.dao.ConsultationDAO;
import com.cabinet.dao.DossierMedicalDAO;
import com.cabinet.dao.InterventionDAO;
import com.cabinet.dao.PatientDAO;
import com.cabinet.dao.MedicamentDAO;
import com.cabinet.dao.PrescriptionDAO;
import com.cabinet.model.Acte;
import com.cabinet.model.Consultation;
import com.cabinet.model.DossierMedical;
import com.cabinet.model.InterventionMedecin;
import com.cabinet.model.Patient;
import com.cabinet.model.Medicament;
import com.cabinet.model.Prescription;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/medical-records")
public class MedicalRecordServlet extends HttpServlet {
    private final PatientDAO patientDAO = new PatientDAO();
    private final DossierMedicalDAO dossierDAO = new DossierMedicalDAO();
    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private final InterventionDAO interventionDAO = new InterventionDAO();
    private final ActeDAO acteDAO = new ActeDAO();
    private final MedicamentDAO medicamentDAO = new MedicamentDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"medecin".equals(role) && !"secretaire".equals(role)) {
            response.sendRedirect("dashboard");
            return;
        }

        // Declare patient outside the if block
        Patient patient = null;

        String patientIdParam = request.getParameter("patientId");
        if (patientIdParam != null && !patientIdParam.isEmpty()) {
            Long patientId = Long.parseLong(patientIdParam);
            patient = patientDAO.findById(patientId);

            if (patient != null) {
                request.setAttribute("patient", patient);
                request.setAttribute("patientId", patientId);

                DossierMedical dossier = dossierDAO.getOrCreateByPatientId(patientId);
                if (dossier != null) {
                    List<Consultation> consultations = consultationDAO.getByDossierId(dossier.getId());
                    request.setAttribute("consultations", consultations);
                    request.setAttribute("dossierId", dossier.getId());

                    // Load prescriptions for all consultations
                    List<Prescription> allPrescriptions = new ArrayList<>();
                    for (Consultation c : consultations) {
                        // Convert Long to int safely
                        Long consultationIdInt = c.getId();
                        allPrescriptions.addAll(prescriptionDAO.getByConsultationId(consultationIdInt));
                    }
                    request.setAttribute("prescriptions", allPrescriptions);

                    // Load antecedents for this patient
                    AntecedentDAO antecedentDAO = new AntecedentDAO();
                    List<Antecedent> antecedents = antecedentDAO.getByPatientId(patientId);
                    request.setAttribute("antecedents", antecedents);
                }
            } else {
                request.setAttribute("patientNotFound", true);
                request.setAttribute("patientId", patientId);
            }
        }

        // Load actes and medicaments 
        List<Acte> actes = acteDAO.getAllActes();
        List<Medicament> medicaments = medicamentDAO.getAll();
        request.setAttribute("actes", actes);
        request.setAttribute("medicaments", medicaments);

        request.getRequestDispatcher("/WEB-INF/views/medical-records.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        String action = request.getParameter("action");

        if ("addConsultation".equals(action)) {
            if (!"admin".equals(role) && !"medecin".equals(role) && !"secretaire".equals(role)) {
                response.sendRedirect("dashboard");
                return;
            }

            Long dossierId = Long.parseLong(request.getParameter("dossierId"));
            LocalDate date = LocalDate.parse(request.getParameter("date"));
            String diagnostic = request.getParameter("diagnostic");
            String traitement = request.getParameter("traitement");
            String notes = request.getParameter("notes");
            String patientId = request.getParameter("patientId");

            Consultation consultation = new Consultation(dossierId, date, diagnostic, traitement, notes);

            com.cabinet.model.Utilisateur loggedUser = (com.cabinet.model.Utilisateur) session.getAttribute("user");
            if (loggedUser != null) {
                consultation.setMedecinId((long) loggedUser.getId());
            }

            Long consultationId = consultationDAO.save(consultation);

            // Save interventions (acts)
            int i = 0;
            while (true) {
                String acteIdParam = request.getParameter("acteId_" + i);
                if (acteIdParam == null || acteIdParam.isEmpty()) {
                    break;
                }

                Long acteId = Long.parseLong(acteIdParam);
                String numDentParam = request.getParameter("numDent_" + i);
                Integer numDent = (numDentParam != null && !numDentParam.isEmpty()) ? Integer.parseInt(numDentParam) : null;
                Integer quantite = Integer.parseInt(request.getParameter("quantite_" + i));

                Acte acte = acteDAO.findById(acteId);
                Double prixUnitaire = acte.getPrixBase();

                InterventionMedecin intervention = new InterventionMedecin(consultationId, acteId, numDent, quantite, prixUnitaire);
                interventionDAO.save(intervention);
                i++;
            }

            // Save prescriptions
            List<Prescription> prescriptions = new ArrayList<>();
            int p = 0;
            while (true) {
                String medicamentValue = request.getParameter("medicament_" + p);
                if (medicamentValue == null || medicamentValue.isEmpty()) {
                    break;
                }

                String posologie = request.getParameter("posologie_" + p);

                String medicamentNom;
                if (medicamentValue.equals("___CUSTOM___")) {
                    medicamentNom = request.getParameter("custom_medicament_" + p);
                    if (medicamentNom == null || medicamentNom.isEmpty()) {
                        p++;
                        continue;
                    }
                } else {
                    medicamentNom = medicamentValue;
                }

                Prescription prescription = new Prescription(medicamentNom, posologie);
                prescriptions.add(prescription);
                p++;
            }

            if (!prescriptions.isEmpty()) {
                prescriptionDAO.saveAll(consultationId, prescriptions);
            }

            response.sendRedirect("medical-records?patientId=" + patientId);
            return;
        }

        String patientId = request.getParameter("patientId");
        if (patientId != null && !patientId.isEmpty()) {
            response.sendRedirect("medical-records?patientId=" + patientId);
        } else {
            response.sendRedirect("dashboard");
        }
    }
}