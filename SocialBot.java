import org.json.JSONObject;
import org.json.JSONArray;
import java.util.*;
public class SocialBot {
    private String username;
    private String password;
    private NetzwerkZugriff socialbotnet;
    private Bots bot;
    private SpeichernNutzer neu;
    private Registrieren reg;

    /**
     * Adresse: https://socialbotnet-ajvb.onrender.com
     * us
     */
    public SocialBot() 
    {
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-ajvb.onrender.com");
        //this.armeeErzeugen();   
    }

    public SocialBot(String usernameNeu, String passwordNeu) {
        this.username = usernameNeu;
        this.password = passwordNeu;
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-ajvb.onrender.com");
    }

public void hashTagLiken(String neuerHashTag) {
    try {
        // Senden der GET-Anfrage und Speichern der Antwort als String.
        String antwort = socialbotnet.GETAnfrageSenden("/api/posts");

        // Parsen der Antwort als JSON-Array.
        JSONArray posts = new JSONArray(antwort);

        // Liste, um Post-IDs und Nachrichten mit dem Hashtag zu speichern.
        List<int[]> gefiltertePosts = new ArrayList<>();

        String hashtag = neuerHashTag;

        // Alle Posts durchlaufen und nach dem Hashtag filtern
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            String content = post.getString("message");
            int postId = post.getInt("id");  // Die Post-ID wird jetzt als int gespeichert

            if (content.contains(hashtag)) {
                // Post-ID und Nachricht in einem Array speichern und zur Liste hinzufügen
                gefiltertePosts.add(new int[]{postId, content.hashCode()});  // content in einen int (z.B. Hash) umwandeln
            }
        }

        // Für alle gefilterten Posts die Methode posten(id) aufrufen
        for (int[] post : gefiltertePosts) {
            int postid = post[0];  // Post-ID
            liken(postid);        // Aufruf der Methode posten mit der Post-ID
        }

    } catch (Exception e) {
        // Fehlerbehandlung bei Problemen mit der Anfrage oder JSON-Parsing.
        System.err.println("Fehler beim Abfragen der Posts: " + e.getMessage());
    }
}


    public void posten(String nachricht) {
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

    public void postsVonNutzer(String user) {
        String antwort = socialbotnet.GETAnfrageSenden("/api/pinnwand/"+ user+"?sortby = likes");
    }

    public void armeeErzeugen(int n)
    { 

        for (int i = 0; i < n; i++) 
        {
            this.bot = new Bots();
            String nutzerNeu = bot.neuenNutzerNamenErzeugen();
            String passwortNeu = bot.neuesPasswortErzeugen();
            neu = new SpeichernNutzer();
            neu.speichernNutzer(nutzerNeu,passwortNeu);
            reg = new Registrieren();
            reg.nutzerAnmelden(nutzerNeu,passwortNeu);

        }

    }

    public void allePostsEinesNutzersLiken(String username)
    {
        String antwort = socialbotnet.GETAnfrageSenden("/api/pinnwand/"+username);
        // alle nachricht in einem Array speichern
        Post[] nachrichten = AntwortParser.zuPostArray(antwort);
        for(int i=0; i<nachrichten.length; i++) {
            Post nachricht = nachrichten[i];
            // Hole die Nachrichten ID aus dem Post Objekt
            int postid = nachricht.getId();
            // Diese ID wird als Eingabewert zum Liken benoetigt
            likenNutzer(postid);
        }
    }
    public void liken(int postid) {
        SpeichernNutzer speicher = new SpeichernNutzer(); 
        List<String[]> nutzerDaten = speicher.getNutzernamenUndPasswoerter();
 
        for (String[] daten : nutzerDaten) {
                // POST-Anfrage mit Parametern vorbereiten
            socialbotnet.POSTAnfrageVorbereiten("username", daten[0] );
            socialbotnet.POSTAnfrageVorbereiten("password",daten[1] );
            socialbotnet.POSTAnfrageVorbereiten("postid", postid);
            // Like schicken
            socialbotnet.POSTAnfrageSenden("/api/like");
        }

        
    }
    public void likenNutzer(int postid) {
        // POST-Anfrage mit Parametern vorbereiten
        socialbotnet.POSTAnfrageVorbereiten("username", username);
        socialbotnet.POSTAnfrageVorbereiten("password", password);
        socialbotnet.POSTAnfrageVorbereiten("postid", postid);
        // Like schicken
        socialbotnet.POSTAnfrageSenden("/api/like");
    }

    public void botsPosten(String nachricht)
    {
        SpeichernNutzer speicher = new SpeichernNutzer(); 
        List<String[]> nutzerDaten = speicher.getNutzernamenUndPasswoerter();
        for (String[] daten : nutzerDaten) {
                // POST-Anfrage mit Parametern vorbereiten
            socialbotnet.POSTAnfrageVorbereiten("username", daten[0] );
            socialbotnet.POSTAnfrageVorbereiten("password",daten[1] );
            socialbotnet.POSTAnfrageVorbereiten("message",nachricht );
            // Like schicken
            socialbotnet.POSTAnfrageSenden("/api/post");
        }

    }

}
