package com.cabinet.dao;

import com.cabinet.model.Facture;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {

    public List<Facture> getByPatientId(Long patientId) {
        List<Facture> list = new ArrayList<>();
        String sql = "SELECT * FROM factures WHERE patient_id = ? ORDER BY dateFacture DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting factures: " + e.getMessage());
        }
        return list;
    }

    public List<Facture> getAll() {
        List<Facture> list = new ArrayList<>();
        String sql = "SELECT * FROM factures ORDER BY dateFacture DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all factures: " + e.getMessage());
        }
        return list;
    }

    public void save(Facture facture) {
        String sql = "INSERT INTO factures (patient_id, consultation_id, dateFacture, montantTotal, montantPaye, statut) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, facture.getPatientId());
            pstmt.setLong(2, facture.getConsultationId());
            pstmt.setString(3, facture.getDateFacture().toString());
            pstmt.setDouble(4, facture.getMontantTotal());
            pstmt.setDouble(5, facture.getMontantPaye());
            pstmt.setString(6, facture.getStatut());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving facture: " + e.getMessage());
        }
    }

    public void updateStatut(Long id, String statut) {
        String sql = "UPDATE factures SET statut = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, statut);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating facture status: " + e.getMessage());
        }
    }

    public void updatePaiement(Long id, Double montantPaye) {
        String sql = "UPDATE factures SET montantPaye = ?, statut = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, montantPaye);
            pstmt.setString(2, montantPaye >= getMontantTotal(id) ? "PAYEE" : "PARTIELLE");
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
        }
    }

    private Double getMontantTotal(Long id) {
        String sql = "SELECT montantTotal FROM factures WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("montantTotal");
            }
        } catch (SQLException e) {
            System.err.println("Error getting montantTotal: " + e.getMessage());
        }
        return 0.0;
    }
    public List<Facture> getByConsultationId(Long consultationId) {
        List<Facture> list = new ArrayList<>();
        String sql = "SELECT * FROM factures WHERE consultation_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, consultationId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return list;
    }

    private Facture mapRow(ResultSet rs) throws SQLException {
        Facture f = new Facture();
        f.setId(rs.getLong("id"));
        f.setPatientId(rs.getLong("patient_id"));
        f.setConsultationId(rs.getLong("consultation_id"));
        String dateStr = rs.getString("dateFacture");
        if (dateStr != null) {
            f.setDateFacture(LocalDateTime.parse(dateStr));
        }
        f.setMontantTotal(rs.getDouble("montantTotal"));
        f.setMontantPaye(rs.getDouble("montantPaye"));
        f.setStatut(rs.getString("statut"));
        return f;
    }
}