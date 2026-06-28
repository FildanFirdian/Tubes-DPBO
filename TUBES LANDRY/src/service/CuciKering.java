package service;

/**
 * {@code CuciKering} adalah layanan laundry yang hanya melakukan proses
 * pencucian dan pengeringan tanpa menyetrika pakaian.
 *
 * <p><b>Rumus biaya:</b> {@code berat (kg) × hargaPerKg}</p>
 *
 * <p>Mewarisi {@link Layanan} dan mengimplementasikan logika perhitungan
 * biaya paling sederhana di antara semua jenis layanan.</p>
 */
public class CuciKering extends Layanan {

    /**
     * Membuat objek layanan Cuci Kering.
     *
     * @param idLayanan    ID layanan dari database (harus &gt; 0)
     * @param namaLayanan  nama layanan untuk ditampilkan di UI
     * @param hargaPerKg   harga per kilogram dalam Rupiah
     * @param estimasiHari estimasi hari penyelesaian
     * @param jenisProses  jenis proses fisik (misal: "Mesin Otomatis")
     */
    public CuciKering(int idLayanan, String namaLayanan,
                      double hargaPerKg, int estimasiHari, String jenisProses) {
        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari, jenisProses);
    }

    /**
     * Menghitung total biaya cuci kering berdasarkan berat cucian.
     *
     * <p><b>Rumus:</b> {@code biaya = berat × hargaPerKg}</p>
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
        return berat * getHargaPerKg();
    }

    /**
     * Mengembalikan deskripsi singkat layanan Cuci Kering.
     *
     * @return deskripsi berformat "Cuci Kering - [proses] ([x] Hari)"
     */
    @Override
    public String deskripsiLayanan() {
        return "Cuci Kering - " + getJenisProses() +
               " (" + getEstimasiHari() + " Hari)";
    }
}