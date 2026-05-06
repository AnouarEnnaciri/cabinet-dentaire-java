package com.cabinet.dao;

import com.cabinet.model.Medicament;
import com.cabinet.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO {

    public List<Medicament> getAll() {
        List<Medicament> list = new ArrayList<>();
        String sql = "SELECT * FROM medicaments ORDER BY nom ASC";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicament m = new Medicament();
                m.setId(rs.getInt("id"));
                m.setNom(rs.getString("nom"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error getting medicaments: " + e.getMessage());
        }
        return list;
    }

    public Medicament findById(int id) {
        String sql = "SELECT * FROM medicaments WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Medicament m = new Medicament();
                m.setId(rs.getInt("id"));
                m.setNom(rs.getString("nom"));
                return m;
            }
        } catch (SQLException e) {
            System.err.println("Error finding medicament: " + e.getMessage());
        }
        return null;
    }
}