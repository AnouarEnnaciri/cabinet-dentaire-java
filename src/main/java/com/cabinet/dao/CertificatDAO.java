package com.cabinet.dao;

import com.cabinet.model.Certificat;
import com.cabinet.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;

public class CertificatDAO {

    public void save(Certificat certificat) {
        String sql = "INSERT INTO certificats (patient_id, patient_nom, consultation_id, type, date_emission, contenu) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, certificat.getPatientId());
            pstmt.setString(2, certificat.getPatientNom());

            if (certificat.getConsultationId() != null) {
                pstmt.setInt(3, certificat.getConsultationId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.setString(4, certificat.getType());

            // Store as ISO string (not timestamp)
            String formattedDate = certificat.getDateEmission().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            pstmt.setString(5, formattedDate);

            pstmt.setString(6, certificat.getContenu());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                certificat.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("CertificatDAO: save failed", e);
        }
    }

    public List<Certificat> getByConsultationId(long consultationId) {
        List<Certificat> list = new ArrayList<>();
        String sql = "SELECT * FROM certificats WHERE consultation_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, consultationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("CertificatDAO: fetch failed", e);
        }

        return list;
    }

    private Certificat mapRow(ResultSet rs) throws SQLException {
        Certificat c = new Certificat();

        c.setId(rs.getInt("id"));
        c.setPatientId(rs.getInt("patient_id"));
        c.setPatientNom(rs.getString("patient_nom"));

        int consultationId = rs.getInt("consultation_id");
        if (rs.wasNull()) {
            c.setConsultationId(null);
        } else {
            c.setConsultationId(consultationId);
        }

        c.setType(rs.getString("type"));

        // Handle both string and numeric timestamp formats
        String dateEmissionStr = rs.getString("date_emission");
        if (dateEmissionStr != null) {
            try {
                // Try to parse as ISO date string first
                c.setDateEmission(LocalDateTime.parse(dateEmissionStr));
            } catch (Exception e) {
                try {
                    // If that fails, treat as milliseconds timestamp
                    long timestamp = Long.parseLong(dateEmissionStr);
                    c.setDateEmission(LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(timestamp),
                            ZoneId.systemDefault()
                    ));
                } catch (Exception ex) {
                    // fallback to current time
                    c.setDateEmission(LocalDateTime.now());
                    System.err.println("Failed to parse date: " + dateEmissionStr);
                }
            }
        }

        c.setContenu(rs.getString("contenu"));

        return c;
    }
}