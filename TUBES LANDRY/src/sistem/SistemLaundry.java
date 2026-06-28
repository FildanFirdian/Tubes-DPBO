package sistem;

import actor.Pelanggan;
import service.Layanan;
import java.util.HashMap;

/**
 * {@code SistemLaundry} adalah kelas pengelola data in-memory (tanpa database)
 * yang menggunakan struktur data {@link HashMap} untuk menyimpan pelanggan,
 * layanan, dan transaksi.
 *
 * <p><b>Catatan:</b> Kelas ini merupakan versi awal sebelum integrasi database.
 * Untuk aplikasi dengan koneksi MySQL, gunakan {@link DatabaseManager} sebagai
 * gantinya. Kelas ini tetap dipertahankan untuk dokumentasi arsitektur awal.</p>
 *
 * <p><b>Struktur data:</b>
 * <ul>
 *   <li>{@code daftarPelanggan} — Map ID pelanggan → objek Pelanggan</li>
 *   <li>{@code daftarLayanan} — Map ID layanan → objek Layanan</li>
 *   <li>{@code daftarTransaksi} — Map ID transaksi → objek Transaksi</li>
 * </ul>
 * </p>
 */
public class SistemLaundry {

    /** Penyimpanan in-memory untuk data pelanggan, di-index berdasarkan ID pelanggan. */
    private HashMap<Integer, Pelanggan> daftarPelanggan;

    /** Penyimpanan in-memory untuk data layanan, di-index berdasarkan ID layanan. */
    private HashMap<Integer, Layanan> daftarLayanan;

    /** Penyimpanan in-memory untuk data transaksi, di-index berdasarkan ID transaksi. */
    private HashMap<Integer, Transaksi> daftarTransaksi;

    /**
     * Menginisialisasi sistem laundry dengan HashMap kosong.
     * Data harus ditambahkan secara manual menggunakan metode tambah*().
     */
    public SistemLaundry() {
        daftarPelanggan = new HashMap<>();
        daftarLayanan   = new HashMap<>();
        daftarTransaksi = new HashMap<>();
    }

    // ==================== PELANGGAN ====================

    /**
     * Menambahkan atau mengganti data pelanggan di map.
     * Jika ID sudah ada, data lama akan ditimpa.
     *
     * @param pelanggan objek {@link Pelanggan} yang akan disimpan; tidak boleh null
     * @throws IllegalArgumentException jika pelanggan null
     */
    public void tambahPelanggan(Pelanggan pelanggan) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pelanggan yang ditambahkan tidak boleh null.");
        }
        daftarPelanggan.put(pelanggan.getIdPelanggan(), pelanggan);
    }

    /**
     * Mencari pelanggan berdasarkan ID-nya.
     *
     * @param id ID pelanggan yang dicari
     * @return objek {@link Pelanggan} jika ditemukan; {@code null} jika tidak ada
     */
    public Pelanggan cariPelanggan(int id) {
        return daftarPelanggan.get(id);
    }

    /**
     * Menghapus pelanggan dari map berdasarkan ID.
     * Tidak melakukan apa-apa jika ID tidak ditemukan.
     *
     * @param id ID pelanggan yang akan dihapus
     */
    public void hapusPelanggan(int id) {
        daftarPelanggan.remove(id);
    }

    /**
     * Menampilkan semua data pelanggan ke console (System.out).
     * Menggunakan metode {@code toString()} dari setiap pelanggan.
     */
    public void tampilSemuaPelanggan() {
        if (daftarPelanggan.isEmpty()) {
            System.out.println("Belum ada pelanggan terdaftar.");
            return;
        }
        for (Pelanggan p : daftarPelanggan.values()) {
            System.out.println(p);
        }
    }

    // ==================== LAYANAN ====================

    /**
     * Menambahkan atau mengganti data layanan di map.
     * Jika ID sudah ada, data lama akan ditimpa.
     *
     * @param layanan objek {@link Layanan} yang akan disimpan; tidak boleh null
     * @throws IllegalArgumentException jika layanan null
     */
    public void tambahLayanan(Layanan layanan) {
        if (layanan == null) {
            throw new IllegalArgumentException("Layanan yang ditambahkan tidak boleh null.");
        }
        daftarLayanan.put(layanan.getIdLayanan(), layanan);
    }

    /**
     * Mencari layanan berdasarkan ID-nya.
     *
     * @param id ID layanan yang dicari
     * @return objek {@link Layanan} jika ditemukan; {@code null} jika tidak ada
     */
    public Layanan cariLayanan(int id) {
        return daftarLayanan.get(id);
    }

    // ==================== TRANSAKSI ====================

    /**
     * Menambahkan atau mengganti data transaksi di map.
     * Jika ID sudah ada, data lama akan ditimpa.
     *
     * @param transaksi objek {@link Transaksi} yang akan disimpan; tidak boleh null
     * @throws IllegalArgumentException jika transaksi null
     */
    public void tambahTransaksi(Transaksi transaksi) {
        if (transaksi == null) {
            throw new IllegalArgumentException("Transaksi yang ditambahkan tidak boleh null.");
        }
        daftarTransaksi.put(transaksi.getIdTransaksi(), transaksi);
    }
}