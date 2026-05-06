package com.cabinet.dao;

import com.cabinet.model.Acte;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActeDAO {

    public List<Acte> getAllActes() {
        List<Acte> list = new ArrayList<>();
        String sql = "SELECT * FROM actes ORDER BY libelle";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting actes: " + e.getMessage());
        }
        return list;
    }

    public Acte findById(Long id) {
        String sql = "SELECT * FROM actes WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding acte: " + e.getMessage());
        }
        return null;
    }

    private Acte mapRow(ResultSet rs) throws SQLException {
        Acte acte = new Acte();
        acte.setId(rs.getLong("id"));
        acte.setCode(rs.getString("code"));
        acte.setLibelle(rs.getString("libelle"));
        acte.setCategorie(rs.getString("categorie"));
        acte.setPrixBase(rs.getDouble("prixBase"));
        return acte;
    }
}