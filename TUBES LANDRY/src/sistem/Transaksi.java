package sistem;
import java.util.ArrayList;
import java.util.Date;
import actor.Pelanggan;

import java.util.ArrayList;
import java.util.Date;

public class Transaksi {

    private int idTransaksi;
    private Date tanggal;
    private Pelanggan pelanggan;

    private ArrayList<DetailTransaksi> daftarDetail;

    private StatusLaundry statusLaundry;

    private double totalHarga;
    private double jumlahBayar;
    private double kembalian;

    public Transaksi(int idTransaksi, Date tanggal, Pelanggan pelanggan) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.pelanggan = pelanggan;

        daftarDetail = new ArrayList<>();
    }

    public void tambahDetail(DetailTransaksi detail) {
        daftarDetail.add(detail);
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public double hitungTotalHarga() {

        totalHarga = 0;

        for (DetailTransaksi detail : daftarDetail) {
            totalHarga += detail.getSubtotal();
        }

        return totalHarga;
    }

    public void prosesPembayaran(double jumlahBayar) {

        this.jumlahBayar = jumlahBayar;

        hitungTotalHarga();

        kembalian = jumlahBayar - totalHarga;
    }

    public void ubahStatus(StatusLaundry status) {
        this.statusLaundry = status;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public double getKembalian() {
        return kembalian;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }
}