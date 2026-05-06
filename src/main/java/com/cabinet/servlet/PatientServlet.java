package com.cabinet.servlet;

import com.cabinet.dao.PatientDAO;
import com.cabinet.model.Patient;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    private final PatientDAO patientDAO = new PatientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Patient> patients = patientDAO.getAllPatients();
        request.setAttribute("patientList", patients);
        request.getRequestDispatcher("/WEB-INF/views/patients.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            int age = Integer.parseInt(request.getParameter("age"));
            patientDAO.addPatient(new Patient(name, phone, email, age));
        }

        if ("delete".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            patientDAO.deletePatient(id);
        }

        if ("update".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            int age = Integer.parseInt(request.getParameter("age"));

            Patient patient = new Patient(name, phone, email, age);
            patient.setId(id);
            patientDAO.updatePatient(patient);
        }

        response.sendRedirect(request.getContextPath() + "/patients");
    }
}