
/**
 * Beschreiben Sie hier die Klasse Registrieren.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Registrieren
{
    private String username;
    private String password;
    private NetzwerkZugriff socialbotnet;

    /**
     * Konstruktor für Objekte der Klasse Registrieren
     */
    public Registrieren()
    {
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-ajvb.onrender.com");
    }
    public void nutzerAnmelden(String username, String passwort){
        // POST-Anfrage mit Parametern vorbereiten
        socialbotnet.POSTAnfrageVorbereiten("username", username);
        socialbotnet.POSTAnfrageVorbereiten("password", passwort);
        socialbotnet.POSTAnfrageVorbereiten("password2", passwort);
        
        // Anmeldung senden schicken
        socialbotnet.POSTAnfrageSenden("/registrieren");
        System.out.println("Der Nutzer" + username + "wurde erfolgreich registriert.");
    }
}