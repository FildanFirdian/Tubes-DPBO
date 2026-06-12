    package actor;


public abstract class User {
    protected int idUser;
    protected String username;
    protected String password;

    public User(int idUser,String username, String password) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
    }

    public abstract void tampilInfo();

    public int getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}