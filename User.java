public class User {
    private int id;
    private String username;
    private String hobbies;
    private String about;

    public User(int id, String username, String hobbies, String about) {
        this.id = id;
        this.username = username;
        this.hobbies = hobbies;
        this.about = about;
    }

    /** @return the username */
    public String getUsername() {
        return username;
    }

    /** @return the id */
    public int getId() {
        return id;
    }

    /** @return the hobbies */
    public String getHobbies() {
        return hobbies;
    }

    /** @return the about */
    public String getAbout() {
        return about;
    }
}
