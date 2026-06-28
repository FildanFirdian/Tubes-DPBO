package service;

/**
 * Kelas abstrak {@code Layanan} merupakan kelas induk dari semua jenis
 * layanan yang tersedia di sistem laundry.
 *
 * <p>Menerapkan konsep <b>Abstraksi</b> dan <b>Polimorfisme</b> OOP:
 * setiap subclass wajib mengimplementasikan {@link #hitungBiaya(double)}
 * dan {@link #deskripsiLayanan()} sesuai aturan masing-masing layanan.</p>
 *
 * <p><b>Subclass yang tersedia:</b>
 * <ul>
 *   <li>{@link CuciKering} – biaya = berat × harga/kg</li>
 *   <li>{@link CuciSetrika} – biaya = (berat × harga/kg) + biayaTambahan</li>
 *   <li>{@link SetrikaSaja} – biaya = (berat × harga/kg) × (1 - diskon)</li>
 * </ul>
 * </p>
 *
 * <p><b>Validasi:</b> Semua parameter harus valid saat konstruksi objek.</p>
 */
public abstract class Layanan {

    /** ID unik layanan di tabel {@code layanan} database. */
    private int idLayanan;

    /** Nama layanan yang ditampilkan di UI (misal: "Cuci Kering Reguler"). */
    private String namaLayanan;

    /** Harga dasar per kilogram dalam Rupiah. */
    private double hargaPerKg;

    /** Estimasi waktu penyelesaian layanan dalam hari. */
    private int estimasiHari;

    /** Jenis proses fisik yang digunakan (misal: "Mesin Otomatis", "Manual"). */
    private String jenisProses;

    /**
     * Membuat objek Layanan baru dengan validasi lengkap.
     *
     * @param idLayanan    ID unik layanan (harus &gt; 0)
     * @param namaLayanan  nama layanan; tidak boleh null/kosong
     * @param hargaPerKg   harga per kg dalam Rupiah; harus &gt; 0
     * @param estimasiHari jumlah hari estimasi; tidak boleh negatif
     * @param jenisProses  deskripsi jenis proses; tidak boleh null/kosong
     * @throws IllegalArgumentException jika salah satu parameter tidak valid
     */
    public Layanan(int idLayanan, String namaLayanan,
                   double hargaPerKg, int estimasiHari, String jenisProses) {
        if (idLayanan <= 0) {
            throw new IllegalArgumentException("ID layanan harus lebih dari 0.");
        }
        if (namaLayanan == null || namaLayanan.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama layanan tidak boleh kosong.");
        }
        if (hargaPerKg <= 0) {
            throw new IllegalArgumentException("Harga per Kg harus lebih dari 0.");
        }
        if (estimasiHari < 0) {
            throw new IllegalArgumentException("Estimasi hari tidak boleh negatif.");
        }
        if (jenisProses == null || jenisProses.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis proses tidak boleh kosong.");
        }
        this.idLayanan    = idLayanan;
        this.namaLayanan  = namaLayanan;
        this.hargaPerKg   = hargaPerKg;
        this.estimasiHari = estimasiHari;
        this.jenisProses  = jenisProses;
    }

    /**
     * Menghitung total biaya layanan berdasarkan berat cucian.
     * <p>Setiap subclass mengimplementasikan rumus perhitungan yang berbeda.</p>
     *
     * @param berat berat cucian dalam kilogram; harus &gt; 0
     * @return total biaya dalam Rupiah (double)
     * @throws IllegalArgumentException jika berat &lt;= 0
     */
    public abstract double hitungBiaya(double berat);

    /**
     * Mengembalikan deskripsi singkat layanan untuk ditampilkan di UI.
     * Mencakup jenis proses dan estimasi waktu.
     *
     * @return deskripsi layanan sebagai String
     */
    public abstract String deskripsiLayanan();

    /**
     * Mengembalikan ID unik layanan.
     *
     * @return ID layanan (integer)
     */
    public int getIdLayanan() { return idLayanan; }

    /**
     * Mengembalikan nama layanan.
     *
     * @return nama layanan sebagai String
     */
    public String getNamaLayanan() { return namaLayanan; }

    /**
     * Mengembalikan harga dasar per kilogram.
     *
     * @return harga per kg dalam Rupiah (double)
     */
    public double getHargaPerKg() { return hargaPerKg; }

    /**
     * Mengembalikan estimasi penyelesaian layanan.
     *
     * @return jumlah hari (integer)
     */
    public int getEstimasiHari() { return estimasiHari; }

    /**
     * Mengembalikan jenis proses fisik layanan.
     *
     * @return jenis proses sebagai String
     */
    public String getJenisProses() { return jenisProses; }

    /**
     * Mengembalikan nama layanan sebagai representasi teks objek.
     * Digunakan oleh ComboBox JavaFX di UI.
     *
     * @return nama layanan sebagai String
     */
    @Override
    public String toString() {
        return namaLayanan;
    }
}