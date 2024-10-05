import org.json.JSONObject;
import org.json.JSONArray;
import java.util.*;

/**
 * Beschreiben Sie hier die Klasse Registrieren.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class user
{
    private AntwortParser antwortparser;
    private NetzwerkZugriff socialbotnet;

    /**
     * Konstruktor für Objekte der Klasse Registrieren
     */
    public user()
    {
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-ajvb.onrender.com");
    }

    public void alleNutzerAnzeigen(){

        try {
            // Senden der GET-Anfrage und Speichern der Antwort als String.
            String antwort = socialbotnet.GETAnfrageSenden("/api/users");

            // Parsen der Antwort als JSON-Array.
            JSONArray nutzerArray = new JSONArray(antwort);

            // Schleife durch das Array, um alle Nutzernamen auszugeben.
            for (int i = 0; i < nutzerArray.length(); i++) {
                JSONObject nutzerObjekt = nutzerArray.getJSONObject(i);
                String nutzerName = nutzerObjekt.getString("username");
                System.out.println("Nutzername: " + nutzerName);
            }

        } catch (Exception e) {
            // Fehlerbehandlung bei Problemen mit der Anfrage oder JSON-Parsing.
            System.err.println("Fehler beim Abfragen der Benutzer: " + e.getMessage());

        }
    }

    public void allePosts()
    {
        String antwort = socialbotnet.GETAnfrageSenden("/api/posts");
    }

    public void pinnwandEinesNutzers( String username)
    {
        String antwort = socialbotnet.GETAnfrageSenden("/api/pinnwand/" + username);
    }

    public void posten(String username, String password ,String nachricht) {
        /**
         * Ergänze hier den Code, damit der Bot im Netzwerk posten kann. 
         */ 
        // POST-Anfrage mit Parametern vorbereiten
        socialbotnet.POSTAnfrageVorbereiten("username", username);
        socialbotnet.POSTAnfrageVorbereiten("password", password);
        socialbotnet.POSTAnfrageVorbereiten("message", nachricht);
        // Post senden schicken
        socialbotnet.POSTAnfrageSenden("/api/post");

    }
      
}