package com.cabinet.servlet;

import com.cabinet.dao.FactureDAO;
import com.cabinet.model.Facture;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/factures")
public class FactureServlet extends HttpServlet {
    private final FactureDAO factureDAO = new FactureDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if ("medecin".equals(role)) {
            response.sendRedirect("dashboard");
            return;
        }

        List<Facture> factures = factureDAO.getAll();
        request.setAttribute("factures", factures);
        request.getRequestDispatcher("/WEB-INF/views/factures.jsp").forward(request, response);
    }
}