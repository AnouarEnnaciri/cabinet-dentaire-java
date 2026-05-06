package com.cabinet.servlet;

import com.cabinet.dao.PatientDAO;
import com.cabinet.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/patients/edit")
public class PatientEditServlet extends HttpServlet {

    private final PatientDAO dao = new PatientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        Patient patient = dao.findById(id);

        if (patient == null) {
            response.sendRedirect(request.getContextPath() + "/patients");
            return;
        }

        request.setAttribute("patient", patient);

        request.getRequestDispatcher("/WEB-INF/views/edit-patient.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("PATIENT UPDATE HIT");

        Long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        int age = Integer.parseInt(request.getParameter("age"));

        Patient p = new Patient();
        p.setId(id);
        p.setName(name);
        p.setPhone(phone);
        p.setEmail(email);
        p.setAge(age);

        dao.updatePatient(p);

        response.sendRedirect(request.getContextPath() + "/patients");
    }
}