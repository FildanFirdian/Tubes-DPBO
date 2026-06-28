package service;

/**
 * {@code CuciSetrika} adalah layanan laundry yang mencuci, mengeringkan,
 * sekaligus menyetrika pakaian.
 *
 * <p><b>Rumus biaya:</b> {@code (berat × hargaPerKg) + biayaTambahan}</p>
 *
 * <p>Biaya tambahan merupakan biaya tetap untuk proses penyetrikaan
 * yang ditambahkan di atas biaya cuci dasar.</p>
 */
public class CuciSetrika extends Layanan {

    /**
     * Biaya tetap tambahan dalam Rupiah untuk proses penyetrikaan.
     * Nilai ini tidak bergantung pada berat cucian.
     */
    private double biayaTambahan;

    /**
     * Membuat objek layanan Cuci Setrika.
     *
     * @param idLayanan     ID layanan dari database (harus &gt; 0)
     * @param namaLayanan   nama layanan untuk ditampilkan di UI
     * @param hargaPerKg    harga per kilogram dalam Rupiah
     * @param estimasiHari  estimasi hari penyelesaian
     * @param jenisProses   jenis proses fisik (misal: "Setrika Uap")
     * @param biayaTambahan biaya tetap untuk penyetrikaan; jika negatif akan di-set 0
     */
    public CuciSetrika(int idLayanan, String namaLayanan,
                       double hargaPerKg, int estimasiHari,
                       String jenisProses, double biayaTambahan) {
        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari, jenisProses);
        this.biayaTambahan = (biayaTambahan < 0) ? 0 : biayaTambahan;
    }

    /**
     * Mengembalikan biaya tambahan penyetrikaan.
     *
     * @return biaya tambahan dalam Rupiah (double)
     */
    public double getBiayaTambahan() { return biayaTambahan; }

    /**
     * Mengubah biaya tambahan penyetrikaan.
     *
     * @param biayaTambahan biaya baru; tidak boleh negatif
     * @throws IllegalArgumentException jika biayaTambahan &lt; 0
     */
    public void setBiayaTambahan(double biayaTambahan) {
        if (biayaTambahan < 0) {
            throw new IllegalArgumentException(
                "Biaya tambahan tidak boleh negatif: " + biayaTambahan);
        }
        this.biayaTambahan = biayaTambahan;
    }

    /**
     * Menghitung total biaya cuci setrika berdasarkan berat cucian.
     *
     * <p><b>Rumus:</b> {@code biaya = (berat × hargaPerKg) + biayaTambahan}</p>
     *
     * @param berat berat cucian dalam kilogram; harus &gt; 0
     * @return total biaya dalam Rupiah
     * @throws IllegalArgumentException jika berat &lt;= 0
     */
    @Override
    public double hitungBiaya(double berat) {
        if (berat <= 0) {
            throw new IllegalArgumentException(
                "Berat laundry tidak valid: " + berat + " kg. Harus lebih dari 0.");
        }
        return (berat * getHargaPerKg()) + biayaTambahan;
    }

    /**
     * Mengembalikan deskripsi singkat layanan Cuci Setrika.
     *
     * @return deskripsi berformat "Cuci Setrika - [proses] ([x] Hari)"
     */
    @Override
    public String deskripsiLayanan() {
        return "Cuci Setrika - " + getJenisProses() +
               " (" + getEstimasiHari() + " Hari)" +
               " | Biaya setrika: Rp" + (long) biayaTambahan;
    }
}