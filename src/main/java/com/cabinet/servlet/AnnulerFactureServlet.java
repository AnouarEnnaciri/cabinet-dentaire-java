package com.cabinet.servlet;

import com.cabinet.util.DatabaseUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/annuler-facture")
public class AnnulerFactureServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String factureIdParam = request.getParameter("factureId");

        if (factureIdParam == null || factureIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing factureId");
            return;
        }

        Long factureId = Long.parseLong(factureIdParam);

        String sql = "UPDATE factures SET statut = 'ANNULÉE' WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, factureId);
            int updated = pstmt.executeUpdate();

            if (updated > 0) {
                System.out.println("Facture " + factureId + " annulée avec succès");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Facture annulée");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Facture non trouvée");
            }

        } catch (Exception e) {
            System.err.println("Error cancelling facture: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erreur: " + e.getMessage());
        }
    }
}