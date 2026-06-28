package sistem;

import service.Layanan;

/**
 * {@code DetailTransaksi} merepresentasikan satu baris item dalam sebuah
 * {@link Transaksi}, yaitu satu layanan dengan berat tertentu.
 *
 * <p>Subtotal dihitung otomatis saat objek dibuat menggunakan metode
 * {@link Layanan#hitungBiaya(double)} dari layanan yang dipilih.
 * Ini merupakan penerapan <b>Polimorfisme</b>: setiap subclass Layanan
 * menggunakan rumus hitungBiaya yang berbeda.</p>
 *
 * <p><b>Relasi:</b> DetailTransaksi → Layanan (many-to-one)</p>
 */
public class DetailTransaksi {

    /** ID unik detail dari tabel {@code detail_transaksi}. 0 jika belum tersimpan. */
    private int idDetail;

    /** Berat cucian dalam kilogram untuk item ini. */
    private double berat;

    /** Total biaya item ini, dihitung dari layanan.hitungBiaya(berat). */
    private double subtotal;

    /** Layanan yang dipilih untuk item ini. */
    private Layanan layanan;

    /**
     * Membuat DetailTransaksi dengan ID eksplisit (digunakan saat load dari database).
     *
     * @param idDetail ID detail dari database
     * @param berat    berat cucian dalam kg; harus &gt; 0
     * @param layanan  layanan yang dipilih; tidak boleh null
     * @throws IllegalArgumentException jika berat &lt;= 0 atau layanan null
     */
    public DetailTransaksi(int idDetail, double berat, Layanan layanan) {
        validasiInput(berat, layanan);
        this.idDetail = idDetail;
        this.berat    = berat;
        this.layanan  = layanan;
        this.subtotal = hitungSubtotal();
    }

    /**
     * Membuat DetailTransaksi baru tanpa ID (digunakan saat membuat transaksi baru).
     * ID akan di-generate otomatis oleh database saat disimpan.
     *
     * @param layanan layanan yang dipilih; tidak boleh null
     * @param berat   berat cucian dalam kg; harus &gt; 0
     * @throws IllegalArgumentException jika berat &lt;= 0 atau layanan null
     */
    public DetailTransaksi(Layanan layanan, double berat) {
        validasiInput(berat, layanan);
        this.idDetail = 0;
        this.berat    = berat;
        this.layanan  = layanan;
        this.subtotal = hitungSubtotal();
    }

    /**
     * Validasi terpusat untuk parameter berat dan layanan.
     *
     * @param berat   berat yang akan divalidasi
     * @param layanan layanan yang akan divalidasi
     * @throws IllegalArgumentException jika parameter tidak valid
     */
    private void validasiInput(double berat, Layanan layanan) {
        if (layanan == null) {
            throw new IllegalArgumentException("Layanan pada detail transaksi tidak boleh null.");
        }
        if (berat <= 0) {
            throw new IllegalArgumentException(
                "Berat tidak valid: " + berat + " kg. Minimal 0.1 kg.");
        }
    }

    /**
     * Menghitung subtotal item menggunakan polimorfisme layanan.
     * Setiap subclass {@link Layanan} menggunakan rumus berbeda.
     *
     * @return subtotal dalam Rupiah (hasil dari {@link Layanan#hitungBiaya(double)})
     */
    public double hitungSubtotal() {
        return layanan.hitungBiaya(berat);
    }

    /**
     * Mengembalikan ID detail dari database.
     *
     * @return ID detail (0 jika belum tersimpan)
     */
    public int getIdDetail() { return idDetail; }

    /**
     * Mengembalikan subtotal biaya item ini.
     *
     * @return subtotal dalam Rupiah (double)
     */
    public double getSubtotal() { return subtotal; }

    /**
     * Mengembalikan layanan yang dipilih untuk item ini.
     *
     * @return objek {@link Layanan}
     */
    public Layanan getLayanan() { return layanan; }

    /**
     * Mengembalikan berat cucian item ini.
     *
     * @return berat dalam kilogram (double)
     */
    public double getBerat() { return berat; }
}