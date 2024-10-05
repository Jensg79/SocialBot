import json.*;
import java.sql.Timestamp;

public class AntwortParser
{
    /**
     * Versucht die Antwort in ein User Array zu parsen.
     * @param antwort Die Antwort des SocialBotNet Servers
     * @return Die erstellten {@link User} Objekte
     */
    public static User[] zuUserArray(String antwort) {
        return zuUserArray(new JSONArray(antwort));
    }

    /**
     * Versucht die Antwort in ein Post Array zu parsen.
     * @param antwort Die Antwort des SocialBotNet Servers
     * @return Die erstellten {@link Post} Objekte
     */
    public static Post[] zuPostArray(String antwort) {
        return zuPostArray(new JSONArray(antwort));
    }

    /** Private Methoden **/

    private static User[] zuUserArray(JSONArray array) {
        int anzahl = array.length();
        User[] users = new User[anzahl];
        for (int i = 0; i < anzahl; i++) {
            JSONObject obj = array.getJSONObject(i);
            users[i] = zuUser(obj);
        }
        return users;
    }

    private static User zuUser(JSONObject obj) {
        int id = obj.getInt("id");
        String username = obj.getString("username");
        String hobbies = obj.getString("hobbies");
        String about = obj.getString("about");
        return new User(id, username, hobbies, about);
    }

    private static Post[] zuPostArray(JSONArray array) {
        int anzahl = array.length();
        Post[] posts = new Post[anzahl];
        for (int i = 0; i < anzahl; i++) {
            JSONObject obj = array.getJSONObject(i);
            posts[i] = zuPost(obj);
        }
        return posts;
    }

    private static Post zuPost(JSONObject obj) {
        int id = obj.getInt("id");
        String message = obj.getString("message");
        Timestamp publishingDate = Timestamp.valueOf(obj.getString("publishingDate"));
        User user = zuUser(obj.getJSONObject("user"));
        User wall = zuUser(obj.getJSONObject("wall"));
        double trendingScore = obj.getDouble("trendingScore");
        int likes = obj.getInt("likes");
        User[] likedBy = zuUserArray(obj.getJSONArray("likedBy"));
        return new Post(id, message, publishingDate, user, wall, trendingScore, likes, likedBy);
    }
}
