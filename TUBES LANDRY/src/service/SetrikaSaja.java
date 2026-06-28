package service;

/**
 * {@code SetrikaSaja} adalah layanan laundry khusus penyetrikaan tanpa
 * proses pencucian, dengan potongan harga (diskon).
 *
 * <p><b>Rumus biaya:</b>
 * {@code subtotal = berat × hargaPerKg},
 * {@code biaya = subtotal - (subtotal × penguranganBiaya)}</p>
 *
 * <p>{@code penguranganBiaya} adalah persentase diskon dalam bentuk desimal,
 * misalnya {@code 0.10} berarti diskon 10%.</p>
 */
public class SetrikaSaja extends Layanan {

    /**
     * Persentase pengurangan biaya (diskon) dalam bentuk desimal.
     * Contoh: 0.10 = 10% diskon. Nilai harus antara 0.0 dan 1.0.
     */
    private double penguranganBiaya;

    /**
     * Membuat objek layanan Setrika Saja.
     *
     * @param idLayanan        ID layanan dari database (harus &gt; 0)
     * @param namaLayanan      nama layanan untuk ditampilkan di UI
     * @param hargaPerKg       harga per kilogram dalam Rupiah
     * @param estimasiHari     estimasi hari penyelesaian
     * @param jenisProses      jenis proses fisik (misal: "Manual")
     * @param penguranganBiaya persentase diskon desimal (0.0–1.0); tidak boleh negatif
     * @throws IllegalArgumentException jika penguranganBiaya &lt; 0 atau &gt; 1
     */
    public SetrikaSaja(int idLayanan, String namaLayanan,
                       double hargaPerKg, int estimasiHari,
                       String jenisProses, double penguranganBiaya) {
        super(idLayanan, namaLayanan, hargaPerKg, estimasiHari, jenisProses);
        if (penguranganBiaya < 0 || penguranganBiaya > 1) {
            throw new IllegalArgumentException(
                "Pengurangan biaya harus antara 0.0 dan 1.0 (0%–100%). " +
                "Nilai diterima: " + penguranganBiaya);
        }
        this.penguranganBiaya = penguranganBiaya;
    }

    /**
     * Mengembalikan persentase pengurangan biaya (diskon).
     *
     * @return pengurangan biaya sebagai desimal (0.0–1.0)
     */
    public double getPenguranganBiaya() { return penguranganBiaya; }

    /**
     * Mengubah persentase pengurangan biaya (diskon).
     *
     * @param penguranganBiaya nilai baru; harus antara 0.0 dan 1.0
     * @throws IllegalArgumentException jika nilai di luar rentang valid
     */
    public void setPenguranganBiaya(double penguranganBiaya) {
        if (penguranganBiaya < 0 || penguranganBiaya > 1) {
            throw new IllegalArgumentException(
                "Pengurangan biaya harus antara 0.0 dan 1.0. Diterima: " + penguranganBiaya);
        }
        this.penguranganBiaya = penguranganBiaya;
    }

    /**
     * Menghitung total biaya layanan setrika dengan potongan harga.
     *
     * <p><b>Rumus:</b><br>
     * {@code subtotal = berat × hargaPerKg}<br>
     * {@code potongan = subtotal × penguranganBiaya}<br>
     * {@code biaya    = subtotal - potongan}</p>
     *
     * @param berat berat cucian dalam kilogram; harus &gt; 0
     * @return total biaya setelah potongan dalam Rupiah
     * @throws IllegalArgumentException jika berat &lt;= 0
     */
    @Override
    public double hitungBiaya(double berat) {
        if (berat <= 0) {
            throw new IllegalArgumentException(
                "Berat laundry tidak valid: " + berat + " kg. Harus lebih dari 0.");
        }
        double subtotal = berat * getHargaPerKg();
        double potongan = subtotal * penguranganBiaya;
        return subtotal - potongan;
    }

    /**
     * Mengembalikan deskripsi singkat layanan Setrika Saja.
     *
     * @return deskripsi berformat "Setrika Saja - [proses] | Diskon: [x]% ([y] Hari)"
     */
    @Override
    public String deskripsiLayanan() {
        return "Setrika Saja - " + getJenisProses() +
               " | Diskon: " + (int)(penguranganBiaya * 100) + "%" +
               " (" + getEstimasiHari() + " Hari)";
    }
}