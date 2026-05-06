package com.cabinet.servlet;

import com.cabinet.dao.UtilisateurDAO;
import com.cabinet.model.Utilisateur;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/edit-user")
public class EditUserServlet extends HttpServlet {
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));
        Utilisateur user = utilisateurDAO.findById(id);

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/edit-user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));
        String login = request.getParameter("login");
        String nom = request.getParameter("nom");
        String email = request.getParameter("email");
        String userRole = request.getParameter("role");
        String newPassword = request.getParameter("newPassword");

        utilisateurDAO.updateUserWithPassword(id, login, nom, email, userRole, newPassword);

        // Message de succès
        session.setAttribute("msg", "Utilisateur modifié avec succès");

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}