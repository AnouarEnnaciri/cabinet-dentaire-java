package com.cabinet.servlet;

import com.cabinet.util.DatabaseUtil;
import com.cabinet.model.Utilisateur;
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

@WebServlet("/statistiques-medecin")
public class StatistiquesMedecinServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        Utilisateur user = (Utilisateur) session.getAttribute("user");

        if (!"medecin".equals(role) && !"admin".equals(role)) {
            response.sendRedirect("dashboard");
            return;
        }

        Long medecinId = (long) user.getId();

        request.setAttribute("totalConsultations", getTotalConsultations(medecinId));
        request.setAttribute("totalRevenue", getTotalRevenue(medecinId));
        request.setAttribute("totalPatients", getTotalPatients(medecinId));
        request.setAttribute("consultationsLabels", getConsultationsLabels(medecinId));
        request.setAttribute("consultationsValues", getConsultationsValues(medecinId));
        request.setAttribute("revenueLabels", getRevenueLabels(medecinId));
        request.setAttribute("revenueValues", getRevenueValues(medecinId));
        request.setAttribute("topActes", getTopActes(medecinId));
        request.setAttribute("medecinNom", user.getNom());

        request.getRequestDispatcher("/WEB-INF/views/statistiques-medecin.jsp")
                .forward(request, response);
    }

    private int getTotalConsultations(Long medecinId) {
        String sql = "SELECT COUNT(*) FROM consultations WHERE medecin_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private double getTotalRevenue(Long medecinId) {
        String sql = "SELECT SUM(i.prixUnitaire * i.quantite) " +
                "FROM interventions i " +
                "JOIN consultations c ON i.consultation_id = c.id " +
                "WHERE c.medecin_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private int getTotalPatients(Long medecinId) {
        String sql = "SELECT COUNT(DISTINCT dm.patient_id) " +
                "FROM consultations c " +
                "JOIN dossiers_medicaux dm ON c.dossier_id = dm.id " +
                "WHERE c.medecin_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private List<String> getConsultationsLabels(Long medecinId) {
        List<String> labels = new ArrayList<>();
        String sql = "SELECT strftime('%m', date) as month FROM consultations " +
                "WHERE medecin_id = ? GROUP BY month ORDER BY month";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) labels.add(getMonthName(rs.getString("month")));
        } catch (Exception e) { e.printStackTrace(); }
        return labels;
    }

    private List<Integer> getConsultationsValues(Long medecinId) {
        List<Integer> values = new ArrayList<>();
        String sql = "SELECT COUNT(*) as count FROM consultations " +
                "WHERE medecin_id = ? GROUP BY strftime('%m', date) ORDER BY strftime('%m', date)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) values.add(rs.getInt("count"));
        } catch (Exception e) { e.printStackTrace(); }
        return values;
    }

    private List<String> getRevenueLabels(Long medecinId) {
        List<String> labels = new ArrayList<>();
        String sql = "SELECT strftime('%m', c.date) as month " +
                "FROM interventions i JOIN consultations c ON i.consultation_id = c.id " +
                "WHERE c.medecin_id = ? GROUP BY month ORDER BY month";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) labels.add(getMonthName(rs.getString("month")));
        } catch (Exception e) { e.printStackTrace(); }
        return labels;
    }

    private List<Double> getRevenueValues(Long medecinId) {
        List<Double> values = new ArrayList<>();
        String sql = "SELECT SUM(i.prixUnitaire * i.quantite) as total " +
                "FROM interventions i JOIN consultations c ON i.consultation_id = c.id " +
                "WHERE c.medecin_id = ? GROUP BY strftime('%m', c.date) ORDER BY strftime('%m', c.date)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) values.add(rs.getDouble("total"));
        } catch (Exception e) { e.printStackTrace(); }
        return values;
    }

    private List<Map<String, Object>> getTopActes(Long medecinId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT a.libelle, COUNT(i.id) as count " +
                "FROM interventions i " +
                "JOIN actes a ON i.acte_id = a.id " +
                "JOIN consultations c ON i.consultation_id = c.id " +
                "WHERE c.medecin_id = ? " +
                "GROUP BY a.id ORDER BY count DESC LIMIT 5";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("libelle", rs.getString("libelle"));
                row.put("count", rs.getInt("count"));
                list.add(row);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private String getMonthName(String month) {
        String[] months = {"Jan", "Fév", "Mar", "Avr", "Mai", "Juin",
                "Juil", "Aoû", "Sep", "Oct", "Nov", "Déc"};
        try { return months[Integer.parseInt(month) - 1]; }
        catch (Exception e) { return month; }
    }
}