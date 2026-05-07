package com.cabinet.servlet;

import com.cabinet.dao.PatientDAO;
import com.cabinet.dao.RendezVousDAO;
import com.cabinet.model.Patient;
import com.cabinet.model.RendezVous;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/public-rdv")
public class PublicRdvServlet extends HttpServlet {
    private final RendezVousDAO rdvDAO = new RendezVousDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/public-rdv.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nom = request.getParameter("nom");
        String telephone = request.getParameter("telephone");
        String dateStr = request.getParameter("date");
        String motif = request.getParameter("motif");

        LocalDate date = LocalDate.parse(dateStr);

        // create or get patient
        Patient patient = patientDAO.findOrCreateByNomTelephone(nom, telephone);

        // create RDV
        RendezVous rdv = new RendezVous();
        rdv.setPatientId(patient.getId());
        rdv.setDate(date);
        rdv.setMotif(motif);
        rdv.setStatut("EN_ATTENTE");
        rdv.setDateDemande(LocalDateTime.now());
        rdv.setPatientNom(nom);
        rdv.setPatientTelephone(telephone);

        rdvDAO.savePublicRdv(rdv);

        request.setAttribute("success", "Votre demande de rendez-vous a été envoyée. Nous vous contacterons.");
        request.getRequestDispatcher("/WEB-INF/views/public-rdv.jsp").forward(request, response);
    }
}