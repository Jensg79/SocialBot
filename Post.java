import java.sql.Timestamp;

public class Post {
    private int id;
    private String message = "";
    private Timestamp publishingDate;
    private User user;
    private User wall;
    private double trendingScore;
    private int likes;
    private User[] likedBy = new User[0];

    public Post(int id, String message, Timestamp publishingDate, User user, User wall, double trendingScore, int likes, User[] likedBy) {
        this.id = id;
        this.message = message;
        this.publishingDate = publishingDate;
        this.user = user;
        this.wall = wall;
        this.trendingScore = trendingScore;
        this.likes = likes;
        this.likedBy = likedBy;
    }

    /** @return the id */
    public int getId() {
        return id;
    }

    /** @return the message */
    public String getMessage() {
        return message;
    }

    /** @return the publishingDate */
    public Timestamp getPublishingDate() {
        return publishingDate;
    }

    /** @return the user */
    public User getUser() {
        return user;
    }

    /** @return the userid */
    public int getUserId() {
        return user.getId();
    }

    /** @return the username */
    public String getUsername() {
        return user.getUsername();
    }

    /** @return the wall */
    public User getWall() {
        return wall;
    }

    /** @return the likedBy */
    public User[] getLikedBy() {
        return likedBy;
    }
}
