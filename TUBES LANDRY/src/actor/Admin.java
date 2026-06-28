package actor;

/**
 * Kelas {@code Admin} merepresentasikan administrator sistem laundry.
 *
 * <p>Admin mewarisi {@link User} dan memiliki kemampuan untuk melakukan
 * login serta mengelola seluruh data dalam sistem (pelanggan, transaksi,
 * status laundry).</p>
 *
 * <p><b>Relasi:</b> Admin ← User (inheritance)</p>
 */
public class Admin extends User {

    /** ID unik admin di tabel {@code admin} database. */
    private int idAdmin;

    /** Nama lengkap admin yang ditampilkan di dashboard. */
    private String namaAdmin;

    /**
     * Membuat objek Admin baru.
     *
     * @param idAdmin   ID unik admin dari database
     * @param username  nama pengguna untuk login; tidak boleh kosong
     * @param password  kata sandi admin; tidak boleh kosong
     * @param namaAdmin nama lengkap admin untuk ditampilkan di UI
     * @throws IllegalArgumentException jika namaAdmin kosong atau null
     */
    public Admin(int idAdmin, String username, String password, String namaAdmin) {
        super(idAdmin, username, password);
        if (namaAdmin == null || namaAdmin.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama admin tidak boleh kosong.");
        }
        this.idAdmin   = idAdmin;
        this.namaAdmin = namaAdmin;
    }

    /**
     * Memvalidasi kombinasi username dan password.
     *
     * <p>Digunakan pada proses login untuk memverifikasi identitas admin
     * secara lokal (bandingkan dengan objek yang sudah di-load dari DB).</p>
     *
     * @param username username yang diinputkan pengguna
     * @param password password yang diinputkan pengguna
     * @return {@code true} jika kombinasi cocok; {@code false} jika tidak
     */
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Menampilkan informasi admin ke console (implementasi dari {@link User#tampilInfo()}).
     */
    @Override
    public void tampilInfo() {
        System.out.println("ID Admin : " + idAdmin);
        System.out.println("Nama     : " + namaAdmin);
    }

    /**
     * Mengembalikan nama lengkap admin.
     *
     * @return nama admin sebagai String
     */
    public String getNamaAdmin() {
        return this.namaAdmin;
    }
}