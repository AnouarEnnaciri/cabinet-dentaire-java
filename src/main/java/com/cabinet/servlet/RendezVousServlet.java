package com.cabinet.servlet;

import com.cabinet.dao.PatientDAO;
import com.cabinet.dao.RendezVousDAO;
import com.cabinet.dao.UtilisateurDAO;
import com.cabinet.model.Patient;
import com.cabinet.model.RendezVous;
import com.cabinet.model.Utilisateur;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/rendezvous")
public class RendezVousServlet extends HttpServlet {
    private final RendezVousDAO rdvDAO = new RendezVousDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        Utilisateur user = (Utilisateur) session.getAttribute("user");

        List<RendezVous> rdvs;

        if ("medecin".equals(role)) {
            // Doctor sees only their own appointments
            rdvs = rdvDAO.getByMedecinId(user.getId());
        } else if ("secretaire".equals(role)) {
            // Secretary sees ALL appointments + pending requests
            rdvs = rdvDAO.getAll();
            List<RendezVous> pendingRdvs = rdvDAO.getByStatut("EN_ATTENTE");
            request.setAttribute("pendingRdvs", pendingRdvs);
        } else {
            // Admin sees all
            rdvs = rdvDAO.getAll();
        }

        // Enrich RDV with patient names
        for (RendezVous rdv : rdvs) {
            if (rdv.getPatientId() != null) {
                Patient p = patientDAO.findById(rdv.getPatientId());
                if (p != null) {
                    rdv.setPatientNom(p.getName());
                    rdv.setPatientTelephone(p.getPhone());
                }
            }
        }

        request.setAttribute("rdvList", rdvs);

        // Get list of doctors for dropdown
        List<Utilisateur> medecins = utilisateurDAO.findByRole("medecin");
        request.setAttribute("medecins", medecins);

        request.getRequestDispatcher("/WEB-INF/views/rendezvous.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            try {
                Long patientId = Long.parseLong(request.getParameter("patientId"));
                Long medecinId = Long.parseLong(request.getParameter("medecinId"));
                String dateStr = request.getParameter("date");
                String heureStr = request.getParameter("heure");
                String motif = request.getParameter("motif");

                // Verify patient exists
                Patient patient = patientDAO.findById(patientId);
                if (patient == null) {
                    response.sendRedirect("rendezvous?error=Patient non trouvé");
                    return;
                }

                // Parse date
                LocalDate date;
                try {
                    date = LocalDate.parse(dateStr);
                    int year = date.getYear();
                    if (year < 2020 || year > 2030) {
                        response.sendRedirect("rendezvous?error=Date invalide (année entre 2020 et 2030)");
                        return;
                    }
                } catch (DateTimeParseException e) {
                    response.sendRedirect("rendezvous?error=Date invalide");
                    return;
                }

                RendezVous rdv = new RendezVous();
                rdv.setPatientId(patientId);
                rdv.setPatientNom(patient.getName());
                rdv.setPatientTelephone(patient.getPhone());
                rdv.setMedecinId(medecinId);
                rdv.setDate(date);
                rdv.setMotif(motif);
                rdv.setStatut("planifie");
                rdv.setDateDemande(LocalDateTime.now());

                rdvDAO.save(rdv);

            } catch (NumberFormatException e) {
                response.sendRedirect("rendezvous?error=ID invalide");
                return;
            }
        }

        if ("confirm".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            rdvDAO.updateStatut(id, "confirme");
        }

        if ("refuse".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            rdvDAO.updateStatut(id, "annule");
        }

        response.sendRedirect("rendezvous");
    }
}