package com.cabinet.dao;

import com.cabinet.model.DossierMedical;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDateTime;

public class DossierMedicalDAO {

    public DossierMedical getOrCreateByPatientId(Long patientId) {
        // Vérifier si le dossier existe
        String sql = "SELECT * FROM dossiers_medicaux WHERE patient_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                DossierMedical d = new DossierMedical();
                d.setId(rs.getLong("id"));
                d.setPatientId(rs.getLong("patient_id"));
                String dateStr = rs.getString("dateCreation");
                if (dateStr != null) {
                    d.setDateCreation(LocalDateTime.parse(dateStr));
                }
                return d;
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche dossier: " + e.getMessage());
        }

        // new folder
        String insertSql = "INSERT INTO dossiers_medicaux (patient_id, dateCreation) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, patientId);
            pstmt.setString(2, LocalDateTime.now().toString());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long newId = generatedKeys.getLong(1);
                    DossierMedical d = new DossierMedical();
                    d.setId(newId);
                    d.setPatientId(patientId);
                    d.setDateCreation(LocalDateTime.now());
                    return d;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur création dossier: " + e.getMessage());
        }
        return null;
    }
}