package com.cabinet.servlet;

import com.cabinet.dao.ConsultationDAO;
import com.cabinet.model.Consultation;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/editConsultation")
public class ConsultationEditServlet extends HttpServlet {
    private final ConsultationDAO consultationDAO = new ConsultationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"medecin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String action = request.getParameter("action");

        // deleting
        if ("delete".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Long patientId = Long.parseLong(request.getParameter("patientId"));
            consultationDAO.deleteConsultation(id);
            response.sendRedirect(request.getContextPath() + "/medical-records?patientId=" + patientId);
            return;
        }

        // editing
        String idParam = request.getParameter("id");
        String patientIdParam = request.getParameter("patientId");

        if (idParam == null || patientIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/medical-records");
            return;
        }

        Long id = Long.parseLong(idParam);
        Long patientId = Long.parseLong(patientIdParam);

        Consultation consultation = consultationDAO.getById(id);

        if (consultation == null) {
            response.sendRedirect(request.getContextPath() + "/medical-records?patientId=" + patientId);
            return;
        }

        request.setAttribute("consultation", consultation);
        request.setAttribute("patientId", patientId);
        request.getRequestDispatcher("/WEB-INF/views/edit-consultation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"medecin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));
        Long patientId = Long.parseLong(request.getParameter("patientId"));
        String diagnostic = request.getParameter("diagnostic");
        String traitement = request.getParameter("traitement");
        String notes = request.getParameter("notes");

        consultationDAO.updateConsultation(id, diagnostic, traitement, notes);

        response.sendRedirect(request.getContextPath() + "/medical-records?patientId=" + patientId);
    }
}