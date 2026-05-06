package com.cabinet.dao;

import com.cabinet.model.Prescription;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    // Save multiple prescriptions for a consultation
    public void saveAll(long consultationId, List<Prescription> prescriptions) {
        String sql = "INSERT INTO prescriptions (consultation_id, medicament_nom, posologie) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Prescription p : prescriptions) {
                pstmt.setLong(1, consultationId);
                pstmt.setString(2, p.getMedicamentNom());
                pstmt.setString(3, p.getPosologie());
                pstmt.addBatch();
            }
            pstmt.executeBatch();

        } catch (SQLException e) {
            System.err.println("Error saving prescriptions: " + e.getMessage());
        }
    }

    // Get all prescriptions for a consultation
    public List<Prescription> getByConsultationId(long consultationId) {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE consultation_id = ? ORDER BY id ASC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, consultationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Prescription p = new Prescription();
                p.setId(rs.getInt("id"));
                p.setConsultationId(rs.getInt("consultation_id"));
                p.setMedicamentNom(rs.getString("medicament_nom"));
                p.setPosologie(rs.getString("posologie"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error getting prescriptions: " + e.getMessage());
        }
        return list;
    }
    // Delete all prescriptions for a consultation (useful for updates)
    public void deleteByConsultationId(long consultationId) {
        String sql = "DELETE FROM prescriptions WHERE consultation_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, consultationId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting prescriptions: " + e.getMessage());
        }
    }
}