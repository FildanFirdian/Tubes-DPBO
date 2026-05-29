package sistem;

import java.util.Collection;

public class LaporanTransaksi implements CetakDokumen {

    private Collection<Transaksi> daftarTransaksi;

    public LaporanTransaksi(
            Collection<Transaksi> daftarTransaksi) {

        this.daftarTransaksi = daftarTransaksi;
    }

    @Override
    public void cetakDokumen() {

        System.out.println("===== LAPORAN TRANSAKSI =====");

        for (Transaksi t : daftarTransaksi) {

            System.out.println("ID : " + t.getIdTransaksi() + " | Total : " + t.getTotalHarga());
        }
    }
}