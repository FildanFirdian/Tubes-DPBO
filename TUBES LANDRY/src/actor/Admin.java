package actor;


public class Admin extends User {

    private int idAdmin;
    private String username;
    private String password;
    private String namaAdmin;

    public Admin(int idAdmin, String username, String password, String namaAdmin) {
        super(idAdmin, username, password);
        this.idAdmin = idAdmin;
        this.username = username;
        this.password = password;
        this.namaAdmin = namaAdmin;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username)
                && this.password.equals(password);
    }

    public void tampilInfo() {
        System.out.println("ID Admin : " + idAdmin);
        System.out.println("Nama     : " + namaAdmin);
    }
}