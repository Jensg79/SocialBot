import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetzwerkZugriff {

    private static final Logger LOGGER = Logger.getLogger(NetzwerkZugriff.class.getName());
    private String domain;
    private List<String> parameter = new ArrayList<>();

    public NetzwerkZugriff(String domainName) {
        System.setProperty("java.net.useSystemProxies", "true");
        this.domain = domainName;
    }

    /**
     * Setzt Daten, die bei der nächsten POST-Anfrage gesendet werden sollen.
     * @param schluessel Der Parametername, der gesetzt werden soll
     * @param daten Die Parameterdaten, die später gesendet werden sollen
     */
    public void POSTAnfrageVorbereiten(String schluessel, Object daten) {
        try {
            parameter.add(URLEncoder.encode(schluessel, "UTF-8") + "=" + URLEncoder.encode(daten.toString(), "UTF-8"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Fehler beim Vorbereiten des Parameters: {0}", e.getMessage());
        }
    }

    /**
     * Sendet die POST-Anfrage an die angegebene Adresse an den verbundenen Server.
     * Die Daten müssen vorher mit POSTAnfrageVorbereiten gesetzt werden.
     * @param url Die relative Adresse auf dem Server, an die die Anfrage geschickt werden soll
     */
    public void POSTAnfrageSenden(String url) {
        String urlParameter = String.join("&", parameter);
        try {
            URL urlObj = new URL(domain + url);
            HttpURLConnection verbindung = (HttpURLConnection) urlObj.openConnection();
            verbindung.setRequestMethod("POST");
            verbindung.setDoOutput(true);
            
            try (OutputStream os = verbindung.getOutputStream()) {
                os.write(urlParameter.getBytes());
                os.flush();
            }

            LOGGER.log(Level.INFO, "Sende 'POST'-Anfrage an URL: {0}", url);
            LOGGER.log(Level.INFO, "POST Parameter: {0}", urlParameter);

            antwortLesen(verbindung);
            parameter.clear();  // Parameterliste leeren nach erfolgreicher Anfrage

        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Fehlerhafte URL: {0}", domain + url);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Verbindungsfehler: {0}", e.getMessage());
        }
    }

    /**
     * Sendet die GET-Anfrage an die angegebene Adresse an den verbundenen Server.
     * @param url Die relative Adresse auf dem Server, an die die Anfrage geschickt werden soll
     */
    public String GETAnfrageSenden(String url) {
        String antwort = "";
        try {
            URL urlObj = new URL(domain + url);
            HttpURLConnection verbindung = (HttpURLConnection) urlObj.openConnection();
            verbindung.setRequestMethod("GET");

            LOGGER.log(Level.INFO, "Sende 'GET'-Anfrage an URL: {0}", url);
            antwort = antwortLesen(verbindung);

        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Fehlerhafte URL: {0}", domain + url);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Verbindungsfehler: {0}", e.getMessage());
        }

        return antwort;
    }

    /**
     * Liest die Antwort von der Verbindung und gibt sie als String zurück.
     * @param verbindung Die HTTP-Verbindung, von der die Antwort gelesen wird
     * @return Die Antwort als String
     */
    private String antwortLesen(HttpURLConnection verbindung) {
        StringBuilder antwortBuffer = new StringBuilder();
        try {
            int responseCode = verbindung.getResponseCode();
            LOGGER.log(Level.INFO, "Die Antwort hat den Status: {0} {1}", new Object[]{responseCode, verbindung.getResponseMessage()});

            try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    (responseCode == 200) ? verbindung.getInputStream() : verbindung.getErrorStream()))) {

                String inputZeile;
                while ((inputZeile = in.readLine()) != null) {
                    antwortBuffer.append(inputZeile.trim());
                }
            }

            String antwort = antwortBuffer.toString();
            LOGGER.log(Level.INFO, "Antwort: {0}", antwort);
            return antwort;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Lesen der Antwort: {0}", e.getMessage());
        }
        return null;
    }
}