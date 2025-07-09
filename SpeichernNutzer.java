import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

public class SpeichernNutzer {

    private static final String DB_URL = "jdbc:postgresql://elixir-hawk-3221.8nj.gcp-europe-west1.cockroachlabs.cloud:26257/benutzer?sslmode=verify-full";
    private static final String DB_USER = "gundelwein";
    private static final String DB_PASSWORD = "NApeDKj1pCB2a-yUM608Og";
    private static final Logger LOGGER = Logger.getLogger(SpeichernNutzer.class.getName());

    // Konstruktor
    public SpeichernNutzer() {}

    // Methode, um eine Datenbankverbindung zu erstellen
   public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Methode, um den Nutzer zu speichern
    public void speichernNutzer(String username, String passwort) {
        String sql = "INSERT INTO benutzer (username, passwort) VALUES (?, ?)";

        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwort);

            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Benutzer erfolgreich gespeichert!");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Speichern des Nutzers: {0}", e.getMessage());
        }
    }

    // Methode, um den Nutzer mit gehashtem Passwort zu speichern
    public void speichernNutzerHash(String username, String passwort) {
        String gehashtesPasswort = hashPasswort(passwort);

        if (gehashtesPasswort == null) {
            LOGGER.log(Level.SEVERE, "Fehler beim Hashen des Passworts.");
            return;
        }

        speichernNutzer(username, gehashtesPasswort);
    }

    // Methode zum Hashen des Passworts mit SHA-256
    private String hashPasswort(String passwort) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(passwort.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Hashen des Passworts: {0}", e.getMessage());
            return null;
        }
    }

    // Methode zur Passwortverifizierung
    public boolean verifizierePasswort(String eingegebenesPasswort, String gespeichertesHashPasswort) {
        String gehashtesEingegebenesPasswort = hashPasswort(eingegebenesPasswort);

        if (gehashtesEingegebenesPasswort == null) {
            LOGGER.log(Level.SEVERE, "Fehler beim Hashen des eingegebenen Passworts.");
            return false;
        }

        // Timing-sicherer Vergleich der gehashten Passwörter
        boolean istKorrekt = MessageDigest.isEqual(
            gehashtesEingegebenesPasswort.getBytes(), 
            gespeichertesHashPasswort.getBytes()
        );

        if (istKorrekt) {
            LOGGER.log(Level.INFO, "Passwort korrekt!");
        } else {
            LOGGER.log(Level.WARNING, "Passwort inkorrekt!");
        }

        return istKorrekt;
    }

    // Methode: Abrufen von Nutzernamen und Passwörtern
    public List<String[]> getNutzernamenUndPasswoerter() {
        String sql = "SELECT username, passwort FROM benutzer";
        List<String[]> nutzerListe = new ArrayList<>();

        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Durch das ResultSet iterieren und Nutzernamen und Passwörter in eine Liste speichern
            while (rs.next()) {
                String username = rs.getString("username");
                String passwort = rs.getString("passwort");
                nutzerListe.add(new String[] { username, passwort });
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Abrufen der Nutzernamen und Passwörter: {0}", e.getMessage());
        }
        
        return nutzerListe;
    }
}
