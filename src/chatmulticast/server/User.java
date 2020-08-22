package chatmulticast.server;

public class User {
    private String address;
    private String name;

    public User(String username, String address) {
        this.name = username;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
