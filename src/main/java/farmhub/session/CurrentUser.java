package farmhub.session;

public class CurrentUser {
    private int id;
    private String username;
    private String email;
    private int farmerId;  // Added farmerId

    public CurrentUser(int id, String username, String email, int farmerId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.farmerId = farmerId;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getFarmerId() {
        return farmerId;
    }
}
