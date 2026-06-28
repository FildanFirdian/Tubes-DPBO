package sistem;

import actor.Pelanggan;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code Transaksi} merepresentasikan satu sesi transaksi laundry
 * yang dilakukan oleh seorang pelanggan.
 *
 * <p>Satu transaksi dapat memiliki <b>banyak detail</b> ({@link DetailTransaksi}),
 * di mana setiap detail mencatat layanan yang dipilih beserta beratnya.</p>
 *
 * <p><b>Relasi:</b>
 * <ul>
 *   <li>Transaksi → Pelanggan (many-to-one)</li>
 *   <li>Transaksi → StatusLaundry (many-to-one)</li>
 *   <li>Transaksi → DetailTransaksi (one-to-many)</li>
 * </ul>
 * </p>
 */
public class Transaksi {

    /** ID unik transaksi dari tabel {@code transaksi} database. */
    private int idTransaksi;

    /** Pelanggan yang melakukan transaksi ini. */
    private Pelanggan pelanggan;

    /** Tanggal transaksi dalam format "YYYY-MM-DD". */
    private String tanggal;

    /** Status terkini transaksi (Diproses / Selesai / Diambil). */
    private StatusLaundry statusLaundry;

    /** Total biaya yang harus dibayar; dihitung otomatis dari detail list. */
    private double jumlahBayar;

    /** Daftar detail item yang menyusun transaksi ini. */
    private List<DetailTransaksi> detailList = new ArrayList<>();

    /**
     * Membuat objek Transaksi baru.
     *
     * <p>Status awal otomatis diset ke "Diproses" (ID=1).
     * Tanggal otomatis diisi dengan tanggal hari ini.</p>
     *
     * @param idTransaksi ID transaksi (0 jika belum tersimpan ke DB)
     * @param pelanggan   pelanggan pemilik transaksi; tidak boleh null
     * @throws IllegalArgumentException jika pelanggan null
     */
    public Transaksi(int idTransaksi, Pelanggan pelanggan) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pelanggan tidak boleh null pada transaksi.");
        }
        this.idTransaksi  = idTransaksi;
        this.pelanggan    = pelanggan;
        this.tanggal      = java.time.LocalDate.now().toString();
        this.statusLaundry = new StatusLaundry(1, "Diproses");
    }

    /**
     * Menambahkan satu item detail ke transaksi dan memperbarui total bayar.
     *
     * @param detail objek {@link DetailTransaksi} yang akan ditambahkan; tidak boleh null
     * @throws IllegalArgumentException jika detail null
     */
    public void tambahDetail(DetailTransaksi detail) {
        if (detail == null) {
            throw new IllegalArgumentException("Detail transaksi tidak boleh null.");
        }
        detailList.add(detail);
        this.jumlahBayar = getTotalHarga();
    }

    /**
     * Menghitung selisih antara jumlahBayar dan total harga aktual.
     * Berguna untuk menghitung kembalian jika ada overpayment.
     *
     * @return nilai kembalian; minimum 0 (tidak pernah negatif)
     */
    public double getKembalian() {
        double kembalian = jumlahBayar - getTotalHarga();
        return (kembalian < 0) ? 0 : kembalian;
    }

    /**
     * Menghitung total harga dari semua detail item.
     * Menggunakan stream untuk menjumlahkan subtotal setiap {@link DetailTransaksi}.
     *
     * @return total harga dalam Rupiah (double)
     */
    public double getTotalHarga() {
        return detailList.stream().mapToDouble(DetailTransaksi::getSubtotal).sum();
    }

    // ==================== Getters ====================

    /** @return ID unik transaksi */
    public int getIdTransaksi() { return idTransaksi; }

    /** @return pelanggan pemilik transaksi */
    public Pelanggan getPelanggan() { return pelanggan; }

    /** @return tanggal transaksi format "YYYY-MM-DD" */
    public String getTanggal() { return tanggal; }

    /** @return status laundry saat ini */
    public StatusLaundry getStatusLaundry() { return statusLaundry; }

    /** @return total bayar yang tersimpan (bisa berbeda dari getTotalHarga jika di-load dari DB) */
    public double getJumlahBayar() { return jumlahBayar; }

    /** @return daftar semua detail item layanan pada transaksi ini */
    public List<DetailTransaksi> getDetailList() { return detailList; }

    // ==================== Setters (digunakan saat load dari DB) ====================

    /**
     * Mengubah tanggal transaksi. Digunakan saat me-load data dari database.
     *
     * @param tanggal tanggal dalam format "YYYY-MM-DD"; tidak boleh kosong
     */
    public void setTanggal(String tanggal) {
        if (tanggal == null || tanggal.trim().isEmpty()) {
            throw new IllegalArgumentException("Tanggal transaksi tidak boleh kosong.");
        }
        this.tanggal = tanggal;
    }

    /**
     * Mengubah status laundry transaksi. Dipanggil dari UI saat admin
     * mengubah status, atau saat load dari database.
     *
     * @param statusLaundry status baru; tidak boleh null
     */
    public void setStatusLaundry(StatusLaundry statusLaundry) {
        if (statusLaundry == null) {
            throw new IllegalArgumentException("Status laundry tidak boleh null.");
        }
        this.statusLaundry = statusLaundry;
    }

    /**
     * Mengubah total jumlah bayar. Digunakan saat me-load data dari database
     * agar nilai konsisten dengan yang tersimpan di DB.
     *
     * @param jumlahBayar nilai total bayar; tidak boleh negatif
     */
    public void setJumlahBayar(double jumlahBayar) {
        if (jumlahBayar < 0) {
            throw new IllegalArgumentException("Jumlah bayar tidak boleh negatif.");
        }
        this.jumlahBayar = jumlahBayar;
    }
}