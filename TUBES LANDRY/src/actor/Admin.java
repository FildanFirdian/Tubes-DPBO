package actor;

import java.util.Collection;
import java.util.Date;

import service.Layanan;

import sistem.SistemLaundry;
import sistem.Transaksi;
import sistem.DetailTransaksi;
import sistem.Diskon;
import sistem.StatusLaundry;
import sistem.Nota;
import sistem.LaporanTransaksi;

public class Admin extends User {
    private String levelAkses;

    public Admin(int idUser, String nama, String noHP, String alamat, String username, String password, String levelAkses) {
        super(idUser, nama, noHP, alamat, username, password);
        this.levelAkses = levelAkses;
    }

    public void tambahPelanggan(SistemLaundry sistem, Pelanggan pelanggan) {
        sistem.tambahPelanggan(pelanggan);
    }

    public void tambahLayanan(SistemLaundry sistem, Layanan layanan) {
        sistem.tambahLayanan(layanan);
    }

    public Transaksi buatTransaksi(SistemLaundry sistem, int idTransaksi, Pelanggan pelanggan, Date tanggalMasuk, Date tanggalSelesai) {
        Transaksi transaksi = new Transaksi(idTransaksi, tanggalMasuk, tanggalSelesai, pelanggan);
        sistem.tambahTransaksi(transaksi);
        pelanggan.tambahTransaksi(transaksi);
        return transaksi;
    }

    public void tambahDetailTransaksi(Transaksi transaksi, DetailTransaksi detail) {
        transaksi.tambahDetail(detail);
    }

    public void terapkanDiskon(Transaksi transaksi, Diskon diskon) {
        transaksi.terapkanDiskon(diskon);
    }

    public void inputPembayaran(Transaksi transaksi, double jumlahBayar) {
        transaksi.inputPembayaran(jumlahBayar);
    }

    public void updateStatusLaundry(Transaksi transaksi, StatusLaundry statusBaru) {
        transaksi.ubahStatus(statusBaru);
    }

    public Pelanggan cariPelanggan(SistemLaundry sistem, int idPelanggan) {
        return sistem.cariPelanggan(idPelanggan);
    }

    public Layanan cariLayanan(SistemLaundry sistem, int idLayanan) {
        return sistem.cariLayanan(idLayanan);
    }

    public Transaksi cariTransaksi(SistemLaundry sistem, int idTransaksi) {
        return sistem.cariTransaksi(idTransaksi);
    }

    public Nota cetakNota(String noNota, Date tanggalCetak, Transaksi transaksi) {
        Nota nota = new Nota(noNota, tanggalCetak, transaksi);
        nota.cetakDokumen();
        return nota;
    }

    public LaporanTransaksi cetakLaporanTransaksi(String periode, Collection<Transaksi> daftarTransaksi) {
        LaporanTransaksi laporan = new LaporanTransaksi(periode, daftarTransaksi);
        laporan.cetakDokumen();
        return laporan;
    }

    @Override
    public void tampilInfo() {
        super.tampilInfo();
        System.out.println("Level Akses : " + levelAkses);
    }
}