package com.cabinet.dao;

import com.cabinet.model.Antecedent;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AntecedentDAO {

    public List<Antecedent> getByPatientId(Long patientId) {
        List<Antecedent> list = new ArrayList<>();
        String sql = "SELECT * FROM antecedents WHERE patient_id = ? ORDER BY dateCreation DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting antecedents: " + e.getMessage());
        }
        return list;
    }

    public void save(Antecedent antecedent) {
        String sql = "INSERT INTO antecedents (patient_id, type, description, dateCreation) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, antecedent.getPatientId());
            pstmt.setString(2, antecedent.getType());
            pstmt.setString(3, antecedent.getDescription());
            pstmt.setString(4, LocalDateTime.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving antecedent: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM antecedents WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting antecedent: " + e.getMessage());
        }
    }

    private Antecedent mapRow(ResultSet rs) throws SQLException {
        Antecedent a = new Antecedent();
        a.setId(rs.getLong("id"));
        a.setPatientId(rs.getLong("patient_id"));
        a.setType(rs.getString("type"));
        a.setDescription(rs.getString("description"));
        String dateStr = rs.getString("dateCreation");
        if (dateStr != null) {
            a.setDateCreation(LocalDateTime.parse(dateStr));
        }
        return a;
    }
}