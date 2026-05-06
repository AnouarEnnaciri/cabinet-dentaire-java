package com.cabinet.servlet;

import com.cabinet.dao.ConsultationDAO;
import com.cabinet.model.Consultation;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/consultations/edit")
public class EditConsultationServlet extends HttpServlet {

    private final ConsultationDAO consultationDAO = new ConsultationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Long patientId = Long.parseLong(request.getParameter("patientId"));

        Consultation consultation = consultationDAO.getById(id);

        request.setAttribute("consultation", consultation);
        request.setAttribute("patientId", patientId);
        request.getRequestDispatcher("/WEB-INF/views/edit-consultation.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Long patientId = Long.parseLong(request.getParameter("patientId"));
        String diagnostic = request.getParameter("diagnostic");
        String traitement = request.getParameter("traitement");
        String notes = request.getParameter("notes");

        consultationDAO.updateConsultation(id, diagnostic, traitement, notes);

        response.sendRedirect(request.getContextPath() + "/medical-records?patientId=" + patientId);
    }
}