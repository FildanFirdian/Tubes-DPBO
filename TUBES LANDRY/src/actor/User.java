    package actor;


public abstract class User {
    protected int idUser;
    protected String nama;
    protected String noHP;
    protected String alamat;
    protected String username;
    protected String password;

    public User(int idUser, String nama, String noHP, String alamat, String username, String password) {
        this.idUser = idUser;
        this.nama = nama;
        this.noHP = noHP;
        this.alamat = alamat;
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        System.out.println(nama + " berhasil logout.");
    }

    public void tampilInfo() {
        System.out.println("ID User : " + idUser);
        System.out.println("Nama    : " + nama);
        System.out.println("No HP   : " + noHP);
        System.out.println("Alamat  : " + alamat);
    }

    public int getIdUser() {
        return idUser;
    }

    public String getNama() {
        return nama;
    }

    public String getNoHP() {
        return noHP;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}