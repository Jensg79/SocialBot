import org.json.JSONObject;
import org.json.JSONArray;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import org.json.JSONException;

public class SocialBot {
    private String username;
    private String password;
    private NetzwerkZugriff socialbotnet;
    private Bots bot;
    private SpeichernNutzer speichernNutzer;
    private Nutzer user;

    /**
     * Adresse: https://socialbotnet-o1cy.onrender.com
     */
    public SocialBot() {
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-i635.onrender.com");
    }

    public SocialBot(String usernameNeu, String passwordNeu) {
        this.username = usernameNeu;
        this.password = passwordNeu;
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-i635.onrender.com");
    }

public void hashTagLiken(String neuerHashTag) {
    // Regul�rer Ausdruck f�r exakten Hashtag (z.?B. #java, aber nicht #javascript)
    Pattern hashtagPattern = Pattern.compile("\\B" + Pattern.quote(neuerHashTag) + "\\b");

    try {
        // Senden der GET-Anfrage und Speichern der Antwort als String
        String antwort = socialbotnet.GETAnfrageSenden("/api/posts");

        // Leere oder fehlerhafte Antwort pr�fen
        if (antwort == null || antwort.isEmpty()) {
            System.err.println("Leere Antwort vom Server erhalten.");
            return;
        }

        // Parsen der Antwort als JSON-Array
        JSONArray posts = new JSONArray(antwort);

        // Alle Posts pr�fen
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            String content = post.optString("message", "");
            int postId = post.optInt("id", -1);

            // Wenn Post-ID g�ltig ist und Hashtag gefunden wurde ? liken
            Matcher matcher = hashtagPattern.matcher(content);
            if (postId >= 0 && matcher.find()) {
                liken(postId);
            }
        }

    } catch (JSONException e) {
        System.err.println("Fehler beim Verarbeiten der JSON-Daten: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Unerwarteter Fehler: " + e.getClass().getSimpleName() + " � " + e.getMessage());
        e.printStackTrace();
    }
}

    public void posten(String nachricht) {
        try {
            // POST-Anfrage mit Parametern vorbereiten.
            socialbotnet.POSTAnfrageVorbereiten("username", username);
            socialbotnet.POSTAnfrageVorbereiten("password", password);
            socialbotnet.POSTAnfrageVorbereiten("message", nachricht);
            // Post senden.
            socialbotnet.POSTAnfrageSenden("/api/post");
        } catch (Exception e) {
            System.err.println("Fehler beim Posten: " + e.getMessage());
        }
    }

    public void postsVonNutzer(String user) {
        try {
            String antwort = socialbotnet.GETAnfrageSenden("/api/pinnwand/" + user + "?sortby=likes");
            
            // Parsen der Antwort als JSON-Array.
            JSONArray nutzerArray = new JSONArray(antwort);

            // Schleife durch das Array, um alle Posts auszugeben.
            for (int i = 0; i < nutzerArray.length(); i++) {
                JSONObject nutzerObjekt = nutzerArray.getJSONObject(i);
                String message = nutzerObjekt.getString("message");
                System.out.println((i + 1) + ". Post: " + message);
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen der Pinnwand: " + e.getMessage());
        }
    }

    public void armeeErzeugen(int n) {
        for (int i = 0; i < n; i++) {
            this.bot = new Bots();
            String neuerNutzername = bot.neuenNutzerNamenErzeugen();
            String neuesPasswort = bot.neuesPasswortErzeugen();
            System.out.println("Der neue Nutzer hat folgenden Nutzernamen: " + neuerNutzername);
            System.out.println("Der neue Nutzer hat folgendes Passwort: " + neuesPasswort);
            speichernNutzer = new SpeichernNutzer();
            speichernNutzer.speichernNutzer(neuerNutzername, neuesPasswort);
            user = new Nutzer();
            user.nutzerAnmelden(neuerNutzername, neuesPasswort);
        }
    }

    public void allePostsEinesNutzersLiken(String username) {
        try {
            String antwort = socialbotnet.GETAnfrageSenden("/api/pinnwand/" + username);
            Post[] nachrichten = AntwortParser.zuPostArray(antwort);
            for (Post nachricht : nachrichten) {
                int postId = nachricht.getId();
                liken(postId);
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Liken der Posts: " + e.getMessage());
        }
    }

    public void liken(int postId) {
        try {
            SpeichernNutzer speicher = new SpeichernNutzer();
            List<String[]> nutzerDaten = speicher.getNutzernamenUndPasswoerter();

            for (String[] daten : nutzerDaten) {
                // POST-Anfrage mit Parametern vorbereiten.
                socialbotnet.POSTAnfrageVorbereiten("username", daten[0]);
                socialbotnet.POSTAnfrageVorbereiten("password", daten[1]);
                socialbotnet.POSTAnfrageVorbereiten("postid", postId);
                // Like senden.
                socialbotnet.POSTAnfrageSenden("/api/like");
            }

        } catch (Exception e) {
            System.err.println("Fehler beim Liken: " + e.getMessage());
        }
    }

    public void likenNutzer(int postId) {
        try {
            // POST-Anfrage mit Parametern vorbereiten.
            socialbotnet.POSTAnfrageVorbereiten("username", username);
            socialbotnet.POSTAnfrageVorbereiten("password", password);
            socialbotnet.POSTAnfrageVorbereiten("postid", postId);
            // Like senden.
            socialbotnet.POSTAnfrageSenden("/api/like");
        } catch (Exception e) {
            System.err.println("Fehler beim Liken des Nutzers: " + e.getMessage());
        }
    }

    public void botsPosten(String nachricht) {
        try {
            SpeichernNutzer speicher = new SpeichernNutzer();
            List<String[]> nutzerDaten = speicher.getNutzernamenUndPasswoerter();
            for (String[] daten : nutzerDaten) {
                // POST-Anfrage mit Parametern vorbereiten.
                socialbotnet.POSTAnfrageVorbereiten("username", daten[0]);
                socialbotnet.POSTAnfrageVorbereiten("password", daten[1]);
                socialbotnet.POSTAnfrageVorbereiten("message", nachricht);
                // Post senden.
                socialbotnet.POSTAnfrageSenden("/api/post");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Posten durch Bots: " + e.getMessage());
        }
    }
}
