package com.cabinet.servlet;

import com.cabinet.dao.UtilisateurDAO;
import com.cabinet.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/users/edit")
public class AdminUserEditServlet extends HttpServlet {

    private final UtilisateurDAO dao = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));

        Utilisateur user = dao.findById(id);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        request.setAttribute("user", user);

        request.getRequestDispatcher("/WEB-INF/views/edit-user.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        String login = request.getParameter("login");
        String nom = request.getParameter("nom");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        dao.updateUser(id, login, nom, email, role);

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}