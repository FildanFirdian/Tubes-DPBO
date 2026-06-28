package actor;

/**
 * Kelas abstrak {@code User} merupakan kelas induk (superclass) dari semua
 * pengguna sistem laundry, yaitu {@link Admin} dan {@link Pelanggan}.
 *
 * <p>Kelas ini menerapkan konsep <b>Abstraksi</b> dalam OOP: setiap User
 * wajib mengimplementasikan metode {@link #tampilInfo()} sesuai dengan
 * peran masing-masing.</p>
 *
 * <p><b>Validasi input:</b> username dan password tidak boleh null atau kosong.</p>
 */
public abstract class User {

    /** ID unik user di tabel {@code user} database. */
    protected int idUser;

    /** Nama pengguna untuk login ke sistem. */
    protected String username;

    /** Kata sandi pengguna (disimpan plain-text untuk keperluan akademik). */
    protected String password;

    /**
     * Membuat objek User baru.
     *
     * @param idUser   ID unik user dari database (boleh 0 jika belum tersimpan)
     * @param username nama pengguna untuk login; tidak boleh null atau kosong
     * @param password kata sandi pengguna; tidak boleh null atau kosong
     * @throws IllegalArgumentException jika username atau password kosong/null
     */
    public User(int idUser, String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong.");
        }
        this.idUser   = idUser;
        this.username = username;
        this.password = password;
    }

    /**
     * Menampilkan informasi lengkap pengguna ke console.
     * Wajib diimplementasikan oleh setiap subclass.
     */
    public abstract void tampilInfo();

    /**
     * Mengembalikan ID user dari database.
     *
     * @return ID user (integer)
     */
    public int getIdUser() { return idUser; }

    /**
     * Mengembalikan username pengguna.
     *
     * @return username sebagai String
     */
    public String getUsername() { return username; }

    /**
     * Mengembalikan password pengguna.
     *
     * @return password sebagai String
     */
    public String getPassword() { return password; }
}