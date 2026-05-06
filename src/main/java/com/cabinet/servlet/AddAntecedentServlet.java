package com.cabinet.servlet;

import com.cabinet.dao.AntecedentDAO;
import com.cabinet.model.Antecedent;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/add-antecedent")
public class AddAntecedentServlet extends HttpServlet {
    private final AntecedentDAO antecedentDAO = new AntecedentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"medecin".equals(role)) {
            response.sendError(403, "Accès non autorisé");
            return;
        }

        Long patientId = Long.parseLong(request.getParameter("patientId"));
        String type = request.getParameter("type");
        String description = request.getParameter("description");

        Antecedent antecedent = new Antecedent(patientId, type, description, LocalDateTime.now());
        antecedentDAO.save(antecedent);

        response.setStatus(200);
    }
}