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
import java.util.List;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        List<Utilisateur> medecins = utilisateurDAO.findByRole("medecin");
        List<Utilisateur> secretaires = utilisateurDAO.findByRole("secretaire");

        request.setAttribute("medecins", medecins);
        request.setAttribute("secretaires", secretaires);

        request.getRequestDispatcher("/WEB-INF/views/admin-users.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {

            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String nom = request.getParameter("nom");
            String email = request.getParameter("email");
            String userRole = request.getParameter("userRole");

            utilisateurDAO.addUser(login, password, userRole, nom, email);
            if ("medecin".equals(userRole)) {
                session.setAttribute("msg", "Médecin ajouté avec succès");
            } else if ("secretaire".equals(userRole)) {
                session.setAttribute("msg", "Secrétaire ajouté avec succès");
            } else {
                session.setAttribute("msg", "Utilisateur ajouté avec succès");
            }

        } else if ("delete".equals(action)) {

            Long id = Long.parseLong(request.getParameter("id"));
            utilisateurDAO.deleteUser(id);
            session.setAttribute("msg", "Utilisateur supprimé avec succès");

        } else {
            session.setAttribute("msg", "Action invalide");
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}