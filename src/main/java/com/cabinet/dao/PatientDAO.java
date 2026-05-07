package com.cabinet.dao;

import com.cabinet.model.Patient;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setPhone(rs.getString("phone"));
                p.setEmail(rs.getString("email"));
                p.setAge(rs.getInt("age"));
                patients.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error getting patients: " + e.getMessage());
        }
        return patients;
    }

    public void addPatient(Patient patient) {
        String sql = "INSERT INTO patients (name, phone, email, age) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getPhone());
            pstmt.setString(3, patient.getEmail());
            pstmt.setInt(4, patient.getAge());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
        }
    }

    public void deletePatient(Long id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
        }
    }

    public void updatePatient(Patient patient) {
        String sql = "UPDATE patients SET name = ?, phone = ?, email = ?, age = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getPhone());
            pstmt.setString(3, patient.getEmail());
            pstmt.setInt(4, patient.getAge());
            pstmt.setLong(5, patient.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
        }
    }
    public Patient findById(Long id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setPhone(rs.getString("phone"));
                p.setEmail(rs.getString("email"));
                p.setAge(rs.getInt("age"));
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Error finding patient: " + e.getMessage());
        }
        return null;
    }
    public Patient findOrCreateByNomTelephone(String nom, String telephone) {
        // Chercher si le patient existe déjà
        String sql = "SELECT * FROM patients WHERE name = ? AND phone = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, telephone);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setPhone(rs.getString("phone"));
                p.setEmail(rs.getString("email"));
                p.setAge(rs.getInt("age"));
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Error finding patient: " + e.getMessage());
        }

        // create new patient
        Patient newPatient = new Patient();
        newPatient.setName(nom);
        newPatient.setPhone(telephone);
        newPatient.setEmail(null);
        newPatient.setAge(0);

        addPatient(newPatient);

        // Récupérer le patient créé (avec son ID)
        return findOrCreateByNomTelephone(nom, telephone);
    }
}

