package actor;

import sistem.Transaksi;
import sistem.SistemLaundry;

/**
 * Kelas {@code Pelanggan} merepresentasikan pelanggan yang menggunakan
 * jasa laundry.
 *
 * <p>Pelanggan mewarisi {@link User} dan memiliki data identitas tambahan
 * seperti nama lengkap, nomor HP, dan alamat.</p>
 *
 * <p><b>Relasi:</b>
 * <ul>
 *   <li>Pelanggan ← User (inheritance)</li>
 *   <li>Pelanggan → Transaksi (one-to-many, dikelola via database)</li>
 * </ul>
 * </p>
 */
public class Pelanggan extends User {

    /** ID unik pelanggan di tabel {@code pelanggan} database. */
    private int idPelanggan;

    /** Nama lengkap pelanggan. */
    private String nama;

    /** Nomor HP pelanggan untuk keperluan kontak. */
    private String noHP;

    /** Alamat pengiriman/domisili pelanggan. */
    private String alamat;

    /**
     * Membuat objek Pelanggan baru.
     *
     * @param idPelanggan ID unik pelanggan dari database (0 jika belum tersimpan)
     * @param nama        nama lengkap pelanggan; tidak boleh kosong
     * @param noHP        nomor HP pelanggan; tidak boleh kosong
     * @param alamat      alamat pelanggan; tidak boleh kosong
     * @param idUser      ID relasi ke tabel user
     * @param username    username untuk login; tidak boleh kosong
     * @param password    password untuk login; tidak boleh kosong
     * @throws IllegalArgumentException jika nama, noHP, atau alamat kosong/null
     */
    public Pelanggan(int idPelanggan, String nama, String noHP, String alamat,
                     int idUser, String username, String password) {
        super(idUser, username, password);
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong.");
        }
        if (noHP == null || noHP.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor HP tidak boleh kosong.");
        }
        if (alamat == null || alamat.trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat tidak boleh kosong.");
        }
        this.idPelanggan = idPelanggan;
        this.nama        = nama;
        this.noHP        = noHP;
        this.alamat      = alamat;
    }

    /**
     * Mengembalikan ID unik pelanggan.
     *
     * @return ID pelanggan (integer)
     */
    public int getIdPelanggan() { return idPelanggan; }

    /**
     * Mengembalikan nama lengkap pelanggan.
     *
     * @return nama pelanggan sebagai String
     */
    public String getNama() { return nama; }

    /**
     * Mengubah nama pelanggan.
     *
     * @param nama nama baru; tidak boleh kosong
     * @throws IllegalArgumentException jika nama kosong atau null
     */
    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong.");
        }
        this.nama = nama;
    }

    /**
     * Mengembalikan nomor HP pelanggan.
     *
     * @return nomor HP sebagai String
     */
    public String getNoHP() { return noHP; }

    /**
     * Mengubah nomor HP pelanggan.
     *
     * @param noHP nomor HP baru; tidak boleh kosong
     * @throws IllegalArgumentException jika noHP kosong atau null
     */
    public void setNoHP(String noHP) {
        if (noHP == null || noHP.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor HP tidak boleh kosong.");
        }
        this.noHP = noHP;
    }

    /**
     * Mengembalikan alamat pelanggan.
     *
     * @return alamat sebagai String
     */
    public String getAlamat() { return alamat; }

    /**
     * Mengubah alamat pelanggan.
     *
     * @param alamat alamat baru; tidak boleh kosong
     * @throws IllegalArgumentException jika alamat kosong atau null
     */
    public void setAlamat(String alamat) {
        if (alamat == null || alamat.trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat tidak boleh kosong.");
        }
        this.alamat = alamat;
    }

    /**
     * Mengembalikan representasi teks dari objek Pelanggan.
     * Digunakan oleh ComboBox JavaFX untuk menampilkan nama di UI.
     *
     * @return String format "ID: x, Nama: ..., ..."
     */
    @Override
    public String toString() {
        return "ID: " + idPelanggan + ", Nama: " + nama +
               ", No HP: " + noHP + ", Alamat: " + alamat;
    }

    /**
     * Menampilkan informasi lengkap pelanggan ke console
     * (implementasi dari {@link User#tampilInfo()}).
     */
    @Override
    public void tampilInfo() {
        System.out.println("ID Pelanggan : " + idPelanggan);
        System.out.println("Nama         : " + nama);
        System.out.println("No HP        : " + noHP);
        System.out.println("Alamat       : " + alamat);
    }
}