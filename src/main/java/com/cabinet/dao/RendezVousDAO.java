package com.cabinet.dao;

import com.cabinet.model.RendezVous;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {

    public List<RendezVous> getAll() {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous ORDER BY date DESC, heure ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting rendez-vous: " + e.getMessage());
        }
        return list;
    }

    public List<RendezVous> getByMedecinId(Long medecinId) {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE medecin_id = ? ORDER BY date DESC, heure ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, medecinId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting rendez-vous by doctor: " + e.getMessage());
        }
        return list;
    }

    public void save(RendezVous rdv) {
        String sql = "INSERT INTO rendez_vous (patient_id, patient_nom, patient_telephone, medecin_id, date, heure, motif, statut, date_demande) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, rdv.getPatientId());
            pstmt.setString(2, rdv.getPatientNom());
            pstmt.setString(3, rdv.getPatientTelephone());
            pstmt.setLong(4, rdv.getMedecinId());
            pstmt.setString(5, rdv.getDate().toString());
            pstmt.setString(6, rdv.getHeure() != null ? rdv.getHeure().toString() : null);
            pstmt.setString(7, rdv.getMotif());
            pstmt.setString(8, rdv.getStatut());
            pstmt.setString(9, rdv.getDateDemande() != null ? rdv.getDateDemande().toString() : LocalDateTime.now().toString());
            pstmt.executeUpdate();
            System.out.println("RDV saved - Patient: " + rdv.getPatientNom() + ", Doctor: " + rdv.getMedecinId());
        } catch (SQLException e) {
            System.err.println("Error saving rendez-vous: " + e.getMessage());
        }
    }
    public void delete(Long id) {
        String sql = "DELETE FROM rendez_vous WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting rendez-vous: " + e.getMessage());
        }
    }

    private RendezVous mapRow(ResultSet rs) throws SQLException {
        RendezVous rdv = new RendezVous();
        rdv.setId(rs.getLong("id"));

        long patientId = rs.getLong("patient_id");
        if (!rs.wasNull() && patientId > 0) {
            rdv.setPatientId(patientId);
        }

        long medecinId = rs.getLong("medecin_id");
        if (!rs.wasNull() && medecinId > 0) {
            rdv.setMedecinId(medecinId);
        }

        String dateStr = rs.getString("date");
        if (dateStr != null) {
            rdv.setDate(LocalDate.parse(dateStr));
        }

        String heureStr = rs.getString("heure");
        if (heureStr != null) {
            rdv.setHeure(LocalTime.parse(heureStr));
        }

        rdv.setMotif(rs.getString("motif"));
        rdv.setStatut(rs.getString("statut"));
        rdv.setPatientNom(rs.getString("patient_nom"));
        rdv.setPatientTelephone(rs.getString("patient_telephone"));

        String dateDemandeStr = rs.getString("date_demande");
        if (dateDemandeStr != null) {
            rdv.setDateDemande(LocalDateTime.parse(dateDemandeStr));
        }

        return rdv;
    }

    public void savePublicRdv(RendezVous rdv) {
        String sql = "INSERT INTO rendez_vous (patient_id, patient_nom, patient_telephone, date, motif, statut, date_demande) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, rdv.getPatientId());
            pstmt.setString(2, rdv.getPatientNom());
            pstmt.setString(3, rdv.getPatientTelephone());
            pstmt.setString(4, rdv.getDate().toString());
            pstmt.setString(5, rdv.getMotif());
            pstmt.setString(6, rdv.getStatut());
            pstmt.setString(7, rdv.getDateDemande().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving public RDV: " + e.getMessage());
        }
    }

    public List<RendezVous> getPendingRdvs() {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT id, patient_nom, patient_telephone, date, motif, statut, date_demande FROM rendez_vous WHERE statut = 'EN_ATTENTE' ORDER BY date_demande ASC";
        try (Statement stmt = DatabaseUtil.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                RendezVous rdv = new RendezVous();
                rdv.setId(rs.getLong("id"));
                rdv.setPatientNom(rs.getString("patient_nom"));
                rdv.setPatientTelephone(rs.getString("patient_telephone"));
                rdv.setDate(LocalDate.parse(rs.getString("date")));
                rdv.setMotif(rs.getString("motif"));
                rdv.setStatut(rs.getString("statut"));
                String dateDemandeStr = rs.getString("date_demande");
                if (dateDemandeStr != null) {
                    rdv.setDateDemande(LocalDateTime.parse(dateDemandeStr));
                }
                list.add(rdv);
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending RDVs: " + e.getMessage());
        }
        return list;
    }

    public void updateStatut(Long id, String statut) {
        String sql = "UPDATE rendez_vous SET statut = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, statut);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating RDV status: " + e.getMessage());
        }
    }

    public void confirmRdv(Long id, Long medecinId, LocalDate date, LocalTime heure) {
        String sql = "UPDATE rendez_vous SET medecin_id = ?, date = ?, heure = ?, statut = 'CONFIRME' WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, medecinId);
            pstmt.setString(2, date.toString());
            pstmt.setString(3, heure.toString());
            pstmt.setLong(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error confirming RDV: " + e.getMessage());
        }
    }
    public RendezVous getById(Long id) {
        String sql = "SELECT * FROM rendez_vous WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getById: " + e.getMessage());
        }
        return null;
    }
    public List<RendezVous> getByStatut(String statut) {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE statut = ? ORDER BY date_demande DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, statut);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting RDV by statut: " + e.getMessage());
        }
        return list;
    }


}