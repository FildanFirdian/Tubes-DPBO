package sistem;
import actor.Pelanggan;
import actor.User;
import sistem.Transaksi;
import sistem.Nota;
import sistem.LaporanTransaksi;
import sistem.DetailTransaksi;
import sistem.StatusLaundry;
import actor.Admin;
import actor.Pelanggan;
import actor.User;
import service.Layanan;
import sistem.Transaksi;
import java.util.HashMap;

public class SistemLaundry {

    private HashMap<Integer, Pelanggan> daftarPelanggan;
    private HashMap<Integer, Layanan> daftarLayanan;
    private HashMap<Integer, Transaksi> daftarTransaksi;

    public SistemLaundry() {

        daftarPelanggan = new HashMap<>();
        daftarLayanan = new HashMap<>();
        daftarTransaksi = new HashMap<>();
    }

    // CRUD Pelanggan

    public void tambahPelanggan(Pelanggan pelanggan) {
        daftarPelanggan.put(
                pelanggan.getIdPelanggan(),pelanggan);
    }

    public Pelanggan cariPelanggan(int id) {
        return daftarPelanggan.get(id);
    }

    public void hapusPelanggan(int id) {
        daftarPelanggan.remove(id);
    }

    public void tampilSemuaPelanggan() {

        for (Pelanggan p : daftarPelanggan.values()) {
            System.out.println(p);
        }
    }

    // Layanan

    public void tambahLayanan(Layanan layanan) {

        daftarLayanan.put(layanan.getIdLayanan(),layanan);
    }

    public Layanan cariLayanan(int id) {
        return daftarLayanan.get(id);
    }

    // Transaksi

    public void tambahTransaksi(Transaksi transaksi) {

        daftarTransaksi.put(transaksi.getIdTransaksi(),transaksi);
    }
}