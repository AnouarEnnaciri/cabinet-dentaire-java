package com.cabinet.servlet;

import com.cabinet.dao.ConsultationDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/deleteConsultation")
public class DeleteConsultationServlet extends HttpServlet {
    private final ConsultationDAO consultationDAO = new ConsultationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role) && !"medecin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Long consultationId = Long.parseLong(request.getParameter("id"));
        Long patientId = Long.parseLong(request.getParameter("patientId"));



        consultationDAO.deleteConsultation(consultationId);

        response.sendRedirect(request.getContextPath() + "/medical-records?patientId=" + patientId);
    }
}