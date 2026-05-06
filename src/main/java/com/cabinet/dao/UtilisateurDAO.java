package com.cabinet.dao;

import com.cabinet.model.Utilisateur;
import com.cabinet.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public Utilisateur findByLogin(String login, String password) {
        String hashedPassword = DatabaseUtil.hashPassword(password);
        String sql = "SELECT * FROM utilisateurs WHERE login = ? AND motDePasse = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }

        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }

        return null;
    }

    public List<Utilisateur> findByRole(String role) {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE role = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapUser(rs));
            }

        } catch (SQLException e) {
            System.err.println("findByRole error: " + e.getMessage());
        }

        return list;
    }

    public Utilisateur findById(Long id) {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }

        } catch (SQLException e) {
            System.err.println("findById error: " + e.getMessage());
        }

        return null;
    }

    public void addUser(String login, String password, String role, String nom, String email) {
        String hashedPassword = DatabaseUtil.hashPassword(password);
        String sql = "INSERT INTO utilisateurs (login, motDePasse, role, nom, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, role);
            stmt.setString(4, nom);
            stmt.setString(5, email);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("addUser error: " + e.getMessage());
        }
    }

    public void deleteUser(Long id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("deleteUser error: " + e.getMessage());
        }
    }

    public void updateUser(Long id, String login, String nom, String email, String role) {
        String sql = "UPDATE utilisateurs SET login = ?, nom = ?, email = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, nom);
            stmt.setString(3, email);
            stmt.setString(4, role);
            stmt.setLong(5, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("updateUser error: " + e.getMessage());
        }
    }

    // central mapping = cleaner + reusable
    private Utilisateur mapUser(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getLong("id"));
        u.setLogin(rs.getString("login"));
        u.setMotDePasse(rs.getString("motDePasse"));
        u.setRole(rs.getString("role"));
        u.setNom(rs.getString("nom"));
        u.setEmail(rs.getString("email"));
        return u;
    }

    public void updateUserWithPassword(Long id, String login, String nom, String email, String role, String newPassword) {
        String sql;
        if (newPassword != null && !newPassword.isEmpty()) {
            String hashedPassword = DatabaseUtil.hashPassword(newPassword);
            sql = "UPDATE utilisateurs SET login = ?, nom = ?, email = ?, role = ?, motDePasse = ? WHERE id = ?";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, nom);
                stmt.setString(3, email);
                stmt.setString(4, role);
                stmt.setString(5, hashedPassword);
                stmt.setLong(6, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("updateUserWithPassword error: " + e.getMessage());
            }
        } else {
            sql = "UPDATE utilisateurs SET login = ?, nom = ?, email = ?, role = ? WHERE id = ?";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, nom);
                stmt.setString(3, email);
                stmt.setString(4, role);
                stmt.setLong(5, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("updateUser error: " + e.getMessage());
            }
        }
    }
}