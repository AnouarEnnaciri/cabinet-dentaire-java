package com.cabinet.servlet;

import com.cabinet.dao.FactureDAO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/paiement")
public class PaiementServlet extends HttpServlet {
    private final FactureDAO factureDAO = new FactureDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Long factureId = Long.parseLong(request.getParameter("factureId"));
        Double montantPaye = Double.parseDouble(request.getParameter("montantPaye"));

        factureDAO.updatePaiement(factureId, montantPaye);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}