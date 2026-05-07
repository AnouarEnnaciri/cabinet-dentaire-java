package com.cabinet.servlet;

import com.cabinet.dao.RendezVousDAO;
import com.cabinet.model.RendezVous;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/pending-rdv")
public class PendingRdvServlet extends HttpServlet {
    private final RendezVousDAO rdvDAO = new RendezVousDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        // only admin and secretary can see requests
        if (!"admin".equals(role) && !"secretaire".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        List<RendezVous> pendingRdvs = rdvDAO.getPendingRdvs();
        request.setAttribute("pendingRdvs", pendingRdvs);
        request.getRequestDispatcher("/WEB-INF/views/pending-rdv.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"secretaire".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));
        String action = request.getParameter("action");

        if ("accepter".equals(action)) {
            rdvDAO.updateStatut(id, "CONFIRME");
        } else if ("refuser".equals(action)) {
            rdvDAO.updateStatut(id, "ANNULE");
        }

        response.sendRedirect(request.getContextPath() + "/pending-rdv");
    }
}