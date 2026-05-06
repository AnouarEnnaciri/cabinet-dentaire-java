package com.cabinet.servlet;

import com.cabinet.dao.FactureDAO;
import com.cabinet.dao.InterventionDAO;
import com.cabinet.model.Facture;
import com.cabinet.model.InterventionMedecin;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/generer-facture")
public class GenererFactureServlet extends HttpServlet {
    private final InterventionDAO interventionDAO = new InterventionDAO();
    private final FactureDAO factureDAO = new FactureDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Long patientId = Long.parseLong(request.getParameter("patientId"));
        Long consultationId = Long.parseLong(request.getParameter("consultationId"));

        // Check if facture already exists
        List<Facture> existing = factureDAO.getByConsultationId(consultationId);
        if (!existing.isEmpty()) {
            response.sendRedirect("factures");
            return;
        }

        List<InterventionMedecin> interventions = interventionDAO.getByConsultationId(consultationId);

        // DEBUG: Print to console
        System.out.println("=== FACTURE GENERATION ===");
        System.out.println("Consultation ID: " + consultationId);
        System.out.println("Interventions found: " + interventions.size());

        double montantTotal = 0;
        for (InterventionMedecin i : interventions) {
            System.out.println("  - Acte ID: " + i.getActeId() +
                    ", Prix: " + i.getPrixUnitaire() +
                    ", Qte: " + i.getQuantite());
            montantTotal += i.getPrixUnitaire() * i.getQuantite();
        }

        System.out.println("Total: " + montantTotal + " DH");

        if (montantTotal == 0) {
            System.out.println("WARNING: Total is 0! Check if interventions have prixUnitaire set.");
        }

        Facture facture = new Facture(patientId, consultationId, LocalDateTime.now(), montantTotal, 0.0, "EN_ATTENTE");
        factureDAO.save(facture);

        response.sendRedirect("factures");
    }
}