package sistem;

/**
 * {@code StatusLaundry} merepresentasikan status terkini dari sebuah transaksi laundry.
 *
 * <p>Data status dimuat dari tabel {@code status_laundry} di database.
 * Status yang tersedia secara default:</p>
 * <ul>
 *   <li>ID 1 – Diproses</li>
 *   <li>ID 2 – Selesai</li>
 *   <li>ID 3 – Diambil</li>
 * </ul>
 *
 * <p>Objek ini digunakan oleh {@link Transaksi} dan ditampilkan sebagai
 * pilihan dropdown di tab Detail Transaksi untuk mengubah status pesanan.</p>
 */
public class StatusLaundry {

    /** ID unik status dari tabel {@code status_laundry}. */
    private int idStatus;

    /** Nama status yang ditampilkan di UI (misal: "Diproses", "Selesai"). */
    private String namaStatus;

    /**
     * Membuat objek StatusLaundry.
     *
     * @param idStatus   ID unik status; harus &gt;= 1
     * @param namaStatus nama status; tidak boleh kosong
     * @throws IllegalArgumentException jika idStatus &lt; 1 atau namaStatus kosong
     */
    public StatusLaundry(int idStatus, String namaStatus) {
        if (idStatus < 1) {
            throw new IllegalArgumentException("ID status harus >= 1.");
        }
        if (namaStatus == null || namaStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama status tidak boleh kosong.");
        }
        this.idStatus   = idStatus;
        this.namaStatus = namaStatus;
    }

    /**
     * Mengembalikan ID status laundry.
     *
     * @return ID status (integer)
     */
    public int getIdStatus() { return idStatus; }

    /**
     * Mengembalikan nama status laundry.
     *
     * @return nama status sebagai String
     */
    public String getNamaStatus() { return namaStatus; }

    /**
     * Mengembalikan nama status sebagai representasi teks objek.
     * Digunakan oleh ComboBox JavaFX di UI.
     *
     * @return nama status sebagai String
     */
    @Override
    public String toString() { return namaStatus; }
}