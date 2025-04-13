import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bots {
    private static final String DB_URL = "jdbc:postgresql://elixir-hawk-3221.8nj.gcp-europe-west1.cockroachlabs.cloud:26257/benutzerdatenbank?sslmode=verify-full";
    private static final String DB_USER = "gundelwein";
    private static final String DB_PASSWORD ="Gm73bJtiWLVBoXsry_weJw"; // Passwort über Umgebungsvariable laden
    private static final Logger LOGGER = Logger.getLogger(Bots.class.getName());
    

    // Konstanten für die URLs
    private static final String ZITAT_SERVICE_URL = "https://api.zitat-service.de";
    private static final String RANDOM_USER_URL = "https://randomuser.me/api/";

    private final NetzwerkZugriff socialbotnet;
    private final NetzwerkZugriff socialbotnetName;

    public Bots() {
        if (DB_PASSWORD == null) {
            LOGGER.log(Level.SEVERE, "Datenbankpasswort ist nicht gesetzt. Bitte setze die Umgebungsvariable 'DB_PASSWORD'.");
        }
        this.socialbotnet = new NetzwerkZugriff(ZITAT_SERVICE_URL);
        this.socialbotnetName = new NetzwerkZugriff(RANDOM_USER_URL);
    }

    // Methode zur Generierung eines neuen Passworts
    public String neuesPasswortErzeugen() {
        try {
            String antwort = socialbotnet.GETAnfrageSenden("/v1/quote?language=de");
            JSONObject jsonAntwort = new JSONObject(antwort);

            String spruch = jsonAntwort.getString("quote");
            String passcode = generierePasswortAusSatz(spruch);

            LOGGER.info("Neuer Spruch: " + spruch);
            LOGGER.info("Passwort aus den ersten 8 Zeichen des Spruchs generiert: " + passcode);

            return passcode;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Erzeugen eines neuen Passworts", e);
            return "Fehler beim Erzeugen des Passworts!";
        }
    }

    // Methode zur Erzeugung eines neuen Nutzernamens
    public String neuenNutzerNamenErzeugen() {
        try {
            String nutzer = socialbotnetName.GETAnfrageSenden("");
            JSONObject jsonAntwort = new JSONObject(nutzer);
            JSONArray results = jsonAntwort.getJSONArray("results");
            JSONObject nameObject = results.getJSONObject(0).getJSONObject("login");

            String username = nameObject.getString("username");

            LOGGER.info("Neuer Nutzername: " + username);
            return username;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Erzeugen eines neuen Nutzernamens", e);
            return "Fehler beim Erzeugen des Nutzernamens!";
        }
    }

    // Methode zur Passwortgenerierung aus einem Satz
    public String generierePasswortAusSatz(String satz) {
        String[] woerter = satz.split("(?=[.,!?;:])|\\s+");
        StringBuilder passwort = new StringBuilder();

        for (String wort : woerter) {
            if (!wort.matches("[.,!?;:]") && !wort.isEmpty() && passwort.length() < 8) {
                passwort.append(wort.charAt(0));
            }
            if (passwort.length() >= 8) {
                break;
            }
        }

        // Auffüllen, falls das Passwort weniger als 8 Zeichen hat
        while (passwort.length() < 8) {
            passwort.append('x');
        }

        return passwort.toString();
    }

    // Gemeinsame Methode für HTTP-GET-Anfragen mit Fehlerbehandlung
    private String httpGetAnfrage(String urlStr) throws Exception {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }

                return content.toString();
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
