import org.json.JSONObject;
import org.json.JSONArray;
import java.util.*;

/**
 * Beschreiben Sie hier die Klasse Registrieren.
 * 
 * @author J.Gundelwein 
 * @version 1.0
 */
public class Nutzer {
    private AntwortParser antwortparser;
    private NetzwerkZugriff socialbotnet;

    /**
     * Konstruktor für Objekte der Klasse user.
     */
    public Nutzer() {
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-i635.onrender.com");
    }

    /**
     * Registriert einen neuen Nutzer.
     * 
     * @param username  Der Benutzername des neuen Nutzers.
     * @param passwort  Das Passwort des neuen Nutzers.
     */
    public void nutzerAnmelden(String username, String passwort) {
        try {
            // POST-Anfrage mit Parametern vorbereiten.
            socialbotnet.POSTAnfrageVorbereiten("username", username);
            socialbotnet.POSTAnfrageVorbereiten("password", passwort);
            socialbotnet.POSTAnfrageVorbereiten("password2", passwort);

            // Anmeldung senden.
            socialbotnet.POSTAnfrageSenden("/registrieren");
            System.out.println("Der Nutzer " + username + " wurde erfolgreich registriert.");

        } catch (Exception e) {
            // Fehlerbehandlung bei Problemen mit der Anmeldung.
            System.err.println("Fehler bei der Registrierung des Nutzers: " + e.getMessage());
        }
    }

    /**
     * Zeigt alle registrierten Nutzer an.
     */
    public void alleNutzerAnzeigen() {
        try {
            // Senden der GET-Anfrage und Speichern der Antwort als String.
            String antwort = socialbotnet.GETAnfrageSenden("/api/users");

            // Parsen der Antwort als JSON-Array.
            JSONArray nutzerArray = new JSONArray(antwort);

            // Schleife durch das Array, um alle Nutzernamen auszugeben.
            for (int i = 0; i < nutzerArray.length(); i++) {
                JSONObject nutzerObjekt = nutzerArray.getJSONObject(i);
                String nutzerName = nutzerObjekt.getString("username");
                System.out.println((i + 1) + ". Nutzername: " + nutzerName);
            }

        } catch (Exception e) {
            // Fehlerbehandlung bei Problemen mit der Anfrage oder dem JSON-Parsing.
            System.err.println("Fehler beim Abfragen der Benutzer: " + e.getMessage());
        }
    }

    /**
     * Zeigt alle Posts an.
     */
    public void allePosts() {
        try {
            // Senden der GET-Anfrage.
            String antwort = socialbotnet.GETAnfrageSenden("/api/posts");
            // Optional: Weiterverarbeitung der Antwort, wenn erforderlich.
        } catch (Exception e) {
            // Fehlerbehandlung bei Problemen mit der Anfrage.
            System.err.println("Fehler beim Abfragen der Posts: " + e.getMessage());
        }
    }

    /**
     * Zeigt die Pinnwand eines bestimmten Nutzers an.
     * 
     * @param username Der Benutzername des Nutzers, dessen Pinnwand angezeigt werden soll.
     */


    
    public String pinnwandEinesNutzers(String username) {
        try {
            // Senden der GET-Anfrage.
            String antwort = socialbotnet.GETAnfrageSenden("/api/pinnwand/" + username);
            // Optional: Weiterverarbeitung der Antwort, wenn erforderlich.
            return antwort;
        } catch (Exception e) {
        System.err.println("Fehler beim Abrufen der Pinnwand: " + e.getMessage());
        return null;
        }
    }

    /**
     * Postet eine Nachricht im Namen eines Nutzers.
     * 
     * @param username  Der Benutzername des Nutzers, der die Nachricht postet.
     * @param password  Das Passwort des Nutzers.
     * @param nachricht Die zu postende Nachricht.
     */
    public void posten(String username, String password, String nachricht) {
        try {
            // POST-Anfrage mit Parametern vorbereiten.
            socialbotnet.POSTAnfrageVorbereiten("username", username);
            socialbotnet.POSTAnfrageVorbereiten("password", password);
            socialbotnet.POSTAnfrageVorbereiten("message", nachricht);

            // Post senden.
            socialbotnet.POSTAnfrageSenden("/api/post");

        } catch (Exception e) {
            // Fehlerbehandlung bei Problemen mit dem Posten.
            System.err.println("Fehler beim Posten durch den Nutzer " + username + ": " + e.getMessage());
        }
    }
}
