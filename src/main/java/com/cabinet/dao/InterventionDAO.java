package com.cabinet.dao;

import com.cabinet.model.InterventionMedecin;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterventionDAO {

    public void save(InterventionMedecin intervention) {
        String sql = "INSERT INTO interventions (consultation_id, acte_id, numDent, quantite, prixUnitaire) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, intervention.getConsultationId());
            pstmt.setLong(2, intervention.getActeId());
            if (intervention.getNumDent() != null) {
                pstmt.setInt(3, intervention.getNumDent());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setInt(4, intervention.getQuantite());
            pstmt.setDouble(5, intervention.getPrixUnitaire());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving intervention: " + e.getMessage());
        }
    }

    public List<InterventionMedecin> getByConsultationId(Long consultationId) {
        List<InterventionMedecin> list = new ArrayList<>();
        String sql = "SELECT * FROM interventions WHERE consultation_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, consultationId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting interventions by consultation: " + e.getMessage());
        }
        return list;
    }

    private InterventionMedecin mapRow(ResultSet rs) throws SQLException {
        InterventionMedecin i = new InterventionMedecin();
        i.setId(rs.getLong("id"));
        i.setConsultationId(rs.getLong("consultation_id"));
        i.setActeId(rs.getLong("acte_id"));
        i.setNumDent((Integer) rs.getObject("numDent"));
        i.setQuantite(rs.getInt("quantite"));
        i.setPrixUnitaire(rs.getDouble("prixUnitaire"));
        return i;
    }
}