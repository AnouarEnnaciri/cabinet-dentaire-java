package com.cabinet.servlet;

import com.cabinet.dao.RendezVousDAO;
import com.cabinet.dao.UtilisateurDAO;
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
import java.time.LocalTime;
import java.util.List;

@WebServlet("/confirm-rdv")
public class ConfirmRdvServlet extends HttpServlet {
    private final RendezVousDAO rdvDAO = new RendezVousDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"secretaire".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Long rdvId = Long.parseLong(request.getParameter("id"));
        RendezVous rdv = rdvDAO.getById(rdvId);
        List<Utilisateur> medecins = utilisateurDAO.findByRole("medecin");

        request.setAttribute("rdv", rdv);
        request.setAttribute("medecins", medecins);
        request.getRequestDispatcher("/WEB-INF/views/confirm-rdv.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"secretaire".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Long rdvId = Long.parseLong(request.getParameter("rdvId"));
        Long medecinId = Long.parseLong(request.getParameter("medecinId"));
        LocalDate date = LocalDate.parse(request.getParameter("date"));
        LocalTime heure = LocalTime.parse(request.getParameter("heure"));

        rdvDAO.confirmRdv(rdvId, medecinId, date, heure);

        response.sendRedirect(request.getContextPath() + "/pending-rdv");
    }
}