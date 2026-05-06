package com.cabinet.servlet;

import com.cabinet.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/deleteRdv")
public class DeleteRdvServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("rendezvous");
            return;
        }

        Long rdvId = Long.parseLong(idParam);

        String sql = "DELETE FROM rendez_vous WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, rdvId);
            int deleted = pstmt.executeUpdate();

            System.out.println("Rendez-vous supprimé: " + rdvId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("rendezvous");
    }
}