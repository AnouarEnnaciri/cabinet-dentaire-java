package com.cabinet.dao;

import com.cabinet.model.Consultation;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {

    public List<Consultation> getByDossierId(Long dossierId) {
        List<Consultation> list = new ArrayList<>();
        String sql = "SELECT * FROM consultations WHERE dossier_id = ? ORDER BY date DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dossierId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting consultations: " + e.getMessage());
        }
        return list;
    }

    public Consultation getById(Long id) {
        String sql = "SELECT * FROM consultations WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getLong("id"));
                c.setDossierId(rs.getLong("dossier_id"));
                c.setDate(LocalDate.parse(rs.getString("date")));
                c.setDiagnostic(rs.getString("diagnostic"));
                c.setTraitement(rs.getString("traitement"));
                c.setNotes(rs.getString("notes"));
                return c;
            }
        } catch (SQLException e) {
            System.err.println("Error getById: " + e.getMessage());
        }
        return null;
    }

    public Long save(Consultation consultation) {
        String sql = "INSERT INTO consultations (dossier_id, medecin_id, date, diagnostic, traitement, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, consultation.getDossierId());
            if (consultation.getMedecinId() != null) {
                pstmt.setLong(2, consultation.getMedecinId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, consultation.getDate().toString());
            pstmt.setString(4, consultation.getDiagnostic());
            pstmt.setString(5, consultation.getTraitement());
            pstmt.setString(6, consultation.getNotes());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Error saving consultation: " + e.getMessage());
        }
        return 0L;
    }

    public void updateConsultation(Long id, String diagnostic, String traitement, String notes) {
        String sql = "UPDATE consultations SET diagnostic = ?, traitement = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, diagnostic);
            pstmt.setString(2, traitement);
            pstmt.setString(3, notes);
            pstmt.setLong(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updateConsultation: " + e.getMessage());
        }
    }

    public void deleteConsultation(Long id) {
        String sql = "DELETE FROM consultations WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting consultation: " + e.getMessage());
        }
    }

    private Consultation mapRow(ResultSet rs) throws SQLException {
        Consultation c = new Consultation();
        c.setId(rs.getLong("id"));
        c.setDossierId(rs.getLong("dossier_id"));
        c.setMedecinId(rs.getLong("medecin_id"));
        c.setDate(LocalDate.parse(rs.getString("date")));
        c.setDiagnostic(rs.getString("diagnostic"));
        c.setTraitement(rs.getString("traitement"));
        c.setNotes(rs.getString("notes"));
        return c;
    }


}