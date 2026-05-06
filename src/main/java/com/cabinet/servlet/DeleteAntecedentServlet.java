package com.cabinet.servlet;

import com.cabinet.dao.AntecedentDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/delete-antecedent")
public class DeleteAntecedentServlet extends HttpServlet {
    private final AntecedentDAO antecedentDAO = new AntecedentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"medecin".equals(role)) {
            response.sendError(403, "Accès non autorisé");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));
        antecedentDAO.delete(id);

        response.setStatus(200);
    }
}