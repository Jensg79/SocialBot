import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NetzwerkZugriff {
    private String domain;
    private ArrayList<String> parameter = new ArrayList<String>();
   
    public NetzwerkZugriff(String domainName) {
        System.setProperty("java.net.useSystemProxies", "true");
        domain = domainName;
    }

    /**
     * Setzt Daten, die bei der nächsten POST-Anfrage gesendet werden sollen.
     * @param schluessel Der Parametername, der gesetzt werden soll
     * @param daten Die Parameterdaten, die später gesendet werden sollen
     */
    public void POSTAnfrageVorbereiten(String schluessel, Object daten) {
        try {
            parameter.add(URLEncoder.encode(schluessel, "UTF-8") + "=" + URLEncoder.encode(daten.toString(), "UTF-8"));
        } catch (Exception ignore) {}
    }

    /**
     * Sendet die POST-Anfrage an die angegebene Adresse an den verbundenen Server. 
     * Die Daten müssen vorher mit POSTAnfrageVorbereiten gesetzt werden.
     * @param url Die relative Adresse auf dem Server, an die die Anfrage geschickt werden soll
     */
    public void POSTAnfrageSenden(String url) {
        try {
            URL urlobj = new URL(domain + url);
            HttpURLConnection verbindung = (HttpURLConnection) urlobj.openConnection();
            verbindung.setRequestMethod("POST");

            StringBuilder anfrageBuilder = new StringBuilder();
            for (int i = 0; i < parameter.size(); i++) {                
                if ( i != 0) {
                    anfrageBuilder.append('&');
                }
                anfrageBuilder.append(parameter.get(i));
            }
            String urlParameter = anfrageBuilder.toString();

            // Send post request
            verbindung.setDoOutput(true);

            PrintWriter writer = new PrintWriter(verbindung.getOutputStream());
            writer.print(urlParameter);
            writer.flush();
            writer.close();
            parameter = new ArrayList<String>();

            System.out.println("\nSende 'POST'-Anfrage an URL : " + url);
            System.out.println("POST Parameter : " + urlParameter);
            antwortLesen(verbindung);

        } catch (MalformedURLException e) {
            System.out.println("Fehlerhafte URL " + domain + url);
            System.out.println("Vielleicht hast du http:// oder https:// vergessen?");
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            System.out.println("Bei der Verbindung ist etwas schief gegangen: " + e.getMessage());
        }
    }

    /**
     * Sendet die GET-Anfrage an die angegebene Adresse an den verbundenen Server.
     * Parameter können mit ? angehängt werden. Bei mehreren Parametern müssen diese durch & verbunden werden.
     * Beispiel: /api/posts?sortby=likes&limit=5 
     * @param url Die relative Adresse auf dem Server, an die die Anfrage geschickt werden soll
     */

    public String GETAnfrageSenden(String url) {
        String antwort = "";
        try {
            URL urlobj = new URL(domain + url);
            HttpURLConnection verbindung = (HttpURLConnection) urlobj.openConnection(); 
            verbindung.setRequestMethod("GET");

            System.out.println("\nSende 'GET'-Anfrage an URL : " + url);
            antwort = antwortLesen(verbindung);
        } catch (MalformedURLException e) {
            System.out.println("Fehlerhafte URL " + domain + url);
            System.out.println("Vielleicht hast du http:// oder https:// vergessen?");
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            System.out.println("Bei der Verbindung ist etwas schief gegangen: " + e.getMessage());
        }

        return antwort;
    }

    private String antwortLesen(HttpURLConnection verbindung) {
        try{
            int responseCode = verbindung.getResponseCode();
            System.out.println("Die Antwort hat den Status : " + responseCode + " " + verbindung.getResponseMessage());

            BufferedReader in = null;
            try {
                if (responseCode == 200) {
                    in = new BufferedReader(new InputStreamReader(verbindung.getInputStream()));
                } else {
                    in = new BufferedReader(new InputStreamReader(verbindung.getErrorStream()));
                }

                String inputZeile;
                StringBuffer antwortBuffer = new StringBuffer();

                while ((inputZeile = in.readLine()) != null) {
                    antwortBuffer.append(inputZeile.trim());
                }
                String antwort = antwortBuffer.toString();
                System.out.println("Antwort: " + antwort);
                return antwort;
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Bei der Verbindung ist etwas schief gegangen: " + e.getMessage());
            return null;
        }
    }
}
