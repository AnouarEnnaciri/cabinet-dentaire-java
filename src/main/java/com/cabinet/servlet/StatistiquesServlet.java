package com.cabinet.servlet;

import com.cabinet.util.DatabaseUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/statistiques")
public class StatistiquesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role)) {
            response.sendRedirect("dashboard");
            return;
        }

        // Stats cards
        request.setAttribute("totalPatients", getTotalPatients());
        request.setAttribute("totalRdvs", getTotalRdvs());
        request.setAttribute("totalRevenue", getTotalRevenue());
        request.setAttribute("pendingRdvs", getPendingRdvs());

        // Chart data - as simple Lists
        request.setAttribute("revenueLabels", getRevenueLabels());
        request.setAttribute("revenueValues", getRevenueValues());
        request.setAttribute("rdvLabels", getRdvLabels());
        request.setAttribute("rdvValues", getRdvValues());

        // Top acts
        request.setAttribute("topActs", getTopActs());

        request.getRequestDispatcher("/WEB-INF/views/statistiques.jsp").forward(request, response);
    }

    private List<String> getRevenueLabels() {
        List<String> labels = new ArrayList<>();
        String sql = "SELECT strftime('%m', dateFacture) as month FROM factures WHERE dateFacture IS NOT NULL GROUP BY month ORDER BY month";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                labels.add(getMonthName(rs.getString("month")));
            }
        } catch (Exception e) {}
        return labels;
    }

    private List<Double> getRevenueValues() {
        List<Double> values = new ArrayList<>();
        String sql = "SELECT SUM(montantTotal) as total FROM factures WHERE dateFacture IS NOT NULL GROUP BY strftime('%m', dateFacture) ORDER BY strftime('%m', dateFacture)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                values.add(rs.getDouble("total"));
            }
        } catch (Exception e) {}
        return values;
    }

    private List<String> getRdvLabels() {
        List<String> labels = new ArrayList<>();
        String sql = "SELECT strftime('%m', date) as month FROM rendez_vous WHERE date IS NOT NULL GROUP BY month ORDER BY month";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                labels.add(getMonthName(rs.getString("month")));
            }
        } catch (Exception e) {}
        return labels;
    }

    private List<Integer> getRdvValues() {
        List<Integer> values = new ArrayList<>();
        String sql = "SELECT COUNT(*) as count FROM rendez_vous WHERE date IS NOT NULL GROUP BY strftime('%m', date) ORDER BY strftime('%m', date)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                values.add(rs.getInt("count"));
            }
        } catch (Exception e) {}
        return values;
    }

    private List<Map<String, Object>> getTopActs() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT a.libelle, COUNT(i.id) as count FROM interventions i JOIN actes a ON i.acte_id = a.id GROUP BY a.id ORDER BY count DESC LIMIT 5";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("libelle", rs.getString("libelle"));
                row.put("count", rs.getInt("count"));
                list.add(row);
            }
        } catch (Exception e) {}
        return list;
    }

    private int getTotalPatients() {
        String sql = "SELECT COUNT(*) as count FROM patients";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt("count");
        } catch (Exception e) {}
        return 0;
    }

    private int getTotalRdvs() {
        String sql = "SELECT COUNT(*) as count FROM rendez_vous";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt("count");
        } catch (Exception e) {}
        return 0;
    }

    private double getTotalRevenue() {
        String sql = "SELECT SUM(montantTotal) as total FROM factures WHERE statut != 'ANNULÉE'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (Exception e) {}
        return 0;
    }

    private int getPendingRdvs() {
        String sql = "SELECT COUNT(*) as count FROM rendez_vous WHERE statut = 'EN_ATTENTE'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt("count");
        } catch (Exception e) {}
        return 0;
    }

    private String getMonthName(String month) {
        String[] months = {"Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Aoû", "Sep", "Oct", "Nov", "Déc"};
        try { return months[Integer.parseInt(month) - 1]; } catch (Exception e) { return month; }
    }
}