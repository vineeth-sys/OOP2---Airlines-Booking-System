package airline;

public sealed abstract class User permits Admin, Passenger {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
