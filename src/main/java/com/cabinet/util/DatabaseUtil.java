package com.cabinet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:sqlite:clinic.db";

    public static Connection getConnection() throws SQLException {
        try {
            // Force load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC Driver not found! Please check dependencies.", e);
        }
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            createTables(stmt);

        } catch (SQLException e) {
            System.err.println("Database init error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables(Statement stmt) throws SQLException {

        String userTable = "CREATE TABLE IF NOT EXISTS utilisateurs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "login TEXT UNIQUE NOT NULL, " +
                "motDePasse TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "nom TEXT, " +
                "email TEXT" +
                ")";
        stmt.execute(userTable);

        // USERS (hashed passwords, same credentials preserved)

        String adminHash = hashPassword("admin123");
        stmt.execute("INSERT OR IGNORE INTO utilisateurs (login, motDePasse, role, nom, email) " +
                "VALUES ('admin', '" + adminHash + "', 'admin', 'Administrateur', 'admin@cabinet.com')");

        String doctorHash = hashPassword("doctor123");
        stmt.execute("INSERT OR IGNORE INTO utilisateurs (login, motDePasse, role, nom, email) " +
                "VALUES ('doctor', '" + doctorHash + "', 'medecin', 'Dr. Karim', 'karim@cabinet.com')");

        String secretaireHash = hashPassword("secret123");
        stmt.execute("INSERT OR IGNORE INTO utilisateurs (login, motDePasse, role, nom, email) " +
                "VALUES ('secretaire', '" + secretaireHash + "', 'secretaire', 'Fatima Zahra', 'fatima@cabinet.com')");

        // Patients table
        stmt.execute("CREATE TABLE IF NOT EXISTS patients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "phone TEXT, " +
                "email TEXT, " +
                "age INTEGER, " +
                "dateCreation TEXT, " +
                "dateModification TEXT, " +
                "creePar TEXT, " +
                "modifiePar TEXT" +
                ")");

        // RDV-vous table
        stmt.execute("CREATE TABLE IF NOT EXISTS rendez_vous (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, " +
                "patient_nom TEXT, " +
                "patient_telephone TEXT, " +
                "medecin_id INTEGER, " +
                "date TEXT NOT NULL, " +
                "heure TEXT, " +
                "motif TEXT, " +
                "statut TEXT DEFAULT 'EN_ATTENTE', " +
                "date_demande TEXT, " +
                "FOREIGN KEY (patient_id) REFERENCES patients(id), " +
                "FOREIGN KEY (medecin_id) REFERENCES utilisateurs(id)" +
                ")");

        // Dossiers médicaux
        stmt.execute("CREATE TABLE IF NOT EXISTS dossiers_medicaux (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER NOT NULL, " +
                "dateCreation TEXT NOT NULL, " +
                "FOREIGN KEY (patient_id) REFERENCES patients(id)" +
                ")");

        // Consultations
        stmt.execute("CREATE TABLE IF NOT EXISTS consultations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dossier_id INTEGER NOT NULL, " +
                "medecin_id INTEGER, " +
                "date TEXT NOT NULL, " +
                "diagnostic TEXT, " +
                "traitement TEXT, " +
                "notes TEXT, " +
                "FOREIGN KEY (dossier_id) REFERENCES dossiers_medicaux(id), " +
                "FOREIGN KEY (medecin_id) REFERENCES utilisateurs(id)" +
                ")");

        // Catalogue  actes
        String actesTable = "CREATE TABLE IF NOT EXISTS actes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code TEXT UNIQUE NOT NULL, " +
                "libelle TEXT NOT NULL, " +
                "categorie TEXT, " +
                "prixBase DOUBLE NOT NULL" +
                ")";
        stmt.execute(actesTable);

        // Antécédents médicaux
        String antecedentsTable = "CREATE TABLE IF NOT EXISTS antecedents (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER NOT NULL, " +
                "type TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "dateCreation TEXT NOT NULL, " +
                "FOREIGN KEY (patient_id) REFERENCES patients(id)" +
                ")";
        stmt.execute(antecedentsTable);

// Actes par défaut
        stmt.execute("INSERT OR IGNORE INTO actes (code, libelle, categorie, prixBase) VALUES ('DET01', 'Détartrage', 'Soins', 300)");
        stmt.execute("INSERT OR IGNORE INTO actes (code, libelle, categorie, prixBase) VALUES ('BLN01', 'Blanchiment', 'Esthétique', 1500)");
        stmt.execute("INSERT OR IGNORE INTO actes (code, libelle, categorie, prixBase) VALUES ('IMP01', 'Implant', 'Chirurgie', 5000)");
        stmt.execute("INSERT OR IGNORE INTO actes (code, libelle, categorie, prixBase) VALUES ('EXT01', 'Extraction', 'Chirurgie', 400)");

// Interventions (liaison Consultation → Acte)
        String interventionsTable = "CREATE TABLE IF NOT EXISTS interventions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "consultation_id INTEGER NOT NULL, " +
                "acte_id INTEGER NOT NULL, " +
                "numDent INTEGER, " +
                "quantite INTEGER DEFAULT 1, " +
                "prixUnitaire DOUBLE NOT NULL, " +
                "FOREIGN KEY (consultation_id) REFERENCES consultations(id), " +
                "FOREIGN KEY (acte_id) REFERENCES actes(id)" +
                ")";
        stmt.execute(interventionsTable);

        // Factures
        String facturesTable = "CREATE TABLE IF NOT EXISTS factures (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER NOT NULL, " +
                "consultation_id INTEGER, " +
                "dateFacture TEXT NOT NULL, " +
                "montantTotal DOUBLE NOT NULL, " +
                "montantPaye DOUBLE DEFAULT 0, " +
                "statut TEXT DEFAULT 'EN_ATTENTE', " +
                "FOREIGN KEY (patient_id) REFERENCES patients(id), " +
                "FOREIGN KEY (consultation_id) REFERENCES consultations(id)" +
                ")";
        stmt.execute(facturesTable);

        // Catalogue des médicaments
        String medicamentsTable = "CREATE TABLE IF NOT EXISTS medicaments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT NOT NULL UNIQUE" +
                ")";
        stmt.execute(medicamentsTable);

// Prescriptions / Ordonnances
        String prescriptionsTable = "CREATE TABLE IF NOT EXISTS prescriptions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "consultation_id INTEGER NOT NULL, " +
                "medicament_nom TEXT NOT NULL, " +
                "posologie TEXT NOT NULL, " +
                "FOREIGN KEY (consultation_id) REFERENCES consultations(id) ON DELETE CASCADE" +
                ")";
        stmt.execute(prescriptionsTable);

        // Certificats médicaux
        String certificatsTable = "CREATE TABLE IF NOT EXISTS certificats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER NOT NULL, " +
                "patient_nom TEXT NOT NULL, " +
                "consultation_id INTEGER, " +
                "type TEXT NOT NULL, " +
                "date_emission TEXT NOT NULL, " +
                "contenu TEXT, " +
                "FOREIGN KEY (patient_id) REFERENCES patients(id), " +
                "FOREIGN KEY (consultation_id) REFERENCES consultations(id)" +
                ")";
        stmt.execute(certificatsTable);

// Insert sample medicaments
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Amoxicilline 500mg')");
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Amoxicilline 1g')");
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Ibuprofène 400mg')");
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Ibuprofène 600mg')");
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Paracétamol 500mg')");
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Paracétamol 1g')");
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Clindamycine 300mg')");
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Chlorhexidine 0.12%')");
        stmt.execute("INSERT OR IGNORE INTO medicaments (nom) VALUES ('Metronidazole 250mg')");

        System.out.println("Database tables ready");
    }



    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }
}